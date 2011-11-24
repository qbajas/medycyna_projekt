/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

import java.util.ArrayList;

/**
 * Klasa obliczającą metryki określające aktualną skuteczność podanej sieci
 * neuronowej. Używana jest przede wszystkim przez NeuralNetworkTrainer
 * na etapie treningu.
 *
 * @author empitness
 */
public class NeuralNetworkEvaluator {

  /*
   * Sieć neuronowa poddawana ocenie
   */
  private NeuralNetwork _network;

  /*
   * Tablica wag połączęń neuronowych
   */
  private double[][][] _weights;

  /*
   * Tablica z wartościami gradientów błędu
   */
  private double[][] _errorGradients;

  /*
   * Tablica z wartościami neuronów
   */
  private double[][] _neuronValues;

  /*
   * Zwraca tablicę gradientów błędów
   *
   * @return tablica gradientów błędów
   */
  public double[][] getErrorGradients() { return _errorGradients; }

  /*
   * Zwraca tablicę wartości neuronów
   *
   * @return tablica wartości neuronów
   */
  public double[][] getNeuronValues() { return _neuronValues; }

  /*
   * Tworzy nową instancję ewaluatora sieci.
   *
   * @param network sieć neuronowa do oceny
   */
  public NeuralNetworkEvaluator(NeuralNetwork network)
  {
	_network = network;

	_weights = _network.getWeights();

	_errorGradients = new double[network.getNumHiddenLayers() + 1][];
	for (int i = 0; i < network.getNumHiddenLayers(); ++i)
	  _errorGradients[i] = new double[network.getNumNeuronsPerHiddenLayer()];
	_errorGradients[network.getNumHiddenLayers()] = new double[network.getNumOutputs()];

	_neuronValues = new double[network.getNumHiddenLayers() + 2][];
	_neuronValues[0] = new double[network.getNumInputs()];
	for (int i = 1; i < network.getNumHiddenLayers() + 1; ++i)
	  _neuronValues[i] = new double[network.getNumNeuronsPerHiddenLayer()];
	_neuronValues[network.getNumHiddenLayers() + 1] = new double[network.getNumOutputs()];
  }

  /*
   * Uaktualnia wewnetrzne struktury potrzebne do poprawnego obliczania
   * skutecznosci sieci. Nalezy wywolac po kazdym przepuszczenie danych
   * przez siec, przed pobieraniem informacji o skutecznosci dla tych danych.
   *
   * @param desiredOutputValues talbica z oczekiwanymi wartościami wyjściowymi
   * ostatniego przebiegu sieci
   */
  public void update(double[] desiredOutputValues)
  {
	// zczytaj wagi
	_weights = _network.getWeights();

	// zczytaj wartosci neuronow
	ArrayList<ArrayList<Double> > neurons = _network.getNeuronValues();
	for (int l = 0; l < neurons.size(); ++l)
	{
	  ArrayList<Double> layer = neurons.get(l);
	  for (int n = 0; n < layer.size(); ++n)
	  {
		_neuronValues[l][n] = layer.get(n);
	  }
	}


	// oblicz gradienty
	// najpierw neuronow outputu:
	for (int n = 0; n < _network.getNumOutputs(); ++n)
	{
	  _errorGradients[_network.getNumHiddenLayers()][n] =
			  calcErrorGradientOutput(desiredOutputValues[n], _neuronValues[_network.getNumHiddenLayers()+1][n]);
	}
	// potem neuronow warstw ukrytych:
	// najpierw ostatniej ukrytej:
	for (int n = 0; n < _network.getNumNeuronsPerHiddenLayer(); ++n)
	{
	  _errorGradients[_network.getNumHiddenLayers()-1][n] =
			  calcErrorGradientHiddenOutput(n);
	}
	// potem reszty:
	for (int l = _network.getNumHiddenLayers()-2; l >= 0; --l)
	{
	  for (int n = 0; n < _network.getNumNeuronsPerHiddenLayer(); ++n)
	  {
		_errorGradients[l][n] = calcErrorGradientHiddenHidden(l, n);
	  }
	}

  }

  /*
   * Oblicza błąd średniokwadratowy (ang. MSE - Mean Square Error) na podanym
   * zbiorze danych medycznych.
   *
   * @param data kolekcja danych medycznych
   * @return błąd średniokwadratowy sieci dla podanych danych
   */
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

  /*
   * Oblicza procentową skuteczność sieci na podanych danych wejściowych,
   * gdzie skuteczność to % liczba dobrze rozpoznanych próbek.
   *
   * @param data dane medyczne
   * @return skuteczność w przedziale 0.0-100.0
   */
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
		if ( roundOutputValue(outputs.get(i)) != (int)(double)entry.targets.get(i) )
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

  /*
   * Zaokrągla podaną wartość wyjściową sieci do binarnego wyniku.
   *
   * @param x wartość do zaokrąglenia
   * @return zaokrąglona wartość (0 - zdrowy, 1 - chory)
   */
  public int roundOutputValue(double x)
  {
//	if (x < 0.5) return 0;
//	else if (x < 1.5) return 1;
//	else if (x < 2.5) return 2;
//	else if (x < 3.5) return 3;
//	else if (x < 4.5) return 4;
//	else return -1;

	if (x > 0.7) return 1;
	else return 0;
  }

  /*
   * Zwraca wczesniej obliczony w update() gradient bledu dla
   * neurona w konkretnej warstwie, gdzie 0 - pierwsza warstwa ukryta.
   *
   * @param layer numer warstwy
   * @param neuron numer neurona
   * @return gradient błędu dla podanego neurona w podanej warstwie
   */
  public double getErrorGradient(int layer, int neuron)
  {
	return _errorGradients[layer][neuron];
  }

  /*
   * Oblicza gradient bledu neurona wyjsciowego.
   *
   * @param desiredVal oczekiwana wartośc wyjściowa
   * @param outputVal otrzymana wartość wyjściowa
   * @return gradient błędu neurona wyjściowego
   */
  private double calcErrorGradientOutput(double desiredVal, double outputVal)
  {
	return outputVal * (1.0 - outputVal) * (desiredVal - outputVal);
  }

  /*
   * Oblicza gradient bledu neurona ostatniej warstwy ukrytej.
   *
   * @param neuron numer neurona
   * @return gradient błędu
   */
  private double calcErrorGradientHiddenOutput(int neuron)
  {
	double weightedSum = 0;
	for (int i = 0; i < _network.getNumOutputs(); ++i)
	  weightedSum += getWeight(_network.getNumHiddenLayers(), i, neuron) * _errorGradients[_network.getNumHiddenLayers()][i];

	double neuronVal = _neuronValues[_network.getNumHiddenLayers()][neuron];
	return neuronVal * (1.0 - neuronVal) * weightedSum;
  }

  /*
   * Oblicza gradient bledu neurona posredniej warstwy ukrytej.
   *
   * @param layer numer warstwy
   * @param neuron numer neurona
   * @return gradient błędu
   */
  private double calcErrorGradientHiddenHidden(int layer, int neuron)
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
   *
   * @param nextLayer numer "przedniej" warstwy
   * @param nextNeuron numer neurona w "przedniej" warstwie
   * @param prevNeuron numer neurona w "tylnej" warstwie
   * @return waga wybranego połączenia neuronowego
   */
  private double getWeight(int nextLayer, int nextNeuron, int prevNeuron)
  {
	return _weights[nextLayer][nextNeuron][prevNeuron];
  }

}
