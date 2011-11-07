/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.databasefiller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import heartdoctor.Util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Witek
 */
public class DataBaseFiller {
    private static final String queryString="INSERT INTO LearnDataSet "
                        + "(age,sex,cp,trestbps,chol,fbs,restecg,thalach,exang,oldpeak,slope,ca,thal,num)"
                        + "VALUES"
                        + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
   public static void main(String[] args) {
       	DataBaseFiller dbf=new DataBaseFiller();
        dbf.fillDB();
    }
    
    public void fillDB() {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = null;

        JFileChooser chooser = new JFileChooser("./res");
        int retVal = chooser.showOpenDialog(null);

        FileReader file=null;
        if (retVal == JFileChooser.APPROVE_OPTION) {
            try {
                file = new FileReader(chooser.getSelectedFile().getPath());
                BufferedReader buffer = new BufferedReader(file);
                String line;
                
                conn.setAutoCommit(false);
                
                stm = conn.prepareStatement(queryString);
                while ((line = buffer.readLine()) != null) {
                    line = line.replace("?", "NULL");
                    line = line.replace("-9", "NULL");
                    String[] params = line.split("[, ]");
                    if (params.length == 14) {
                        try {
                            for (int i = 0; i < 14; i++) {
                                if(params[i].equals("NULL"))
                                    stm.setNull(i+1, java.sql.Types.DOUBLE);
                                else
                                    stm.setDouble(i + 1, Double.parseDouble(params[i]));
                            }
                            stm.executeUpdate();
                        } catch (SQLException ex) {
                            System.err.println(ex);
                            throw ex;
                        }
                    } else {
                        System.out.println("Plik jest zrąbany");
                        throw new IOException("Plik jest zrąbany");
                    }
                }
                conn.commit();

            } catch (SQLException ex) {
                DBUtil.rollBack(conn);
                Logger.getLogger(DataBaseFiller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                DBUtil.rollBack(conn);
                Logger.getLogger(DataBaseFiller.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DBUtil.close(stm);
                DBUtil.close(conn);
                try{
                    file.close();
                } catch (IOException ex){
                    Logger.getLogger(DataBaseFiller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
