/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Klasa opisująca warstwę sieci neuronowej.
 *
 * @author empitness
 */
public class NeuronLayer implements Serializable{
  private static final long serialVersionUID = -4629655462286488980L;

  /*
   * Lista neuronów w warstwie
   */
  public ArrayList<Neuron> neurons;

  /*
   * Tworzy nową instację warstwy sieci neuronowej
   *
   * @param numNeurons liczba neuronów w warstwie
   * @param numInputsPerNeuron liczba wejść neuronów w warstwie
   */
  public NeuronLayer(int numNeurons, int numInputsPerNeuron)
  {
	neurons = new ArrayList<Neuron>(numNeurons);
	for (int i = 0; i < numNeurons; ++i)
	{
	  neurons.add( new Neuron(numInputsPerNeuron) );
	}
  }

  /*
   * Zwraca liczbę neuronów w warstwei
   *
   * @return liczba neuronów
   */
  public int getNumNeurons()
  {
	return neurons.size();
  }

}
