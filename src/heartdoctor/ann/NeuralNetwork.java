/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author empitness
 */
public class NeuralNetwork implements Serializable{
  
  private ArrayList<NeuronLayer> _layers;
  private int _numInputs;
  private int _numOutputs;
  private int _numHiddenLayers;
  private int _numNeuronsPerHiddenLayer;

  private ArrayList<ArrayList<Double> > _neuronValues;

  public int getNumInputs() { return _numInputs; }
  public int getNumOutputs() { return _numOutputs; }
  public int getNumHiddenLayers() { return _numHiddenLayers; }
  public int getNumNeuronsPerHiddenLayer() { return _numNeuronsPerHiddenLayer; }

  public ArrayList<ArrayList<Double> > getNeuronValues() { return _neuronValues; }

  public NeuralNetwork(int numInputs, int numOutputs, int numHiddenLayers, int numNeuronsPerHiddenLayer)
  {
	_numInputs = numInputs;
	_numOutputs = numOutputs;
	_numHiddenLayers = numHiddenLayers;
	_numNeuronsPerHiddenLayer = numNeuronsPerHiddenLayer;

	_layers = new ArrayList<NeuronLayer>();
	_neuronValues = new  ArrayList<ArrayList<Double>>();

	if (numHiddenLayers > 0)
	{
	  // utworz pierwsza warstwe ukryta (na wejsciach inputy sieci)
	  _layers.add(new NeuronLayer(numNeuronsPerHiddenLayer, numInputs));

	  // utworz posrednie warstwy ukryte
	  for (int i = 0; i < numHiddenLayers - 1; ++i)
	  {
		_layers.add(new NeuronLayer(numNeuronsPerHiddenLayer, numNeuronsPerHiddenLayer));
	  }

	  // utworz warstwe wyjsciowa (na wyjsciach outputy sieci)
	  _layers.add(new NeuronLayer(numOutputs, numNeuronsPerHiddenLayer));
	}
	else
	{
	  _layers.add(new NeuronLayer(numOutputs, numInputs));
	}
  }

  public double[][][] getWeights()
  {
	double[][][] weights = new double[_numHiddenLayers+1][][];

	for (int l = 0; l < _layers.size(); ++l)
	{
	  NeuronLayer layer = _layers.get(l);
	  weights[l] = new double[layer.getNumNeurons()][];
	  for (int n = 0; n < layer.getNumNeurons(); ++n)
	  {
		Neuron neuron = layer.neurons.get(n);
		weights[l][n] = new double[neuron.getNumInputs()];
		for (int w = 0; w < neuron.getNumInputs(); ++w)
		{
		  weights[l][n][w] = neuron.inputWeights.get(w);
		}
	  }
	}

	return weights;
  }

  public void setWeights(double[][][] weights)
  {
	for (int l = 0; l < _layers.size(); ++l)
	{
	  NeuronLayer layer = _layers.get(l);
	  for (int n = 0; n < layer.getNumNeurons(); ++n)
	  {
		Neuron neuron = layer.neurons.get(n);
		for (int w = 0; w < neuron.getNumInputs(); ++w)
		{
		  neuron.inputWeights.set(w, weights[l][n][w]);
		}
	  }
	}
  }

  public ArrayList<Double> feedForward(ArrayList<Double> inputs)
  {
	assert inputs != null && inputs.size() == _numInputs;

	if (inputs == null)
	  throw new RuntimeException("dostalem nullowy input");
	if (inputs.size() != _numInputs)
	  throw new RuntimeException("dostalem input o zlej wielkosci");

	for (int i = 0; i < inputs.size(); ++i)
	{
	  if (inputs.get(i) == null)
		throw new RuntimeException("pole inputu nr " + i + " jest nullem! wtf!?");
	}

	ArrayList<Double> outputs = new ArrayList<Double>();

	_neuronValues.clear();
	_neuronValues.add( (ArrayList<Double>)inputs.clone() );

	for (int l = 0; l < _layers.size(); ++l)
	{
	  NeuronLayer layer = _layers.get(l);

	  if (l > 0)
	  {
		inputs = (ArrayList<Double>)outputs.clone();
	  }

	  outputs.clear();

	  for (int n = 0; n < layer.getNumNeurons(); ++n)
	  {
		Neuron neuron = layer.neurons.get(n);

		int currentInput = 0;
		double netInput = 0;

		// zsumuj iloczyny wejsc i odpowiadajych im wag
		for (int w = 0; w < neuron.getNumInputs() - 1; ++w)
		{
		  netInput += neuron.inputWeights.get(w) * inputs.get(currentInput++);
		}

		// dodaj bias aktywacji neuronu
		netInput += neuron.inputWeights.get(neuron.getNumInputs()-1) * Params.NeuronActivationBias;

		// przepusc sume wazonych wejsc i biasu przez funkcje aktywacji i dodaj do wyjscia warstwy
		outputs.add( activationFunc(netInput, Params.NeuronActivationResponse) );
	  }

	  _neuronValues.add( (ArrayList<Double>)outputs.clone() );
	}

	return outputs;
  }

  private double activationFunc(double input, double response)
  {
	// sigmoid
	return ( 1.0 / ( 1.0 + Math.exp(-input / response)) );
  }
}
