/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.application;

import heartdoctor.DataModel.User;
import heartdoctor.GUI.LoginPanel;
import heartdoctor.GUI.MainFrame;
import heartdoctor.GUI.MainPanel;
import heartdoctor.Util.SecurityController;
import heartdoctor.gui_controllers.AdminGuiController;
import heartdoctor.gui_controllers.DoctorGuiController;
import heartdoctor.gui_controllers.GuiController;
import java.awt.Color;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author michal
 */
public class AppController {

    static MainFrame frame;
    static GuiController controller;
    private static AppController instance;

    private AppController(){
        
    }
    
    public static AppController get(){
        if(instance==null)
            instance=new AppController();
        return instance;
    }

    public static GuiController getController() {
        return controller;
    }

    public static void setController(GuiController controller) {
        AppController.controller = controller;
    }

    public static MainFrame getFrame() {
        return frame;
    }

    public static void setFrame(MainFrame frame) {
        AppController.frame = frame;
    }
    
    public void showLoginScreen(){

        LoginPanel panel=new LoginPanel(this);
        panel.setBackground(Color.RED);
        frame.setLayout(null);
        frame.setLoginPanel(panel);
        frame.validate();
        frame.repaint();
    }
    
    public void logout(){
        frame.remove(controller.getLp());
        frame.remove(controller.getMainPanel());
        frame.setStatus("Logout");
        frame.getStatusPanel().setLoggedAs("Not logged in");
        frame.getStatusPanel().setRole("");
        showLoginScreen();
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

//                        creating view
//                        frame.setMainPanel(new MainPanel());
                        controller = createGuiController(user, frame);
                        controller.setStartingView();
                        frame.getStatusPanel().setRole(user.getRole());
                        frame.getStatusPanel().setLoggedAs("Logged as: ");


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

    public void showOptionPaneOutsideEDT(final String title, final String message) {
        showOptionPaneOutsideEDT(title, message, JOptionPane.DEFAULT_OPTION);
    }

    public void showOptionPaneOutsideEDT(final String title, final String message, final int option) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JOptionPane.showConfirmDialog(frame, message, title, option);

            }
        });
    }

//    FACTORY
    public GuiController createGuiController(User user, MainFrame frame) {
        if (user.isAdmin()) {
            return new AdminGuiController(frame);
        }
        if (user.isDoctor()) {
            return new DoctorGuiController(frame);
        }
//        default
//        return null;
        return new DoctorGuiController(frame);
    }
}
