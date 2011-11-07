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

}
