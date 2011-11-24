/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

/**
 * Interfejs nasłuchiwacza postępu nauki sieci neuronowej.
 * Patrz: NeuralNetworkTrainer.addListener().
 *
 * @author empitness
 */
public interface NeuralNetworkTrainingListener {

  /*
   * Przekazuje aktualną skuteczność sieci na zbiorze treningowym
   *
   * @param accuracy skuteczność
   */
  public void updateTrainingSetAccuracy(double accuracy);

  /*
   * Przekazuje aktualny błąd MSE sieci na zbiorze treningowym
   *
   * @param mse błąd średniokwadratowy
   */
  public void updateTrainingSetMSE(double mse);

  /*
   * Przekazuje aktualną skuteczność sieci na zbiorze generalizacyjnym
   *
   * @param accuracy skuteczność
   */
  public void updateGeneralizationSetAccuracy(double accuracy);

    /*
   * Przekazuje aktualny błąd MSE sieci na zbiorze generalizacyjnym
   *
   * @param mse błąd średniokwadratowy
   */
  public void updateGeneralizationSetMSE(double mse);

  /*
   * Informuje o rozpoczęciu kolejnej epoki nauki.
   */
  public void nextEpoch();

}
