package heartdoctor;

import heartdoctor.Util.DBUtil;
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
        testDB();
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
}
