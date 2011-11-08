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

  private double _totalError;
  private double[][] _errorGradients;
  private double[][] _prevErrorGradients;
  private double[][][] _weights;
  private double[] _prevWeights;
  private double[][] _neuronValues;
  private double[] _conjGradients;
  private boolean _firstRun;
  private double[][][] _deltaWeights;

  private double _trainingSetAccuracy;
  private double _trainingSetMSE;

  // learning rate
  // momentum

  public NeuralNetworkTrainer(NeuralNetwork network)
  {
	_network = network;
	_evaluator = new NeuralNetworkEvaluator(network);

	_prevErrorGradients = (double[][])_evaluator.getErrorGradients().clone();
	_totalError = 0;
	_firstRun = true;
	//_conjGradients = new double[]

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
	double maxEpoch = 1000;

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
	for (int i = 0; i < _errorGradients.length; ++i)
	  for (int j = 0; j < _errorGradients[i].length; ++j)
		_prevErrorGradients[i][j] = _errorGradients[i][j];

	_evaluator.update(desiredOutputValues);
	_errorGradients = _evaluator.getErrorGradients();
	_neuronValues = _evaluator.getNeuronValues();

//	_totalError = 0;
//	for (int i = 0; i < _network.getNumOutputs(); ++i)
//	  _totalError += Math.pow(_neuronValues[_network.getNumHiddenLayers()][i] - desiredOutputValues[i], 2 );
//	_totalError /= _network.getNumOutputs();
	
	updateWeights();
  }

  private void updateWeights()
  {
	// dW(t) = u(t).d(t) + L.dW(t-1) - E.sgn(W(t))
	// W(t+1) = W(t) + dW(t)
	// u(t) = u0 + T.E(t-1)
	// d(t) = -VE(W(t)) + B(t).d(t-1)
	// d(0) = -VE(W(0))
	// B(t) = ( (VE(W(t)) - VE(W(t-1))) . VE(W(t)) ) / ( VE(W(t-1)) ^ 2 )

//	double u = Params.PresetLearningRate + Params.LearningRateAdjust * _totalError;
//	if (_firstRun)
//	{
//
//	}
//	else
//	{
//
//	}

	for (int l = 0; l < _deltaWeights.length; ++l)
	{
	  for (int n = 0; n < _deltaWeights[l].length; ++n)
	  {
		for (int w = 0; w < _deltaWeights[l][n].length; ++w)
		{
		  _deltaWeights[l][n][w] = 
				  Params.PresetLearningRate * _neuronValues[l][w] * _errorGradients[l][n] +
				  Params.MomentumConst * _deltaWeights[l][n][w];
		  _weights[l][n][w] += _deltaWeights[l][n][w];
		}
	  }
	}
  }

//  private double weightErrorGradient(int weight, boolean current)
//  {
//	int layer;
//	int neuron;
//
//	if (weight < _network.getNumInputs() * _network.getNumNeuronsPerHiddenLayer())
//	{
//	  layer = 0;
//	  neuron = weight / _network.getNumInputs();
//	}
//	else
//	{
//	  weight -= _network.getNumInputs() * _network.getNumNeuronsPerHiddenLayer();
//
//	  int numLayers = weight / (_network.getNumNeuronsPerHiddenLayer() * _network.getNumNeuronsPerHiddenLayer());
//	  int numWeights = weight % (_network.getNumNeuronsPerHiddenLayer() * _network.getNumNeuronsPerHiddenLayer());
//
//	  layer = 1 + numLayers;
//	  neuron = numWeights / _network.getNumNeuronsPerHiddenLayer();
//	}
//
//	if (current)
//	  return _errorGradients[layer][neuron] * _weights[weight];
//	else
//	  return _prevErrorGradients[layer][neuron] * _prevWeights[weight];
//  }

}
