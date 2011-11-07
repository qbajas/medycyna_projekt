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
public class NeuralNetworkEvaluator {

  private NeuralNetwork _network;
  private double[] _weights;
  private double[][] _errorGradients;
  private double[][] _neuronValues;

  public NeuralNetworkEvaluator(NeuralNetwork network)
  {
	_network = network;
  }

  // oblicza blad sredniokwardatowy sieci na podanych danych wejsciowych
  public double calcDataSetMSE(DataSet data)
  {
	double mse = 0;

	for (DataEntry entry : data.entries)
	{
	  // przepusc kazdy kolejny wpis przez siec
	  ArrayList<Double> outputs = _network.feedForward(entry.patterns);

	  // porownaj kazde wyjscie sieci z oczekiwana wartoscia
	  for (int i = 0; i < outputs.size(); ++i)
	  {
		mse += Math.pow(outputs.get(i) - entry.targets.get(i), 2);
	  }
	}

	mse /= (_network.getNumInputs() * data.entries.size());
	return mse;
  }

  // oblicza procentowana skutecznosc sieci na podstanych danych wejsciowych,
  // gdzie skutecznosc to % ilosc dobrze rozpoznanych danych
  public double calcDataSetAccuracy(DataSet data)
  {
	int incorrectResults = 0;

	for (DataEntry entry : data.entries)
	{
	  // przepusc kazdy koeljny wpis przez siec
	  ArrayList<Double> outputs = _network.feedForward(entry.patterns);

	  boolean correntResult = true;

	  // porownaj kazde wyjscie sieci z oczekiwana wartoscia
	  for (int i = 0; i < outputs.size(); ++i)
	  {
		if ( roundOutputValue(outputs.get(i)) != entry.targets.get(i) )
		{
		  correntResult = false;
		  break;
		}
	  }

	  if (!correntResult)
		++incorrectResults;
	}

	return 100 - ((double)incorrectResults / data.entries.size() * 100);
  }

  private int roundOutputValue(double x)
  {
	if (x < 0.5) return 0;
	else if (x < 1.5) return 1;
	else if (x < 2.5) return 2;
	else if (x < 3.5) return 3;
	else if (x < 4.5) return 4;
	else return -1;
  }

  /*
   * Oblicza gradient bledu neurona wyjsciowego.
   */
  private double errorGradientOutput(double desiredVal, double outputVal)
  {
	return outputVal * (1.0 - outputVal) * (desiredVal - outputVal);
  }

  /*
   * Oblicza gradient bledu neurona ostatniej warstwy ukrytej.
   */
  private double errorGradientHiddenOutput(int neuron)
  {
	double weightedSum = 0;
	for (int i = 0; i < _network.getNumOutputs(); ++i)
	  weightedSum += getWeight(_network.getNumHiddenLayers(), i, neuron) * _errorGradients[_network.getNumHiddenLayers()][i];

	double neuronVal = _neuronValues[_network.getNumHiddenLayers()-1][neuron];
	return neuronVal * (1.0 - neuronVal) * weightedSum;
  }

  /*
   * Oblicza gradient bledu neurona posredniej warstwy ukrytej.
   */
  private double errorGradientHiddenHidden(int layer, int neuron)
  {
	double weightedSum = 0;
	for (int i = 0; i < _network.getNumNeuronsPerHiddenLayer(); ++i)
	  weightedSum += getWeight(layer + 1, i, neuron) * _errorGradients[layer + 1][i];

	double neuronVal = _neuronValues[layer][neuron];
	return neuronVal * (1.0 - neuronVal) * weightedSum;
  }

  /*
   * Zwraca wage pomiedzy neuronem nextNeuron w warstwie nextLayer
   * a neuronem prevNeuron w warstwie wczesniejszej (nextLayer-1).
   */
  private double getWeight(int nextLayer, int nextNeuron, int prevNeuron)
  {
	int wIdx = 0;

	if (nextLayer == 0)
	{
	  // nextLayer - first hidden layer
	  // weights from input
	  wIdx = nextNeuron * _network.getNumInputs() + prevNeuron;
	}
	else
	{
	  // nextLayer - hidden/output layer
	  // weights from hidden

	  // dodaj indeksy wag pomiedzy pierwsza ukryta i inputem oraz neuronem w wybranej ukrytej
	  wIdx = _network.getNumNeuronsPerHiddenLayer() * _network.getNumInputs() +
			  nextNeuron * _network.getNumNeuronsPerHiddenLayer() +
			  prevNeuron;

	  // dodaj wagi posrednich warstw ukrytych
	  if (nextLayer > 1)
		wIdx += (nextLayer - 1) * _network.getNumNeuronsPerHiddenLayer() * _network.getNumNeuronsPerHiddenLayer();
	}

	return _weights[wIdx];
  }

}
