/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author empitness
 */
public class FileDataLoader implements DataLoader {

  private String _file;

  public FileDataLoader(String file)
  {
	_file = file;
  }

  public DataSet loadData()
  {
	DataSet data = new DataSet();

	try
	{
	  FileInputStream fstream = new FileInputStream(_file);
	  DataInputStream in = new DataInputStream(fstream);
	  BufferedReader br = new BufferedReader(new InputStreamReader(in));

	  String dataLine;
	  while ( (dataLine = br.readLine()) != null )
	  {
		String[] fields = dataLine.split(",");
		assert fields.length == 14;

		DataEntry entry = new DataEntry();
		for (int i = 0; i < fields.length; ++i)
		{
		  String f = fields[i].trim();

		  Double fVal = null;
		  if (!f.equals("?"))
			fVal = Double.parseDouble(f);
		  
		  if (i == 13)
			entry.targets.add(fVal);
		  else
			entry.patterns.add(fVal);
		}

		data.entries.add(entry);
	  }

	  in.close();
	}
	catch (Exception e)
	{
	  System.err.println("FileDataLoader.loadData(): " + e.getMessage());
	}

	return data;
  }


}
