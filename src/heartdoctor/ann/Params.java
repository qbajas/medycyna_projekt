/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

/**
 * Domyślne parametry sieci neuronowej.
 *
 * @author empitness
 */
public class Params {

  public static final double DefaultMomentumConst = 0.6;
  public static final double DefaultPresetLearningRate = 0.07;
  public static final double DefaultLearningRateAdjust = 0.01;
  public static final double DefaultForgettingConst = 0.000001;
  public static final double NeuronActivationBias = -1.0;
  public static final double NeuronActivationResponse = 1.0;
}
