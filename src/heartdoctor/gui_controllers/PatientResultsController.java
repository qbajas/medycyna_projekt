/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.gui_controllers;

import heartdoctor.DataModel.PatientData;
import heartdoctor.DataModel.PatientSearchResults;
import heartdoctor.GUI.SearchPatients;

/**
 * Kontroler do zarzadzania wyswietlaniem listy pacjentow w widoku SearchPatients
 * @author michal
 */
public class PatientResultsController {
    
    SearchPatients panel;
    PatientSearchResults model;
    int activeRecord;
    
    /**
     * Tworzy nowy kontroler
     * @param searchPanel Panel do kontrolowania
     * @param model Model do wyswietlania
     */
    public PatientResultsController(SearchPatients searchPanel, PatientSearchResults model){
        panel=searchPanel;
        this.model=model;

    }
    
    /**
     * Inicjalizacja widoku, najpierw nalezy stworzyc kontroler, zainicjalizowac
     * komponenty widoku a pozniej uzyc funkcji initView
     */
    public void initView(){
        for(PatientData patient:model.getPatients())
            panel.patientListBox.addItem(patient.getName() + " " + patient.getLastName());
        activeRecord=0;
        panel.prevButton.setEnabled(false);
        panel.nextButton.setEnabled(model.getPatients().size()>1);
    }
    
    /**
     * Odswieza GUI po zmianie danych do wyswietlania
     */
    public void updateGUI(){
         panel.prevButton.setEnabled(activeRecord>0);
         panel.nextButton.setEnabled(activeRecord<model.getPatients().size()-1);
         panel.patientListBox.setSelectedIndex(activeRecord);
         panel.showPatientData(model.getPatient(activeRecord));
    }
    
    /**
     * Wyswietla nastepnego pacjenta na liscie
     */
    public void nextResult(){
        if( activeRecord>=model.getPatients().size()-1 )
            return;
        ++activeRecord;
        updateGUI();
    }
    
    /**
     * Wyswietla poprzedniego klienta na liscie
     */
    public void previousResult(){
       if( activeRecord<1)
            return;
        --activeRecord;
        updateGUI();
    }
    
    /**
     * Wyswietla pacjenta o podanym indeksie
     * @param index 
     */
    public void selectResult(int index){
        if(index>=0 || index<model.getPatients().size()){
            activeRecord=index;
            updateGUI();
        }
            
    }
    
    /**
     * Zwraca aktualnie wyswietlanego pacjenta
     * @return 
     */
    public PatientData getActivePatient(){
        return model.getPatient(activeRecord);
    }
    
    /**
     * Widac ze nic nie robi, cos mialo robic ale nie pamietam co dokladnie
     * chyba mialo modyfikowac aktualnie wybranego pacjenta, na razie nie potrzebne
     * @param data 
     */
    public void setPatientData(PatientData data){
        
    }
    
}
