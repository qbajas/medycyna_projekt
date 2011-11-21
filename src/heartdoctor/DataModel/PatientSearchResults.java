/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.DataModel;

import java.sql.Connection;
import heartdoctor.Util.DBUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa modelujaca liste pacjentow zwrocana z zapytania etc.
 * W konstrukorze na czas testow uzyta funkcja populateRecords zeby 
 * utworzyc jakies rekordy - funkcja do usuniecia gdy bedzie juz gotowa funkcjonalnosc
 * do wyszukiwania
 * @author michal
 */
public class PatientSearchResults {

    private ArrayList<PatientData> patients;

    /**
     * Tworzy nowy model PatientSearchResults i dodaje rekordy 
     */
    public PatientSearchResults() {
        patients = new ArrayList<PatientData>();
//        loadAllPatients();
    }

    /**
     * Tworzy nowy model PatientSearchResults z poczatkowa rezerwacja
     * i dodaje rekordy
     * @param initialCapacity Na ile elementow zarezerwowac pamiec
     */
    public PatientSearchResults(int initialCapacity) {
        patients = new ArrayList<PatientData>(initialCapacity);
//        loadAllPatients();
    }

    /**
     * konstruktor tworzacy rezultaty na podstawie query
     * @param query
     */
//    public PatientSearchResults(String query) {
//        patients = search(query);
//    }
    /**
     * wykonuje zapytanie sql do bazy, rezulaty zapisuje w obiekcie
     * @param query
     * @return
     */
//    public ArrayList<PatientData> search(String query) {
    public void search(String query) {
//        patients = new ArrayList<PatientData>();
        Connection conn = DBUtil.getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = conn.prepareStatement(query);
            rs = stm.executeQuery();
            patients = convertResultSet(rs);
        } catch (SQLException ex) {
            Logger.getLogger(PatientSearchResults.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtil.close(stm, rs);
            DBUtil.close(conn);
        }
    }

    /**
     * na podstawie resultatu zapytania generuje obiekty 
     * @param rs
     * @return
     */
    public ArrayList<PatientData> convertResultSet(ResultSet rs) {
        ArrayList<PatientData> _patients = new ArrayList<PatientData>();
        PatientData patientData;
        MedicalData medicalData;
        try {
            while (rs.next()) {

                medicalData = new MedicalData();
                medicalData.setDbID(rs.getInt(9));
                medicalData.setAge(rs.getInt(10));
                medicalData.setSex(rs.getDouble(11));
                medicalData.setChestPain(rs.getDouble(12));
                medicalData.setBloodPressure(rs.getDouble(13));
                medicalData.setCholestoral(rs.getDouble(14));
                medicalData.setBloodSugar(rs.getDouble(15));
                medicalData.setRestecg(rs.getDouble(16));
                medicalData.setMaxHeartRate(rs.getDouble(17));
                medicalData.setAngina(rs.getDouble(18));
                medicalData.setOldpeak(rs.getDouble(19));
                medicalData.setSlope(rs.getDouble(20));
                medicalData.setCa(rs.getDouble(21));
                medicalData.setThal(rs.getDouble(22));
                medicalData.setProgramDiagnosis(rs.getDouble(25));
                medicalData.setDiagnosis(rs.getDouble(23));

//                rs.getInt(9), rs.getDouble(10), new MedicalData.Sex())
                patientData = new PatientData(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),
                        rs.getString(7), rs.getString(8), medicalData);

                _patients.add(patientData);

            }
        } catch (SQLException ex) {
            Logger.getLogger(PatientSearchResults.class.getName()).log(Level.SEVERE, null, ex);
        }
        return _patients;
    }

    /**
     * Zwraca liste pacjentow
     * @return 
     */
    public ArrayList<PatientData> getPatients() {
        return patients;
    }

    /**
     * Wrzuca do modelu dana liste pacjentow
     * @param patients 
     */
    public void setPatients(ArrayList<PatientData> patients) {
        this.patients = patients;
    }

    /**
     * Zwraca pacjenta o podanym indeksie
     * @param indeks
     * @return 
     */
    public PatientData getPatient(int indeks) {
        return patients.get(indeks);
    }

    /**
     * Dodaje pacjenta do listy
     * @param patient 
     */
    public void add(PatientData patient) {
        patients.add(patient);
    }

    /**
     * generuje zapytanie sql z danych z kontrolera
     */
    public static String generateSQL(String searchBy, String condition, String value) {
        return  getBasicQuery() + " WHERE " + searchBy.toLowerCase() + " " + condition + " '" + value + "'";
    }


    /**
     * wczytywanie wszystkich pacjentow
     */
    public void loadAllPatients()
    {
        search(getBasicQuery());
    }


    /**
     * szkielet zapytania
     * @return
     */
    private static String getBasicQuery(){
        return "SELECT * FROM Patients P JOIN LearnDataSet L on P.id=L.patient_id";
    }

    /**
     * Generuje 10 rekordow, funkcja na czas testow, do wywalenia pozniej
     */
    public void populateRecords() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            PatientData patient = new PatientData(i, "Imie" + i,
                    "MiddleName" + i, "Nazwisko" + i, "" + i * 12314,
                    "address" + i, "code" + i, "city" + i, null);

            MedicalData medical = new MedicalData();

            medical.setAge(i * 11);
            medical.setSex(i % 2);
            medical.setAngina(rand.nextInt(2));

            medical.setBloodPressure(80 + i * 12);
            medical.setBloodSugar(i);
            medical.setMaxHeartRate(40 + i * 10);
            medical.setOldpeak(i);
            medical.setCholestoral(i * 20);

            medical.setChestPain(1 + i % 4);
            medical.setCa(i % 4);
            medical.setRestecg(i % 3);
            medical.setSlope(1 + i % 3);

            patient.setMedicalData(medical);

            int thal;
            if (i < 3) {
                thal = 3;
            } else if (i < 6) {
                thal = 6;
            } else {
                thal = 7;
            }
            medical.setThal(thal);

            patients.add(patient);

        }
    }
}
