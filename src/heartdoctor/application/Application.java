/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.application;

import heartdoctor.GUI.DiagnosisPanel;
import heartdoctor.GUI.LeftPanel;
import heartdoctor.GUI.MainPanel;
import javax.swing.JFrame;

/**
 *
 * @author michal
 */
public class Application {
    JFrame frame;
    MainPanel mainPanel;
    
    public Application(){
        frame= new JFrame();
        frame.setSize(800, 450);
       // frame.setResizable(false);
        mainPanel=new MainPanel();
        mainPanel.setLeftPanel(new LeftPanel());
        mainPanel.setContentPanel(new DiagnosisPanel());
        frame.add(mainPanel);
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }

    
}
