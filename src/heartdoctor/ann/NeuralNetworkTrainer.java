/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

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

	double desiredAccuracy = 100;
	double maxEpoch = 2000;

	_trainingSetAccuracy = 0;
	_trainingSetMSE = 0;

	double generalizationAccuracy = 0;
	double generalizationMSE = 0;

	while ( (_trainingSetAccuracy < desiredAccuracy || generalizationAccuracy < desiredAccuracy) &&
			epoch < maxEpoch )
	{
	  double prevTAccuracy = _trainingSetAccuracy;
	  double prevGAccuracy = generalizationAccuracy;

	  runTrainingEpoch(trainingSet);

	  generalizationAccuracy = _evaluator.calcDataSetAccuracy(generalizationSet);
	  generalizationMSE = _evaluator.calcDataSetMSE(generalizationSet);

	  ++epoch;

	  System.out.printf("Epoch %d: accuracy %f, MSE %f\n", epoch, generalizationAccuracy, generalizationMSE);
	}

	double validationAccuracy = _evaluator.calcDataSetAccuracy(validationSet);
	double validationMSE = _evaluator.calcDataSetMSE(validationSet);

	System.out.printf("Gotowe! Epoch: %d, ValidAccuracy: %f, ValidMSE: %f\n", epoch, validationAccuracy, validationMSE);
  }

  private void runTrainingEpoch(DataSet trainingSet)
  {
	double incorrectPatterns = 0;
	double mse = 0;

	for (int i = 0; i < trainingSet.entries.size(); ++i)
	{
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
		}

		mse += Math.pow(_neuronValues[_network.getNumHiddenLayers()+1][j] - (double)entry.targets.get(j), 2);
	  }

	  if (!patternCorrect)
		++incorrectPatterns;
	}

	_trainingSetAccuracy = 100 - ((double)incorrectPatterns / trainingSet.entries.size() * 100);
	_trainingSetMSE = mse / ( _network.getNumOutputs() * trainingSet.entries.size() );
  }

  private void backpropagate(double[] desiredOutputValues)
  {
	_evaluator.update(desiredOutputValues);
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
		  
		  _deltaWeights[l][n][w] = 
				  Params.PresetLearningRate * (w < _neuronValues[l].length? _neuronValues[l][w] : -1) * _errorGradients[l][n] +
				  Params.MomentumConst * _deltaWeights[l][n][w];
		  _weights[l][n][w] += _deltaWeights[l][n][w];
		}
	  }
	}

	_network.setWeights(_weights);
  }

}
