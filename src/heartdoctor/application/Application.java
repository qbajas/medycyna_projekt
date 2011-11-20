/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.application;

import heartdoctor.GUI.DiagnosisPanel;
import heartdoctor.GUI.LeftPanel;
import heartdoctor.GUI.LoginPanel;
import heartdoctor.GUI.MainFrame;
import heartdoctor.GUI.MainPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;

/**
 *
 * @author michal
 */
public class Application {
    MainFrame frame;
    AppController controller;
    public Application(){
        frame= new MainFrame();
        AppController.setFrame(frame);
        controller=AppController.get();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controller.showLoginScreen();
        
       javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                frame.setVisible(true);
                
            }
        });
      
    }
    
}
