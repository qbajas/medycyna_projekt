/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.gui_controllers;

import heartdoctor.GUI.LeftPanel;
import heartdoctor.GUI.MainFrame;
import heartdoctor.GUI.MainPanel;
import javax.swing.JPanel;

/**
 *
 * @author Qba
 */
public abstract class GuiController {

    protected MainFrame frame;
    protected MainPanel mainPanel;
    protected LeftPanel lp;
    
    public abstract void setStartingView();
    public abstract void prepareLeftPanel();

    public void setRightPanel(JPanel panel) {
        mainPanel.setContentPanel(panel);
    }

    public MainFrame getFrame() {
        return frame;
    }

    public void setFrame(MainFrame frame) {
        this.frame = frame;
    }

    public LeftPanel getLp() {
        return lp;
    }

    public void setLp(LeftPanel lp) {
        this.lp = lp;
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    
}
