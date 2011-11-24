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
 * Klasa ładująca dane medyczne pacjentów z pliku tekstowego o regularnym formacie.
 *
 * @author empitness
 */
public class FileDataLoader implements DataLoader {

  /*
   * Ścieżka do pliku tekstowego z danymi medycznymi
   */
  private String _file;

  /*
   * Tworzy nową instancję klasy, zapamiętująć ścieżkę do pliku z danymi.
   *
   * @param file ścieżka do pliku z danymi medycznymi
   */
  public FileDataLoader(String file)
  {
	_file = file;
  }

  /*
   * Ładuje dane medyczne z pliku tekstowego podanego w konstruktorze.
   * Próbki kolejnych pacjentów są oddzielone znakami nowej linii,
   * zaś poszczególne wartości badań oraz diagnoza w obrębie jednej
   * próbki - przecinkami.
   *
   * @return kolekcja próbek załadowanych z pliku
   */
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
		  
		  if (i == 13)
		  {
			entry.targets.add((double)Integer.parseInt(f));
		  }
		  else
		  {
			Double fVal = null;
			if (!f.equals("?"))
			  fVal = Double.parseDouble(f);
			entry.patterns.add(fVal);
		  }
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
