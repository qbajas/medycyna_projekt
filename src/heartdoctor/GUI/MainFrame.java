/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author michal
 */
public class MainFrame extends JFrame {

    static final int WINDOW_WIDTH = 800;
    static final int WINDOW_HEIGHT = 600;
    StatusPanel statusPanel;
    private LoginPanel panel;

    public MainFrame() {
        super();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        statusPanel = new StatusPanel();
        statusPanel.setBounds(0, WINDOW_HEIGHT - statusPanel.PANEL_HEIGHT, statusPanel.PANEL_WIDTH, statusPanel.PANEL_HEIGHT);
        statusPanel.setBackground(Color.CYAN);
        add(statusPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int h = screenSize.height;
        int w = screenSize.width;
        setLocation((w - WINDOW_WIDTH) / 2, (h - WINDOW_HEIGHT) / 2);
        setResizable(false);

    }

    public void setMainPanel(MainPanel mp) {

        remove(panel);
        mp.setLeftPanel(new LeftPanel());
        mp.setContentPanel(new DiagnosisPanel());
        mp.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT - statusPanel.PANEL_HEIGHT);

        add(mp);
        repaint();
    }

    public void setStatus(String status) {
        statusPanel.setStatus(status);
    }

    public static int getWINDOW_HEIGHT() {
        return WINDOW_HEIGHT;
    }

    public static int getWINDOW_WIDTH() {
        return WINDOW_WIDTH;
    }

    public void setLoginPanel(LoginPanel panel) {
        this.panel = panel;
        add(panel);
    }
}
