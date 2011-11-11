/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.gui_controllers;

import heartdoctor.GUI.DiagnosisPanel;
import heartdoctor.GUI.SearchPatients;
import javax.swing.JPanel;

/**
 *
 * @author Qba
 */
public interface GuiController {

    public void setStartingView();
    public void prepareLeftPanel();

    public void setRightPanel(JPanel panel);



}
