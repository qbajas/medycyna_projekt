/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.gui_controllers;

import heartdoctor.DataModel.PatientData;
import heartdoctor.DataModel.PatientSearchResults;
import heartdoctor.GUI.SearchPatients;

/**
 *
 * @author michal
 */
public class PatientResultsController {
    
    SearchPatients panel;
    PatientSearchResults model;
    int activeRecord;
    
    public PatientResultsController(SearchPatients searchPanel, PatientSearchResults model){
        panel=searchPanel;
        this.model=model;

    }
    
    public void initView(){
        for(PatientData patient:model.getPatients())
            panel.patientListBox.addItem(patient.getName() + " " + patient.getLastName());
        activeRecord=0;
        panel.prevButton.setEnabled(false);
        panel.nextButton.setEnabled(model.getPatients().size()>1);
    }
    
    public void updateGUI(){
         panel.prevButton.setEnabled(activeRecord>0);
         panel.nextButton.setEnabled(activeRecord<model.getPatients().size()-1);
         panel.patientListBox.setSelectedIndex(activeRecord);
         panel.showPatientData(model.getPatient(activeRecord));
    }
    
    public void nextResult(){
        if( activeRecord>=model.getPatients().size()-1 )
            return;
        ++activeRecord;
        updateGUI();
    }
    
    public void previousResult(){
       if( activeRecord<1)
            return;
        --activeRecord;
        updateGUI();
    }
    
    public void selectResult(int index){
        if(index>=0 || index<model.getPatients().size()){
            activeRecord=index;
            updateGUI();
        }
            
    }
    
    public PatientData getActivePatient(){
        return model.getPatient(activeRecord);
    }
    
    public void setPatientData(PatientData data){
        
    }
    
}
