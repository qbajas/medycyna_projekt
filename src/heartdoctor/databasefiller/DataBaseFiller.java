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
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Witek
 */
public class DataBaseFiller
{
    public void fillDB()
    {
        Connection conn=DBUtil.getConnection();
        Statement stm=null;

        JFileChooser chooser = new JFileChooser("./res");
        int retVal = chooser.showOpenDialog(null);
        
        if(retVal==JFileChooser.APPROVE_OPTION)
        {
            try
            {
                FileReader file = new FileReader(chooser.getSelectedFile().getPath());
                BufferedReader buffer = new BufferedReader(file);
                String line;
                while ((line = buffer.readLine()) != null)
                {
                    line=line.replace("?", "NULL");
                    line=line.replace("-9", "NULL");
                    String[] params= line.split("[, ]");
                    if(params.length==14)
                    {
                        try
                        {
                            stm = conn.createStatement();
                            stm.execute("INSERT INTO LearnDataSet "
                                + "(age,sex,cp,trestbps,chol,fbs,restecg,thalach,exang,oldpeak,slope,ca,thal,num)"
                                + "VALUES"
                                + "("
                                +params[0]+","+params[1]+","+params[2]+","+params[3]+","+params[4]+","+params[5]+","+params[6]+","
                                +params[7]+","+params[8]+","+params[9]+","+params[10]+","+params[11]+","+params[12]+","+params[13]
                                + ")");
                        }
                         catch(SQLException ex)
                        {
                            System.err.println(ex);
                        }
                    }
                    else
                    {
                        System.out.println("Plik jest zrąbany");
                        break; 
                    }     
                }
                file.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(DataBaseFiller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
