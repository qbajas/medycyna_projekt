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
public class Neuron implements Serializable{

  public ArrayList<Double> inputWeights;

  public Neuron(int numInputs)
  {
	// jedna dodatkowa waga na bias
	inputWeights = new ArrayList<Double>();
	for (int i = 0; i < numInputs + 1; ++i)
	{
	  inputWeights.add(Utils.random());
	}
  }

  public int getNumInputs()
  {
	return inputWeights.size();
  }

}
