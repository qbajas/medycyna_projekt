/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.DataModel;

import java.util.ArrayList;
import java.util.Random;

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
        populateRecords();
    }

    /**
     * Tworzy nowy model PatientSearchResults z poczatkowa rezerwacja
     * i dodaje rekordy
     * @param initialCapacity Na ile elementow zarezerwowac pamiec
     */
    public PatientSearchResults(int initialCapacity) {
        patients = new ArrayList<PatientData>(initialCapacity);
        populateRecords();
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
     * @return
     */
    public static String generateSQL(String searchBy, String condition, String value) {
        return "SELECT * FROM Patients WHERE " + searchBy + " " + condition + " " + value;
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
