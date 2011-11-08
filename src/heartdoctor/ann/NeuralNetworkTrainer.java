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
  private double[] _weights;
  private double[] _prevWeights;
  private double[] _deltaWeights;
  private double[][] _neuronValues;
  private double[] _conjGradients;
  private boolean _firstRun;

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
  }

  public void trainNetwork()
  {
	
  }

  private void runTrainingEpoch()
  {

  }

  private void backpropagate(double[] desiredOutputValues)
  {
	for (int i = 0; i < _errorGradients.length; ++i)
	  for (int j = 0; j < _errorGradients[i].length; ++j)
		_prevErrorGradients[i][j] = _errorGradients[i][j];

	_evaluator.update(desiredOutputValues);
	_errorGradients = _evaluator.getErrorGradients();
	_neuronValues = _evaluator.getNeuronValues();

	_totalError = 0;
	for (int i = 0; i < _network.getNumOutputs(); ++i)
	  _totalError += Math.pow(_neuronValues[_network.getNumHiddenLayers()][i] - desiredOutputValues[i], 2 );
	_totalError /= _network.getNumOutputs();
	
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
