/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.gui_controllers;

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
public class AdminGuiController extends GuiController {

    public AdminGuiController(MainFrame frame) {
        this.frame = frame;
    }

    @Override
    public void setStartingView() {
        mainPanel = new MainPanel();
        frame.remove(frame.getPanel());
        prepareLeftPanel();
        mainPanel.setContentPanel(new JPanel());
        mainPanel.setBounds(0, 0, MainFrame.WINDOW_WIDTH, MainFrame.WINDOW_HEIGHT - StatusPanel.PANEL_HEIGHT);

        frame.add(mainPanel);
        frame.repaint();
    }

    @Override
    public void prepareLeftPanel() {
        lp = new LeftPanel(this);
        lp.getSearchPatientButton().setVisible(false);
        lp.learnButton.setEnabled(true);
        lp.validateButton.setVisible(false);
        mainPanel.setLeftPanel(lp);
    }


}
