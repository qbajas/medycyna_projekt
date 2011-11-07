package heartdoctor;

import heartdoctor.Util.DBUtil;
import heartdoctor.ann.DBDataLoader;
import heartdoctor.databasefiller.*;
import heartdoctor.ann.DataEntry;
import heartdoctor.ann.DataPreprocessor;
import heartdoctor.ann.DataSet;
import heartdoctor.ann.FileDataLoader;
import heartdoctor.application.Application;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author michal
 */
public class HeartDoctor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
          new Application();
          //testDB();
	  //testANN();
    }
    
    public static void testDB(){
        DBDataLoader loader=new DBDataLoader();
        DataSet data=loader.loadData();
        System.out.println("Rozmiar data: "+data.entries.size());
        System.out.println("10 pierwszych rekordow:");
        for(int i=0;i<10;++i)
            System.out.println(data.entries.get(i));
    }

  public static void testANN()
  {
	FileDataLoader dataLoader = new FileDataLoader("processed.cleveland.data");
	DataSet data = dataLoader.loadData();

	DataPreprocessor preprocessor = new DataPreprocessor();
	preprocessor.preprocessData(data);

	for (DataEntry e : data.entries)
	{
	  for (Double v : e.patterns)
	  {
		System.out.print(v + ", ");
	  }
	  System.out.println();
	}
  }
  public static void fillDB()
  {
		DataBaseFiller dbf=new DataBaseFiller();
        dbf.fillDB();
  }
}
