/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.Util;

import heartdoctor.ann.NeuralNetwork;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
        int i=stm.executeUpdate();

        DBUtil.close(stm);
        DBUtil.close(conn);
        if(i==0)
            throw new SQLException("Update failed");
    }

    public static NeuralNetwork readLastANN() throws SQLException, IOException, ClassNotFoundException {
        Connection conn=DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement(READ_LAST);
 
        ResultSet rs = stm.executeQuery();
        if( !rs.next() )
            throw new SQLException("ANN not found");
        
        byte[] buf = rs.getBytes(2);
        ObjectInputStream objectIn = null;
        if (buf != null)
            objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
        
        NeuralNetwork network = (NeuralNetwork) objectIn.readObject();
        
        DBUtil.close(stm, rs);
        DBUtil.close(conn);
        return network;
    }
}
