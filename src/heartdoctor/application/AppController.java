/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.application;

import heartdoctor.DataModel.User;
import heartdoctor.GUI.MainFrame;
import heartdoctor.Util.SecurityController;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author michal
 */
public class AppController {

    MainFrame frame;

    AppController(MainFrame frame) {
        this.frame = frame;
    }

    public void authenticationAction(final User user) {
        frame.setStatus("LOGOWANIE...");
        if (user.getLogin() == null || user.getPassword() == null
                || user.getLogin().isEmpty() || user.getPassword().isEmpty()) {

            JOptionPane.showConfirmDialog(frame,
                    "Fill in login and password", "Correct errors",
                    JOptionPane.DEFAULT_OPTION);
            frame.setStatus(null);
            return;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    frame.add(new JLabel("LOGOWANIE"));
                    if (SecurityController.authenticate(user)) {
                        showOptionPaneOutsideEDT("Access granted", 
                                "Successfully authorized");
                    } else {
                        showOptionPaneOutsideEDT("Access denied", 
                                "WYPIERDALAĆ! WSTĘP WZBRONONY");
                    }
                } catch (SQLException ex) {
                    showOptionPaneOutsideEDT("Connection problem", 
                                "Can't connect to DB");
                }
                
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setStatus(null);
                    }
                });
            }
        }).start();
    }
    
    public void showOptionPaneOutsideEDT(final String title, final String message){
        showOptionPaneOutsideEDT(title,message,JOptionPane.DEFAULT_OPTION);
    }

    public void showOptionPaneOutsideEDT(final String title, final String message,final int option) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JOptionPane.showConfirmDialog(frame,message, title,option);

            }
        });
    }
}
