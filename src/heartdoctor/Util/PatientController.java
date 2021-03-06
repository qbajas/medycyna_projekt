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
    static Connection patientConn = null;
    static PreparedStatement patientStm = null;

    /**
     * Sprawdza czy można utworzyć pacjenta o zadanym nr PESEL, walidacja PESELu
     * @param pesel PESEL do walidacji
     * @return
     * @throws SQLException 
     */
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

    /**
     * Aktualizuje dane osobowe pacjenta
     * @param patient Dane do aktualizacji
     * @return true jesli zaktualizowano
     * @throws SQLException 
     */
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
     * Aktualizuje dane medyczne
     * @param record Dane do aktualizacji
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
            stm = conn.prepareStatement("update  LearnDataSet set "
                    + "age=?, sex=?, cp=?, trestbps=?, chol=?, fbs=?, restecg=?, thalach=?, "
                    + "exang=?, oldpeak=?, slope=?, ca=?, thal=?,num=?,diagnosis=? "
                    + "WHERE id=?");
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

    /**
     * Usuwa z bazy danych danego pacjenta, nie usuwa danych medycznych
     * @param patient
     * @return
     * @throws SQLException 
     */
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

    /**
     * Usuwa rekord z danymi medycznymi
     * @param data Dane medyczne w bazie do usunięcia
     * @return Liczba usuniętych rekordów
     * @throws SQLException 
     */
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

    /**
     * Dodaje do bazy danych pacjenta
     * @param patient Dane osobowe pacjenta
     * @param single true jesli dodajemy jednego pacjenta, false jeśli funkcja
     * będzie wykonywana w pętli- zapewnia szybsze wykonanie kilku insertów
     * Jeśli false należy dodatkowo po pętli wywołać funkcję closeConnection()
     * @return true jesli dodano pacjenta
     * @throws SQLException 
     */
    public static boolean addPatient(PatientData patient, boolean single) throws SQLException {

        if (patientConn == null) {
            patientConn = DBUtil.getConnection();
        }
        ResultSet rs = null;
        try {
            if (!canCreate(patient.getPesel())) 
                return false;
            if (patientStm == null) {
                patientStm = patientConn.prepareStatement("insert into Patients "
                        + "(name,middlename,lastname,pesel,address,postalcode,city) "
                        + "values(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            }
            patientStm.setString(1, patient.getName());
            patientStm.setString(2, patient.getMiddleName());
            patientStm.setString(3, patient.getLastName());
            patientStm.setString(4, patient.getPesel());
            patientStm.setString(5, patient.getAddress());
            patientStm.setString(6, patient.getPostalCode());
            patientStm.setString(7, patient.getCity());

            patientStm.executeUpdate();
            rs = patientStm.getGeneratedKeys();
            if (rs.next()) {
                patient.setID(rs.getInt(1));
            } else {
                throw new RuntimeException("Wrong id returned");
            }

            return true;
        } finally {
            DBUtil.close(rs);
            if (single) {
                DBUtil.close(patientStm);
                DBUtil.close(patientConn);
                patientStm = null;
                patientConn = null;
            }

        }
    }

    /**
     * Zamyka połączenie i PreparedStatement przy dodawaniu wielu pacjentów
     */
    public static void closeConnection() {
        DBUtil.close(patientStm);
        DBUtil.close(patientConn);
    }

    /**
     * Prosty formularz do ręcznego wprowadzania nowego pacjenta do bazy danych
     * @param args 
     */
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
            addPatient(patient, true);
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
