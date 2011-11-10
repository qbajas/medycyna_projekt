package heartdoctor.Util;

import heartdoctor.DataModel.User;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michal
 */
public class SecurityController {

    private static final String queryString = "select * from Users where login=? and password=?";

    public static boolean authenticate(User user) throws SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement(queryString);
            stm.setString(1, user.getLogin());
            stm.setString(2, SHA1(user.getPassword()));
            rs = stm.executeQuery();
            if (rs.next()) {
                user.setName(rs.getString(1));
                user.setLastName(rs.getString(2));
                user.setRole(rs.getString(5));
                return true;
            } else {
                return false;
            }
        } finally {
            DBUtil.close(stm, rs);
            DBUtil.close(conn);
        }

    }

    public static String SHA1(String pass) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] result = md.digest(pass.getBytes("UTF-8"));
            return byteArrayToHexString(result);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SecurityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SecurityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SecurityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String byteArrayToHexString(byte[] b) throws Exception {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    public static boolean canCreate(String login) throws SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement("select * from Users where login=?");
            stm.setString(1, login);
            rs = stm.executeQuery();
            return !rs.next();
        } finally {
            DBUtil.close(stm, rs);
            DBUtil.close(conn);
        }
    }

    public static void addUser(User user) throws SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = null;
        try {
            stm = conn.prepareStatement("insert into Users values(?,?,?,?,?)");
            stm.setString(1, user.getName());
            stm.setString(2, user.getLastName());
            stm.setString(3, user.getLogin());
            stm.setString(4, SHA1(user.getPassword()));
            stm.setString(5, user.getRole());
            stm.executeUpdate();

        } finally {
            DBUtil.close(stm);
            DBUtil.close(conn);
        }
    }

    public static void main(String[] args) {
        System.out.println("Tworzenie użyszkodnika");
        String login;
        Scanner scan = new Scanner(System.in);
        try {

            do {
                System.out.println("Podaj login:");
                login = scan.nextLine();
            } while (!canCreate(login));

            User user = new User();
            user.setLogin(login);
            System.out.println("Podaj haslo");
            user.setPassword(scan.nextLine());
            System.out.println("Podaj imie");
            user.setName(scan.nextLine());
            System.out.println("Podaj nazwisko");
            user.setLastName(scan.nextLine());
            System.out.println("Podaj role");
            user.setRole(scan.nextLine());
            addUser(user);
        } catch (SQLException ex) {
            Logger.getLogger(SecurityController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Nie mozna utworzyc uzytkownika");
        } finally {
            scan.close();
        }
    }
}
