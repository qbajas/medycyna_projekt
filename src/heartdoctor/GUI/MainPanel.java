package heartdoctor.GUI;

import javax.swing.JPanel;

/**
 *
 * @author michal
 */
public class MainPanel extends JPanel {

    private JPanel leftPanel;
    private JPanel contentPanel;

    public static final int WINDOW_WIDTH = MainFrame.WINDOW_WIDTH;
    public static final int WINDOW_HEIGHT = MainFrame.WINDOW_HEIGHT;
    public static final int LEFT_PANEL_WIDTH=200;
    public static final int MAIN_PANEL_WIDTH=WINDOW_WIDTH-LEFT_PANEL_WIDTH;

    public MainPanel() {
        setLayout(null);
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public void setContentPanel(JPanel contentPanel) {
        if (this.contentPanel != null) {
            remove(this.contentPanel);
        }
        this.contentPanel = contentPanel;
        contentPanel.setBounds(LEFT_PANEL_WIDTH, 0, MAIN_PANEL_WIDTH, WINDOW_HEIGHT-StatusPanel.PANEL_HEIGHT);
        add(contentPanel);

        revalidate();
        repaint();
    }

    public JPanel getLeftPanel() {
        return leftPanel;
    }

    public void setLeftPanel(JPanel leftPanel) {
        if (this.leftPanel != null) {
            remove(this.leftPanel);
        }
        this.leftPanel = leftPanel;
        leftPanel.setBounds(0, 0, LEFT_PANEL_WIDTH, WINDOW_HEIGHT-StatusPanel.PANEL_HEIGHT);
        add(leftPanel);
        revalidate();
        repaint();
    }
}
