/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

import java.util.ArrayList;

/**
 * Interfejs dla klas odpowiedzialnych za ładowanie danych medycznych
 * dla sieci neuronowej z różnych źródeł.
 *
 * @author empitness
 */
public interface DataLoader {

  /*
   * Ładuje wszystkie wpisy (dane medyczne pacjentów - wyniki badań + diagnoza)
   * z pewnego źródła i zwraca ich kolekcję.
   */
  public DataSet loadData();

}
