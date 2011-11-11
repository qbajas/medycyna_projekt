/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.gui_controllers;

import heartdoctor.GUI.DiagnosisPanel;
import heartdoctor.GUI.LeftPanel;
import heartdoctor.GUI.MainFrame;
import heartdoctor.GUI.MainPanel;
import heartdoctor.GUI.NetworkStats;
import heartdoctor.GUI.StatusPanel;
import javax.swing.JPanel;

/**
 *
 * @author Qba
 */
public class AdminGuiController implements GuiController {

    private MainFrame frame;
    private MainPanel mainPanel;

    public AdminGuiController(MainFrame frame) {
        this.frame = frame;
    }

    public void setStartingView() {
        mainPanel = new MainPanel();
        frame.remove(frame.getPanel());
        prepareLeftPanel();
        mainPanel.setContentPanel(new NetworkStats(this));
        mainPanel.setBounds(0, 0, MainFrame.WINDOW_WIDTH, MainFrame.WINDOW_HEIGHT - StatusPanel.PANEL_HEIGHT);

        frame.add(mainPanel);
        frame.repaint();
    }

    public void prepareLeftPanel() {
        LeftPanel lp = new LeftPanel(this);
        lp.getDiagnosisPanelButton().setEnabled(false);
        lp.getSearchPatientButton().setEnabled(false);
        mainPanel.setLeftPanel(lp);
    }

    public void setRightPanel(JPanel panel) {
        mainPanel.setContentPanel(panel);
    }
}
