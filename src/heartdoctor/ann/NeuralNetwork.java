/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Klasa sztucznej sieci neuronowej (ang. ANN - Artificial Neural Network).
 * Posiada trzy rodzaje warstw: wejściową, wyjściową i pośrednie ukryte.
 * Liczba warstw ukrytych, liczba neuronów w obrębie każdej takie warstwy
 * oraz wagi poszczególnych połączeń pomiędzy neuronami określają strukturę
 * tej sieci, opisującej jej działanie.
 *
 * @author empitness
 */
public class NeuralNetwork implements Serializable{
  private static final long serialVersionUID = -1562896790507503199L;

  /*
   * Lista warstw sieci (wejściowa, wyjściowa i ukryte)
   */
  private ArrayList<NeuronLayer> _layers;

  /*
   * Liczba wejść sieci (parametrów wejściowych)
   */
  private int _numInputs;

  /*
   * Liczba wyjść sieci (parametrów wyjściowych)
   */
  private int _numOutputs;

  /*
   * Liczba warstw ukrytych sieci
   */
  private int _numHiddenLayers;

  /*
   * Liczba neuronów w każdej warstwie ukrytej sieci
   */
  private int _numNeuronsPerHiddenLayer;

  /*
   * Wartości neuronów obliczone w ostatnim wywołaniu funkcji feedForward().
   * Pierwszy wymiar to kolejne warstwy sieci, drugi wymiar to kolejne neurony
   * wybranej warstwy.
   */
  private ArrayList<ArrayList<Double> > _neuronValues;

  /*
   * Zwraca ilość wejść sieci
   *
   * @return ilość wejść sieci
   */
  public int getNumInputs() { return _numInputs; }

  /*
   * Zwraca ilość wyjść sieci
   *
   * @return ilość wyjść sieci
   */
  public int getNumOutputs() { return _numOutputs; }

  /*
   * Zwraca liczbę warstw ukrytych sieci
   *
   * @return liczba warstw ukrytych sieci
   */
  public int getNumHiddenLayers() { return _numHiddenLayers; }

  /*
   * Zwraca liczbę neuronów w warstach ukrytych
   *
   * @return liczba neuronów w warstawch ukrytych
   */
  public int getNumNeuronsPerHiddenLayer() { return _numNeuronsPerHiddenLayer; }

  /*
   * Zwraca wartości neuronów we wszystkich warstwach, obliczonych w ostatnim
   * wywołaniu funkcji feedForward().
   *
   * @return lista wartości neuronów
   */
  public ArrayList<ArrayList<Double> > getNeuronValues() { return _neuronValues; }

  /*
   * Tworzy nową instację sztucznej sieci neuronowej.
   *
   * @param numInputs liczba wejść sieci
   * @param numOutputs liczba wyjść sieci
   * @param numHiddenLayers liczba warstw ukrytych sieci
   * @param numNeuronsPerHiddenLayer liczba neuronów w warstwach ukrytych sieci
   */
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

  /*
   * Zwraca wagi połączeń pomiędzy neurononami.
   * Pierwszy wymiar - numer warstwy.
   * Drugi wymiar - numer neurona.
   * Trzeci wymiar - numer połączenia wybranego neurona z poprzedzającą warstwą.
   *
   * @return tablica wag połączeń neuronowych
   */
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

  /*
   * Ustawia wagi połączeń pomiędzy neuronami.
   * Opis wymiarów tablicy - patrz komentarz do getWeights().
   *
   * @param weights tablica z wagami połączeń neuronowych
   */
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

  /*
   * Oblicza odpowiedź sieci na podane wejście.
   * Podane parametry wejściowe przypisywane są do neuronów warstwy
   * wejściowej, a następnie przechodzą przez całą sieć, przemnażane
   * przez odpowiednie wagi kolejnych odwiedzanych połączeń neuronowych.
   *
   * @param inputs lista wartości parametrów wejściowych sieci
   * @return lista wartości obliczonych parametrów wyjściowych sieci
   */
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

  /*
   * Funkcja aktywacji neurona (sigmoidalna).
   *
   * @param input wartość wejściowa (parametr)
   * @param response odpowiedź neurona (zwykle 1.0)
   * @return wartość aktywacji neurona
   */
  private double activationFunc(double input, double response)
  {
	// sigmoid
	return ( 1.0 / ( 1.0 + Math.exp(-input / response)) );
  }
}
