/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.DataModel;

/**
 *
 * @author michal
 */
public class NetworkStatistics implements Cloneable{
    public double tsAcc;
    public double tsMSE;
    public double gsAcc;
    public double gsMSE;
    public double valAcc;
    public double valMSE;
    
    public int hidden;
    public int perceptrons;
    
   @Override
   public NetworkStatistics clone() throws CloneNotSupportedException {
        return  (NetworkStatistics) super.clone();
  }
}
