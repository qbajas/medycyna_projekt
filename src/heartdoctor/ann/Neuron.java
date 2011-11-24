/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Klasa opisująca neuron sztucznej sieci neuronowej.
 *
 * @author empitness
 */
public class Neuron implements Serializable{
  private static final long serialVersionUID = -4141257806047709L;

  /*
   * Wagi połączeń neurnowych wchodzących do tego neurona od poprzedniej warstwy
   */
  public ArrayList<Double> inputWeights;

  /*
   * TWorzy nowego neurona o podanej ilości wag wejściowych
   *
   * @param numInputsi liczba wag wejściowych
   */
  public Neuron(int numInputs)
  {
	// jedna dodatkowa waga na bias
	inputWeights = new ArrayList<Double>();
	for (int i = 0; i < numInputs + 1; ++i)
	{
	  inputWeights.add(Utils.random());
	}
  }

  /*
   * Zwraca liczbę wag wejściowych neurona
   *
   * @return liczba wag wejściowych
   */
  public int getNumInputs()
  {
	return inputWeights.size();
  }

}
