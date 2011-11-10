/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

import java.util.Random;

/**
 *
 * @author empitness
 */
public class Utils {

  private static Random _random;

  static {
	_random = new Random(System.currentTimeMillis());
  }

  public static double random()
  {
	return _random.nextDouble() * 2.0 - 1.0;
  }

}
