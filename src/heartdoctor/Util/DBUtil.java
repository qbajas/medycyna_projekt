/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Klasa do pozyskiwania polaczenia z baza danych + ulatwienie zamykania
 * polaczen, Statement, ResultSet
 * @author michal
 */
public class DBUtil {

    private static String DB_CONN_STRING = "jdbc:mysql://medycyna.x10.mx:3306/michal12_medycyna";
    private static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static String USER_NAME = "michal12_app";
    private static String PASSWORD = "1q2w3e";

    public static Connection getConnection() {

        Connection result = null;
        try {
            Class.forName(DRIVER_CLASS_NAME).newInstance();
        } catch (Exception ex) {
            log("Nie moge wczytac drivera bazy danych: " + DRIVER_CLASS_NAME);
        }

        try {
            result = DriverManager.getConnection(DB_CONN_STRING, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            log("Nie mozna sie polaczyc z baza: " + DB_CONN_STRING);
        }
        return result;
    }

    public static void close(Connection conn) {
        if(conn==null)
            return;
        try {
            conn.close();
        } catch (SQLException ex) {
            log(ex);
        }
    }

    public static void close(Statement stm) {
        if(stm==null)
            return;
        try {
            stm.close();
        } catch (SQLException ex) {
            log(ex);
        }
    }

    public static void close(ResultSet rs) {
        if(rs==null)
            return;
        try {
            rs.close();
        } catch (SQLException ex) {
            log(ex);
        }
    }

    public static void close(Statement stm, ResultSet rs) {
        close(rs);
        close(stm);
    }

    private static void log(Object aObject) {
        System.out.println(aObject);
    }
}
