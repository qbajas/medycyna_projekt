/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

import java.util.ArrayList;

/**
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

  public void setLearningRate(double x) { _learningRate = x; }
  public void setLearningRateAdjust(double x) { _learningRateAdjust = x; }
  public void setMomentumConst(double x) { _momentumConst = x; }
  public void setForgettingRate(double x) { _forgettingRate = x; }

  public double getValidationSetAccuracy() { return _validationSetAccuracy; }
  public double getValidationSetMSE() { return _validationSetMSE; }

  public void setDesiredAccuracy(double accuracy) { _desiredAccuracy = accuracy; }
  public void setMaxEpochs(int epochs) { _maxEpochs = epochs; }

  public void addListener(NeuralNetworkTrainingListener listener) { 
      _listeners.add(listener); 
  }
  
  public void removeListener(NeuralNetworkTrainingListener listener) { 
      _listeners.remove(listener); 
  }

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

  private void backpropagate(double[] desiredOutputs)
  {
	_evaluator.update(desiredOutputs);
	_weights = _network.getWeights();
	_errorGradients = _evaluator.getErrorGradients();
	_neuronValues = _evaluator.getNeuronValues();
	
	updateWeights();
  }

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

  public void interrupt(){
      interrupt=true;
  }
}
