/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui_controllers;

import heartdoctor.GUI.MainPanel;
import javax.swing.JPanel;

/**
 * klasa dostarczajaca strategie dla widokow gui
 * @author Qba
 */
public class DoctorGuiController {

    MainPanel mainPanel;

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public DoctorGuiController(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void setStartingScreen() {
//        mainPanel.setLeftPanel(new LeftPanel(this));
//        mainPanel.setContentPanel(new DiagnosisPanel(this));
    }

    public void setContentPanel(JPanel panel) {
        mainPanel.setContentPanel(panel);
    }
}
