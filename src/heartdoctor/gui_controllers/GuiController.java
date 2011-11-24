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
 * Klasa abstrakcyjna
 * klasy po niej dziedziczące są kontrolerami dla widoków (MVC)
 * decyzja o instanizacji konkretnej klasy podejmowana jest w AppController (createGuiController)
 * w tych klasach implementowana jest logika do widoków
 *
 * @author Qba
 */
public abstract class GuiController {

    protected MainFrame frame;
    protected MainPanel mainPanel;
    protected LeftPanel lp;

    /**
     * metoda ustawiająca ekran startowy
     * (ekran po zalogowaniu sie)
     */
    public abstract void setStartingView();

    /**
     * przygotowywanie lewego panelu (glownego menu)
     * menu jest rozne w zaleznosci od tego kto sie zalogowal
     */
    public abstract void prepareLeftPanel();

    /**
     * ustawianie prawego panelu (glowny panel, z najwazaniejsza trescia)
     * @param panel
     */
    public void setRightPanel(JPanel panel) {
        mainPanel.setContentPanel(panel);
    }

    
//    settery i gettery

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
