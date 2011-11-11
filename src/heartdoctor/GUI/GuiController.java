/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.GUI;

import javax.swing.JPanel;

/**
 * klasa dostarczajaca strategie dla widokow gui
 * @author Qba
 */
public class GuiController {

    MainPanel mainPanel;

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public GuiController(MainPanel mainPanel) {
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
