package heartdoctor.application;

import heartdoctor.DataModel.User;
import heartdoctor.GUI.LoginPanel;
import heartdoctor.GUI.MainFrame;
import heartdoctor.Util.ANNSerializer;
import heartdoctor.Util.SecurityController;
import heartdoctor.ann.NeuralNetwork;
import heartdoctor.gui_controllers.AdminGuiController;
import heartdoctor.gui_controllers.DoctorGuiController;
import heartdoctor.gui_controllers.GuiController;
import heartdoctor.gui_controllers.LearningProcessController;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Główny kontroler aplikacji, nadzoruje logowanie i umożliwia dostęp do 
 * współdzielonych zasobów
 * @author michal
 */
public class AppController {

    static MainFrame frame;
    static GuiController controller;
    private static AppController instance;
    private static NeuralNetwork neuralNetwork = null;

    private AppController() {
    }

    public static NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    /**
     * Zwraca instancję kontrolera aplikacji, tworzy tylko jeden kontroler na aplikację
     * @return AppController
     */
    public static AppController get() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }

    /**
     * Zwraca kontrolera GUI
     * @return 
     */
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

    /**
     * Wyświetla ekran do logowania
     */
    public void showLoginScreen() {

        LoginPanel panel = new LoginPanel(this);
        frame.setLayout(null);
        frame.setLoginPanel(panel);
        frame.validate();
        frame.repaint();
    }

    /**
     * Wylogowuje użytkownika
     */
    public void logout() {
        int option = JOptionPane.showConfirmDialog(frame, "Are you sure"
                + " to logout?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        frame.remove(controller.getLp());
        frame.remove(controller.getMainPanel());
        frame.setStatus("Logout");
        frame.getStatusPanel().setLoggedAs("Not logged in");
        frame.getStatusPanel().setRole("");
        neuralNetwork = null;
        if (LearningProcessController.isRunning()) {
            LearningProcessController.get().interrupt();
        }
        showLoginScreen();
    }

    /**
     * Przeprowadza autoryzacje użytkownika i przechodzi do innych ekranów
     * w zależności od roli użytkownika
     * @param user 
     */
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


                        controller = createGuiController(user, frame);
                        if (user.isDoctor()) {
                            try {
                                neuralNetwork = ANNSerializer.readLastANN();
                            } catch (Exception ex) {
                                javax.swing.SwingUtilities.invokeLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        JOptionPane.showMessageDialog(frame,
                                                "Error when loading ANN. Check your DB connection. "
                                                + "Exiting", "Critical error", JOptionPane.ERROR_MESSAGE);
                                        System.exit(-1);
                                    }
                                });
                            }
                        }
                        controller.setStartingView();
                        frame.getStatusPanel().setRole(user.getRole());
                        frame.getStatusPanel().setLoggedAs("Logged as: ");


                    } else {
                        showOptionPaneOutsideEDT("Access denied",
                                "Wrong login or password entered");
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

    /**
     * Wyświetla JOptionPane spoza EventDispatchThread z domyślną opcją
     * @param title
     * @param message 
     */
    public void showOptionPaneOutsideEDT(final String title, final String message) {
        showOptionPaneOutsideEDT(title, message, JOptionPane.DEFAULT_OPTION);
    }

    /**
     * Wyświetla JOptionPane spoza EventDispatchThread
     * @param title
     * @param message
     * @param option 
     */
    public void showOptionPaneOutsideEDT(final String title, final String message, final int option) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JOptionPane.showConfirmDialog(frame, message, title, option);

            }
        });
    }

    /**
     * Funkcja fabryki tworząca kontroler GUI w zależności od roli użytkownika
     * @param user
     * @param frame
     * @return 
     */
    public GuiController createGuiController(User user, MainFrame frame) {
        if (user.isAdmin()) {
            return new AdminGuiController(frame);
        }
        if (user.isDoctor()) {
            return new DoctorGuiController(frame);
        }
        return new DoctorGuiController(frame);
    }
}
