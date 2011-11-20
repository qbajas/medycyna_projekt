/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.Util;

import heartdoctor.ann.NeuralNetwork;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author michal
 */
public class ANNSerializer {

    static final String WRITE_OBJECT_SQL = "INSERT INTO SerializedObjects(object_value) VALUES (?)";
    
    static final String READ_LAST = "SELECT * FROM SerializedObjects order by id limit 1";

    public static void writeANN(NeuralNetwork object,double accuracy) throws SQLException {
        Connection conn= DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement(WRITE_OBJECT_SQL);

        stm.setObject(1, object);
        stm.setDate(2, new java.sql.Date(new Date().getTime()));
        stm.setDouble(3, accuracy);
        int i=stm.executeUpdate();

        DBUtil.close(stm);
        DBUtil.close(conn);
        if(i==0)
            throw new SQLException("Update failed");
    }

    public static NeuralNetwork readLastANN() throws SQLException {
        Connection conn=DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement(READ_LAST);
 
        ResultSet rs = stm.executeQuery();
        if( !rs.next() )
            throw new SQLException("ANN not found");
        NeuralNetwork network = (NeuralNetwork) rs.getObject(2);
        
        DBUtil.close(stm, rs);
        DBUtil.close(conn);
        return network;
    }
}
