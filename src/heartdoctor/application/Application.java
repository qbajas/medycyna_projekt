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
        controller=new AppController(frame);
 
        LoginPanel panel=new LoginPanel(controller);
        panel.setBackground(Color.RED);
        frame.setLayout(null);
//        frame.add(panel);
        frame.setLoginPanel(panel);
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

////			  FileDataLoader dataLoader = new FileDataLoader("processed.cleveland.data");
//			  DataSet data = new DBDataLoader().loadData();//dataLoader.loadData();
//			  DataPreprocessor preprocessor = new DataPreprocessor();
//			  preprocessor.preprocessData(data);
//
//			  NeuralNetwork net = new NeuralNetwork(data.entries.get(0).patterns.size(), data.entries.get(0).targets.size(), 1, 8);
//			  NeuralNetworkTrainer netTrainer = new NeuralNetworkTrainer(net);
//			  netTrainer.trainNetwork(data, data, data);

                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }

    
}
