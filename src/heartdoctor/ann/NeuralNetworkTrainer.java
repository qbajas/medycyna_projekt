/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

import java.util.ArrayList;

/**
 * Klasa trenująca sieć neuronową.
 *
 * @author empitness
 */
public class NeuralNetworkTrainer {

  private NeuralNetwork _network;
  private NeuralNetworkEvaluator _evaluator;

  private double[][] _errorGradients;
  private double[][][] _weights;
  private double[][] _neuronValues;
  private double[][][] _deltaWeights;

  private double _trainingSetAccuracy;
  private double _trainingSetMSE;

  private double _learningRate = Params.DefaultPresetLearningRate;
  private double _learningRateAdjust = Params.DefaultLearningRateAdjust;
  private double _momentumConst = Params.DefaultMomentumConst;
  private double _forgettingRate = Params.DefaultForgettingConst;

  private double _validationSetAccuracy;
  private double _validationSetMSE;

  private double _desiredAccuracy = 100;
  private int _maxEpochs = 4000;
  
  boolean interrupt=false;

  private ArrayList<NeuralNetworkTrainingListener> _listeners = 
          new ArrayList<NeuralNetworkTrainingListener>();

  /*
   * Ustawia tempo nauki sieci
   *
   * @param x tempo nauki
   */
  public void setLearningRate(double x) { _learningRate = x; }
  /*
   * Ustawia dostosowanie tempa nauki sieci
   *
   * @param x dostosowanie tempa nauki
   */
  public void setLearningRateAdjust(double x) { _learningRateAdjust = x; }
  /*
   * Ustawia współczynik pędu nauki
   *
   * @param x pęd nauki
   */
  public void setMomentumConst(double x) { _momentumConst = x; }
  /*
   * Ustawia współczynnik zapominania sieci
   *
   * @param x wsp. zapominania sieci
   */
  public void setForgettingRate(double x) { _forgettingRate = x; }

  /*
   * Zwraca skuteczność sieci dla zbioru walidacyjnego
   *
   * @return skuteczność sieci [0.0,100.0]
   */
  public double getValidationSetAccuracy() { return _validationSetAccuracy; }
  /*
   * Zwraca błąd średniokwadratowy sieci dla zbioru walidacyjnego
   *
   * @return MSE zbioru walidacyjnego
   */
  public double getValidationSetMSE() { return _validationSetMSE; }

  /*
   * Ustala oczekiwaną skuteczność sieci po nauce
   *
   * @param accuracy ozcekiwana skuteczność [0.0,100.0]
   */
  public void setDesiredAccuracy(double accuracy) { _desiredAccuracy = accuracy; }
  /*
   * Ustala maksymalną ilość epok nauki sieci
   *
   * @param epochs maks. ilość epok
   */
  public void setMaxEpochs(int epochs) { _maxEpochs = epochs; }

  /*
   * Dodaje nasłuwicza postępu nauki sieci.
   *
   * @param listener nasłuchiwacz
   */
  public void addListener(NeuralNetworkTrainingListener listener) { 
      _listeners.add(listener); 
  }

  /*
   * Usuwa nasłuwiacza postępu nauki sieci
   *
   * @param listener nasłuchiwacz do usunięcia
   */
  public void removeListener(NeuralNetworkTrainingListener listener) { 
      _listeners.remove(listener); 
  }

  /*
   * TWorzy nową instancję trenera sieci.
   *
   * @param network sieć do nauki
   */
  public NeuralNetworkTrainer(NeuralNetwork network)
  {
	_network = network;
	_evaluator = new NeuralNetworkEvaluator(network);

	_weights = _network.getWeights();
	_deltaWeights = _weights.clone();
	for(int l = 0; l < _deltaWeights.length; ++l)
	  for (int n = 0; n < _deltaWeights[l].length; ++n)
		for (int w = 0; w < _deltaWeights[l][n].length; ++w)
		  _deltaWeights[l][n][w] = 0;
  }

  /*
   * Trenuje sieć neuronową.
   *
   * @param trainingSet zbiór treningowy sieci
   * @param generalizationSet zbiór generalizacyjny sieci
   * @param validationSet zbiór walidacyjny sieci
   */
  public void trainNetwork(DataSet trainingSet, DataSet generalizationSet, DataSet validationSet)
  {
	int epoch = 0;

	_trainingSetAccuracy = 0;
	_trainingSetMSE = 0;

	double generalizationAccuracy = 0;
	double generalizationMSE = 0;

	while ( (_trainingSetAccuracy < _desiredAccuracy || generalizationAccuracy < _desiredAccuracy) &&
			epoch < _maxEpochs )
	{
          if(interrupt)
              return;
	  runTrainingEpoch(trainingSet);
          
	  generalizationAccuracy = _evaluator.calcDataSetAccuracy(generalizationSet);
	  generalizationMSE = _evaluator.calcDataSetMSE(generalizationSet);
          
	  for(NeuralNetworkTrainingListener _listener: _listeners){
		_listener.updateGeneralizationSetAccuracy(generalizationAccuracy);
		_listener.updateGeneralizationSetMSE(generalizationMSE);
                _listener.nextEpoch();
	  }

	  ++epoch;
          
          
	 // System.out.printf("Epoch %d: accuracy %f, MSE %f\n", epoch, generalizationAccuracy, generalizationMSE);
	}

	_validationSetAccuracy = _evaluator.calcDataSetAccuracy(validationSet);
	_validationSetMSE = _evaluator.calcDataSetMSE(validationSet);

	System.out.printf("Gotowe! Epoch: %d, ValidAccuracy: %f, ValidMSE: %f\n", epoch, _validationSetAccuracy, _validationSetMSE);
  }

  /*
   * Przeprowadza pojedynczą iterację nauki sieci.
   *
   * @param trainingSet zbiór treningowy sieci
   */
  private void runTrainingEpoch(DataSet trainingSet)
  {
	double incorrectPatterns = 0;

	for (int i = 0; i < trainingSet.entries.size(); ++i)
	{
	 // System.out.println("obrabiam probke nr " + i);
          if(interrupt)
              return;
	  DataEntry entry = trainingSet.entries.get(i);
	  double[] outputs = new double[entry.targets.size()];
	  for (int j = 0; j < outputs.length; ++j)
		outputs[j] = entry.targets.get(j);

	  _network.feedForward(entry.patterns);
	  backpropagate(outputs);

	  boolean patternCorrect = true;

	  for (int j = 0; j < _network.getNumOutputs(); ++j)
	  {
		if (_evaluator.roundOutputValue(_neuronValues[_network.getNumHiddenLayers()+1][j]) != (int)(double)entry.targets.get(j))
		{
		  patternCorrect = false;
		  break;
		}
	  }

	  if (!patternCorrect)
		++incorrectPatterns;
	}

	_trainingSetAccuracy = 100 - ((double)incorrectPatterns / trainingSet.entries.size() * 100);
	_trainingSetMSE = _evaluator.calcDataSetMSE(trainingSet);

	for(NeuralNetworkTrainingListener _listener:_listeners)
	{
	  _listener.updateTrainingSetAccuracy(_trainingSetAccuracy);
	  _listener.updateTrainingSetMSE(_trainingSetMSE);
	}
  }

  /*
   * Uaktualnia wagi połączęń neuronowych po iteracji nauki sieci na podstawie
   * obliczonych gradientów błędów.
   *
   * @param desiredOutputs oczekiwana wartości wyjściowe sieci
   */
  private void backpropagate(double[] desiredOutputs)
  {
	_evaluator.update(desiredOutputs);
	_weights = _network.getWeights();
	_errorGradients = _evaluator.getErrorGradients();
	_neuronValues = _evaluator.getNeuronValues();
	
	updateWeights();
  }

  /*
   * Uaktualnia wagi połączeń neuronowych.
   */
  private void updateWeights()
  {
	for (int l = 0; l < _deltaWeights.length; ++l)
	{
	  for (int n = 0; n < _deltaWeights[l].length; ++n)
	  {
		for (int w = 0; w < _deltaWeights[l][n].length; ++w)
		{
		  
		  _deltaWeights[l][n][w] = (_learningRate + _learningRateAdjust * _trainingSetMSE) *
				  (w < _neuronValues[l].length? _neuronValues[l][w] : -1) * _errorGradients[l][n] +
				  _momentumConst * _deltaWeights[l][n][w] -
				  Math.signum(_weights[l][n][w]) * _forgettingRate;
		  _weights[l][n][w] += _deltaWeights[l][n][w];
		}
	  }
	}

	_network.setWeights(_weights);
  }

  /*
   * Przerywa ziom xD
   */
  public void interrupt(){
      interrupt=true;
  }
}
