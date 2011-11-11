/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.DataModel;

import java.util.ArrayList;

/**
 *
 * @author michal
 */
public class PatientSearchResults {
    private ArrayList<PatientData> patients;
    
    public PatientSearchResults(){
        patients=new ArrayList<PatientData>();
    }
    
    public PatientSearchResults(int initialCapacity){
        patients=new ArrayList<PatientData>(initialCapacity);
    }

    public ArrayList<PatientData> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<PatientData> patients) {
        this.patients = patients;
    }
    
    public PatientData getPatient(int indeks){
        return patients.get(indeks);
    }
    
    public void add(PatientData patient){
        patients.add(patient);
    }
}
