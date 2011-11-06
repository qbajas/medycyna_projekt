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
public class Neuron {

  public ArrayList<Double> inputWeights;

  public Neuron(int numInputs)
  {
	// jedna dodatkowa waga na bias
	inputWeights = new ArrayList<Double>(numInputs + 1);
	for (int i = 0; i < inputWeights.size(); ++i)
	{
	  inputWeights.set(i, Utils.random());
	}
  }

  public int getNumInputs()
  {
	return inputWeights.size();
  }

}
