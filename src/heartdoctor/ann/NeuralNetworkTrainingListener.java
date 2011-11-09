/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

/**
 *
 * @author empitness
 */
public interface NeuralNetworkTrainingListener {

  public void updateTrainingSetAccuracy(double accuracy);
  public void updateTrainingSetMSE(double mse);

  public void updateGeneralizationSetAccuracy(double accuracy);
  public void updateGeneralizationSetMSE(double mse);

}
