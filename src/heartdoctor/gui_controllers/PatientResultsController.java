/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.gui_controllers;

import heartdoctor.DataModel.PatientSearchResults;
import heartdoctor.GUI.SearchPatients;

/**
 *
 * @author michal
 */
public class PatientResultsController {
    
    SearchPatients panel;
    PatientSearchResults model;
    
    public PatientResultsController(SearchPatients searchPanel, PatientSearchResults model){
        panel=searchPanel;
        this.model=model;
    }
    
    public void nextResult(){
        
    }
    
    public void previousResult(){
        
    }
    
    public void setResult(int indeks){
        
    }
}
