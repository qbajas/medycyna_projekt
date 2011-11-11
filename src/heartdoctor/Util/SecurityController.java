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
 * Klasa do autoryzacji uzytkownika
 * @author michal
 */
public class SecurityController {

    private static final String queryString = "select * from Users where login=? and password=?";

    /**
     * Sprawdza login i haslo uzytkownika, w przypadku poprawnej autoryzacji
     * wczytuje reszte danych uzytkownika
     * @param user Uzytkownik ktorego nalezy zautoryzowac
     * @return true jesli wprowadzono poprawny login i haslo, false w przeciwnym wypadku
     * @throws SQLException
     */
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

    /**
     * Koduje String wejsciowy pass uzywajac sumy SHA1. Wynik zwracany jako
     * String, ktory reprezentuje zapis sumy w hexie.
     * @param pass String do zakodowania
     * @return Hex'owa reprezentacja zakodowanego argumentu
     */
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

    /**
     * Konwertuje tablice byte[] do hex'owej reprezentacji. 
     * @param b Tablica do konwersji
     * @return String z hex'owa reprezentacja tablicy
     * @throws Exception 
     */
    private static String byteArrayToHexString(byte[] b) throws Exception {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    /**
     * Sprawdza czy mozna utworzyc w bazie uzytkownika o podanym loginie, tj. czy
     * login nie jest juz zajety
     * @param login Login do sprawdzenia
     * @return true jesli login nie istnieje, false w przeciwnym przypadku
     * @throws SQLException 
     */
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

    /**
     * Dodaje uzytkownika do bazy. W zasadzie nie bedzie uzywane w programie,
     * ale moze byc pomocne przy tworzeniu bazy uzytkownikow. W teorii jakis
     * inny system powinien byc odpowiedzialny za tworzenie kont uzytkownikow, 
     * my tylko korzystamy z tej bazy
     * @param user User dodawany do bazy
     * @return true jesli uzytkownik zostal dodany, false w przeciwnym razie
     * @throws SQLException 
     */
    public static boolean addUser(User user) throws SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = null;
        try {

            if (!canCreate(user.getLogin())) {
                return false;
            }
            stm = conn.prepareStatement("insert into Users values(?,?,?,?,?)");
            stm.setString(1, user.getName());
            stm.setString(2, user.getLastName());
            stm.setString(3, user.getLogin());
            stm.setString(4, SHA1(user.getPassword()));
            stm.setString(5, user.getRole());
            stm.executeUpdate();
            return true;
        } finally {
            DBUtil.close(stm);
            DBUtil.close(conn);
        }
    }

    /**
     * Kreator dodawania nowego uzytkownika. Funkcja pomocnicza przy 
     * tworzeniu bazy uzytkownikow.
     * @param args 
     */
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
