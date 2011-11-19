/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.gui_controllers;

import heartdoctor.GUI.DiagnosisPanel;
import heartdoctor.GUI.LeftPanel;
import heartdoctor.GUI.MainFrame;
import heartdoctor.GUI.MainPanel;
import heartdoctor.GUI.StatusPanel;
import javax.swing.JPanel;

/**
 * klasa dostarczajaca strategie dla widokow gui
 * @author Qba
 */
public class DoctorGuiController implements GuiController {

    MainPanel mainPanel;
    private final MainFrame frame;

    public DoctorGuiController(MainFrame frame) {
        this.frame = frame;
    }

    public void setStartingView() {
        mainPanel = new MainPanel();
        frame.remove(frame.getPanel());
        prepareLeftPanel();
        
        mainPanel.setContentPanel(new DiagnosisPanel(this));
        
        mainPanel.setBounds(0, 0, MainFrame.WINDOW_WIDTH, MainFrame.WINDOW_HEIGHT - StatusPanel.PANEL_HEIGHT);

        frame.add(mainPanel);
        frame.repaint();
    }

    public void prepareLeftPanel() {
        LeftPanel lp = new LeftPanel(this);
        lp.getSearchPatientButton().setEnabled(true);
        lp.learnButton.setEnabled(false);
        lp.statisticsButton.setEnabled(false);
        lp.validateButton.setEnabled(true);
        mainPanel.setLeftPanel(lp);

    }

    public void setRightPanel(JPanel panel) {
        mainPanel.setContentPanel(panel);
    }
}
