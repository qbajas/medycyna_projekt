/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.Util;

import heartdoctor.DataModel.PatientData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Witek
 */
public class PatientController {
    private static final String queryString = "select * from Patients where pesel=?";



    public static boolean canCreate(String pesel) throws SQLException {
        if(pesel.length()==11 && pesel.matches("[0-9]+"))
        {
            Connection conn = DBUtil.getConnection();
            PreparedStatement stm = null;
            ResultSet rs = null;
            try {
                stm = conn.prepareStatement(queryString);
                stm.setString(1, pesel);
                rs = stm.executeQuery();
                return !rs.next();
            } finally {
                DBUtil.close(stm, rs);
                DBUtil.close(conn);
            }
        }
        else {
            System.out.println("Blad w numerze pesel");
            return false;
        }
    }

    public static boolean addUser(PatientData patient) throws SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = null;
        try {

            if (!canCreate(patient.getPesel())) {
                return false;
            }
            stm = conn.prepareStatement("insert into Patients (name,middlename,lastname,pesel,address,postalcode,city) values(?,?,?,?,?,?,?)");
            stm.setString(1, patient.getName());
            stm.setString(2, patient.getMiddleName());
            stm.setString(3, patient.getLastName());
            stm.setString(4, patient.getPesel());
            stm.setString(5, patient.getAddress());
            stm.setString(6, patient.getPostalCode());
            stm.setString(7, patient.getCity());
            stm.executeUpdate();
            return true;
        } finally {
            DBUtil.close(stm);
            DBUtil.close(conn);
        }
    }

    public static void main(String[] args) {
        System.out.println("Dodawanie pacjenta:");
        String pesel;
        Scanner scan = new Scanner(System.in);
        try {

            do {
                System.out.println("Podaj pesel:");
                pesel = scan.nextLine();
            } while (!canCreate(pesel));
            PatientData patient = new PatientData();
            patient.setPesel(pesel);
            System.out.println("Podaj imie:");
            patient.setName(scan.nextLine());
            System.out.println("Podaj drugie imie:");
            patient.setMiddleName(scan.nextLine());
            System.out.println("Podaj nazwisko:");
            patient.setLastName(scan.nextLine());
            System.out.println("Podaj adres:");
            patient.setAddress(scan.nextLine());
            System.out.println("Podaj kod pocztowy:");
            patient.setPostalCode(scan.nextLine());
            System.out.println("Podaj miejscowosc:");
            patient.setCity(scan.nextLine());
            addUser(patient);
        } catch (SQLException ex) {
            Logger.getLogger(SecurityController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Nie mozna dodac pacjenta");
        }
        catch (Exception e) {
            Logger.getLogger(SecurityController.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e.getMessage());
        }

         finally {
            scan.close();
        }
    }
}
