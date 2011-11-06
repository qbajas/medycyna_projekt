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
public class NeuronLayer {

  public ArrayList<Neuron> neurons;

  public NeuronLayer(int numNeurons, int numInputsPerNeuron)
  {
	neurons = new ArrayList<Neuron>(numNeurons);
	for (int i = 0; i < numNeurons; ++i)
	{
	  neurons.add( new Neuron(numInputsPerNeuron) );
	}
  }

  public int getNumNeurons()
  {
	return neurons.size();
  }

}
