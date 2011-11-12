/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.gui_controllers;

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
