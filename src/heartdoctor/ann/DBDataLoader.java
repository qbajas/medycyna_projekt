/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.ann;

import heartdoctor.Util.DBUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michal
 */
public class DBDataLoader implements DataLoader {

    private static final String queryString = "select * from LearnDataSet";
    private static final String numberString = "select count(*) from LearnDataSet";

    public int getRecordsAmount() {
        Connection conn = DBUtil.getConnection();
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.createStatement();
            rs = stm.executeQuery(numberString);
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new RuntimeException("0 results returned");
        } catch (SQLException ex) {
            Logger.getLogger(DBDataLoader.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("0 results returned");
        } finally{
            DBUtil.close(stm, rs);
            DBUtil.close(conn);
        }
    }

    @Override
    public DataSet loadData() {
        Connection conn = DBUtil.getConnection();
        Statement stm = null;
        ResultSet rs = null;
        DataSet data = new DataSet(getRecordsAmount());
        DataEntry entry = null;
        try {
            stm = conn.createStatement();
            rs = stm.executeQuery(queryString);
            rs.setFetchSize(500);
            while (rs.next()) {
                entry=new DataEntry();
                for (int i = 2; i < 15; ++i) {
                    entry.patterns.add(rs.getDouble(i));
                }
                entry.targets.add(rs.getDouble(15));
                data.entries.add(entry);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBDataLoader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtil.close(stm, rs);
            DBUtil.close(conn);
        }
        return data;
    }
}
