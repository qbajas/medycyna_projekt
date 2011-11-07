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
public class DataEntry {

  public ArrayList<Double> patterns = new ArrayList<Double>(14);
  public ArrayList<Double> targets = new ArrayList<Double>(1);

    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();
        builder.append("patterns:\n");
        for(Double d:patterns)
            builder.append(d).append(" ");
        builder.append("\nTargets: ");
        
        for(Double d:targets)
            builder.append(d).append(" ");
        
        return builder.toString();
    }

  
}
