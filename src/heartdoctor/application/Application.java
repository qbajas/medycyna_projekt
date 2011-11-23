package heartdoctor.application;

import heartdoctor.GUI.MainFrame;
import java.util.Locale;
import javax.swing.JFrame;

/**
 * Klasa inicjalizująca aplikację, tworzy okno aplikacji, kontrolery itp.
 * @author michal
 */
public class Application {

    MainFrame frame;
    AppController controller;

    public Application() {
        Locale.setDefault(Locale.ENGLISH);
        frame = new MainFrame();
        AppController.setFrame(frame);
        controller = AppController.get();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controller.showLoginScreen();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                frame.setVisible(true);

            }
        });

    }

    /**
     * Uruchamia aplikację
     * @param args 
     */
    public static void main(String[] args) {
        new Application();
    }
}
