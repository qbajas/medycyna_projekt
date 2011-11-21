/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.Util;

import heartdoctor.DataModel.MedicalData;
import heartdoctor.DataModel.PatientData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        if (pesel.length() == 11 && pesel.matches("[0-9]+")) {
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
        } else {
            System.out.println("Blad w numerze pesel " + pesel);
            return false;
        }
    }

    public static boolean updatePatient(PatientData patient) throws SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = null;
        try {

            if (!canCreate(patient.getPesel())) {
                return false;
            }
            stm = conn.prepareStatement("update Patients "
                    + "set name=?,middlename=?,lastname=?,pesel=?,address=?,postalcode=?,city=?");
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

    /**
     * poprawione
     * @param record
     * @return
     * @throws SQLException
     */
    public static boolean updateMedicalRecord(MedicalData record) throws SQLException {
        if (record == null) {
            return false;
        }
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement("update  LearnDataSet set"
                    + "age=?, sex=?, cp=?, trestbps=?, chol=?, fbs=?, restecg=?, thalach=?,"
                    + "exang=?, oldpeak=?, slope=?, ca=?, thal=?,num=?,diagnosis=?"
                    + " WHERE id=?");
            stm.setDouble(1, record.getAge());
            stm.setDouble(2, record.getSex());
            stm.setDouble(3, record.getChestPain());
            stm.setDouble(4, record.getBloodPressure());
            stm.setDouble(5, record.getCholestoral());
            stm.setDouble(6, record.getBloodSugar());
            stm.setDouble(7, record.getRestecg());
            stm.setDouble(8, record.getMaxHeartRate());
            stm.setDouble(9, record.getAngina());
            stm.setDouble(10, record.getOldpeak());
            stm.setDouble(11, record.getSlope());
            stm.setDouble(12, record.getCa());
            stm.setDouble(13, record.getThal());
            stm.setDouble(14, record.getDiagnosis());
            stm.setDouble(15, record.getProgramDiagnosis());
            stm.setInt(16, record.getDbID());
            stm.executeUpdate();
            return true;
        } finally {
            DBUtil.close(stm, rs);
            DBUtil.close(conn);
        }
    }

    public static int deletePatient(PatientData patient) throws SQLException {
        Connection conn = DBUtil.getConnection();
        Statement stm = null;
        try {
            stm = conn.createStatement();
            return stm.executeUpdate("delete from Patients where id=" + patient.getID());
        } finally {
            DBUtil.close(stm);
            DBUtil.close(conn);
        }
    }

    public static int deleteMedicalRecord(MedicalData data) throws SQLException {
        Connection conn = DBUtil.getConnection();
        Statement stm = null;
        try {
            stm = conn.createStatement();
            return stm.executeUpdate("delete from LearnDataSet where id=" + data.getDbID());
        } finally {
            DBUtil.close(stm);
            DBUtil.close(conn);
        }
    }

    /**
     * Dodaje do bazy nowy rekord medyczny, laczy go z podanym pacjentem.
     * Gdy pacjent =null rekord nie bedzie polaczony z zadnym pacjentem
     * @param record Dane medyczne do dodania do bazy
     * @param patient Pacjent do ktorego przypisac wyniki badan
     */
    public static void addMedicalRecord(MedicalData record, PatientData patient) throws SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement("insert into LearnDataSet"
                    + "(age, sex, cp, trestbps, chol, fbs, restecg, thalach,"
                    + "exang, oldpeak, slope, ca, thal,num,patient_id,diagnosis) "
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stm.setDouble(1, record.getAge());
            stm.setDouble(2, record.getSex());
            stm.setDouble(3, record.getChestPain());
            stm.setDouble(4, record.getBloodPressure());
            stm.setDouble(5, record.getCholestoral());
            stm.setDouble(6, record.getBloodSugar());
            stm.setDouble(7, record.getRestecg());
            stm.setDouble(8, record.getMaxHeartRate());
            stm.setDouble(9, record.getAngina());
            stm.setDouble(10, record.getOldpeak());
            stm.setDouble(11, record.getSlope());
            stm.setDouble(12, record.getCa());
            stm.setDouble(13, record.getThal());
            stm.setDouble(14, record.getDiagnosis());
            stm.setInt(15, patient.getID());
            stm.setDouble(16, record.getProgramDiagnosis());

            stm.executeUpdate();

            rs = stm.getGeneratedKeys();

            if (rs.next()) {
                record.setDbID(rs.getInt(1));
                patient.setMedicalData(record);
            } else {
                throw new RuntimeException("Wrong id returned");
            }
        } finally {
            DBUtil.close(stm, rs);
            DBUtil.close(conn);
        }

    }

    public static boolean addPatient(PatientData patient) throws SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {

            if (!canCreate(patient.getPesel())) {
                return false;
            }
            stm = conn.prepareStatement("insert into Patients "
                    + "(name,middlename,lastname,pesel,address,postalcode,city) "
                    + "values(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, patient.getName());
            stm.setString(2, patient.getMiddleName());
            stm.setString(3, patient.getLastName());
            stm.setString(4, patient.getPesel());
            stm.setString(5, patient.getAddress());
            stm.setString(6, patient.getPostalCode());
            stm.setString(7, patient.getCity());
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();

            if (rs.next()) {
                patient.setID(rs.getInt(1));
            } else {
                throw new RuntimeException("Wrong id returned");
            }

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
            addPatient(patient);
        } catch (SQLException ex) {
            Logger.getLogger(SecurityController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Nie mozna dodac pacjenta");
        } catch (Exception e) {
            Logger.getLogger(SecurityController.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(e.getMessage());
        } finally {
            scan.close();
        }
    }
}
