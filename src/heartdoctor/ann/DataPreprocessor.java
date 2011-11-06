/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

/**
 *
 * @author empitness
 */
public class DataPreprocessor {

  // poddaje dane wstepnej obrobce przed wrzuceniem do ANN
  public void preprocessData(DataSet data)
  {
	cleanData(data);
	normalizeData(data);
  }

  // uzupelnia puste pola w probkach
  private void cleanData(DataSet data)
  {
	double[] mean = new double[13];
	int[] meanSamples = new int[13];
	
	for (int i = 0; i < mean.length; ++i)
	  mean[i] = 0;
	for (int i = 0; i < meanSamples.length; ++i)
	  meanSamples[i] = 0;

	for (int i = 0; i < data.entries.size(); ++i)
	{
	  DataEntry entry = data.entries.get(i);
	  for (int j = 0; j < entry.patterns.size(); ++j)
	  {
		Double v = entry.patterns.get(j);
		if (v != null)
		{
		  mean[j] += v;
		  ++meanSamples[j];
		}
	  }
	}

	for (int i = 0; i < mean.length; ++i)
	{
	  if (meanSamples[i] > 0)
		mean[i] /= meanSamples[i];
	}

	for (int i = 0; i < data.entries.size(); ++i)
	{
	  DataEntry entry = data.entries.get(i);
	  for (int j = 0; j < entry.patterns.size(); ++j)
	  {
		if (entry.patterns.get(j) == null)
		  entry.patterns.set(j, mean[j]);
	  }
	}
  }

  // normalizuje dane do odbiowiednich zakresow numerycznych
  private void normalizeData(DataSet data)
  {
	/*
	  Atrybuty:
      -- 1. #3  (age)		0-100
      -- 2. #4  (sex)		0,1
      -- 3. #9  (cp)		1-4
      -- 4. #10 (trestbps)	70-190
      -- 5. #12 (chol)		100-400
      -- 6. #16 (fbs)		0,1
      -- 7. #19 (restecg)	0,1,2
      -- 8. #32 (thalach)	100-200
      -- 9. #38 (exang)		0,1
      -- 10. #40 (oldpeak)	0-4
      -- 11. #41 (slope)	1,2,3
      -- 12. #44 (ca)		0-3
      -- 13. #51 (thal)		3,6,7
      ** 14. #58 (num)      0-4          (the predicted attribute)
	 */

	for (DataEntry e : data.entries)
	{
	  for (int i = 0; i < e.patterns.size(); ++i)
	  {
		double v = e.patterns.get(i);

		switch (i)
		{
		  case 0:
			v = linearNorm(v, 0, 100);
			break;
		  case 1:
			break;
		  case 2:
			break;
		  case 3:
			v = linearNorm(v, 70, 190);
			break;
		  case 4:
			v = linearNorm(v, 100, 400);
			break;
		  case 5:
			break;
		  case 6:
			break;
		  case 7:
			v = linearNorm(v, 100, 200);
			break;
		  case 8:
			break;
		  case 9:
			break;
		  case 10:
			break;
		  case 11:
			break;
		  case 12:
			break;
		}

		e.patterns.set(i, v);
	  }
	}
  }

  private double linearNorm(double val, double min, double max)
  {
	return (val - min) / (max - min);
  }

}
