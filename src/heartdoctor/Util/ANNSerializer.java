package heartdoctor.Util;

import heartdoctor.ann.NeuralNetwork;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klasa do serializacji i deserializacji sieci neuronowej
 * @author michal
 */
public class ANNSerializer {

    static final String WRITE_OBJECT_SQL = "INSERT INTO SerializedObjects(object_value) VALUES (?)";
    static final String READ_LAST = "SELECT * FROM SerializedObjects order by id limit 1";

    /**
     * Zapisuje do bazy danych daną sieć neuronową
     * @param object Sieć do zapisu
     * @throws SQLException 
     */
    public static void writeANN(NeuralNetwork object) throws SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement(WRITE_OBJECT_SQL);

        stm.setObject(1, object);
        int i = stm.executeUpdate();

        DBUtil.close(stm);
        DBUtil.close(conn);
        if (i == 0) {
            throw new SQLException("Update failed");
        }
    }

    /**
     * Wczytuje z bazy danych ostatnią zapisaną sieć neuronową
     * @return Sieć neuronowa wczytana z bazy
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static NeuralNetwork readLastANN() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = conn.prepareStatement(READ_LAST);

        ResultSet rs = stm.executeQuery();
        if (!rs.next()) {
            throw new SQLException("ANN not found");
        }

        byte[] buf = rs.getBytes(2);
        ObjectInputStream objectIn = null;
        if (buf != null) {
            objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
        }

        NeuralNetwork network = (NeuralNetwork) objectIn.readObject();

        DBUtil.close(stm, rs);
        DBUtil.close(conn);
        return network;
    }
}
