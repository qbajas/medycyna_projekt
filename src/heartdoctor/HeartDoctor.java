package heartdoctor;

import heartdoctor.Util.DBUtil;
import heartdoctor.ann.DataEntry;
import heartdoctor.ann.DataPreprocessor;
import heartdoctor.ann.DataSet;
import heartdoctor.ann.FileDataLoader;
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
        //testDB();
	  testANN();
    }
    
    public static void testDB(){
        Connection conn=DBUtil.getConnection();
        Statement stm=null;
        ResultSet rs=null;
        
        try{
            stm=conn.createStatement();
             rs=stm.executeQuery("select name from test");
            while(rs.next()){
                System.out.println(rs.getString(1));
            }
        } catch (SQLException ex){
            System.err.println(ex);
        } finally {
            DBUtil.close(stm, rs);
        }
        DBUtil.close(conn);
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
}
