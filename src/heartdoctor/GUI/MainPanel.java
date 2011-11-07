package heartdoctor.GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import sun.awt.VerticalBagLayout;

/**
 *
 * @author michal
 */
public class MainPanel extends JPanel{
    private JPanel leftPanel;
    private JPanel contentPanel;

    public MainPanel() {

    }
    
    

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public void setContentPanel(JPanel contentPanel) {
        if(this.contentPanel!=null)
            remove(this.contentPanel);
        this.contentPanel = contentPanel;
        contentPanel.setBounds(180,0, 500, 700);
        add(contentPanel);
        
                revalidate();
        repaint();
    }

    public JPanel getLeftPanel() {
        return leftPanel;
    }

    public void setLeftPanel(JPanel leftPanel) {
        if(this.leftPanel!=null)
            remove(this.leftPanel);
        this.leftPanel = leftPanel;
        leftPanel.setBounds(0, 0, 400, 700);
        add(leftPanel);
        revalidate();
        repaint();
    }
    
    
    
}
