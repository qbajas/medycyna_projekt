/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.application;

import heartdoctor.GUI.DiagnosisPanel;
import heartdoctor.GUI.LeftPanel;
import heartdoctor.GUI.MainPanel;
import heartdoctor.ann.DataPreprocessor;
import heartdoctor.ann.DataSet;
import heartdoctor.ann.FileDataLoader;
import heartdoctor.ann.NeuralNetwork;
import heartdoctor.ann.NeuralNetworkTrainer;
import javax.swing.JFrame;

/**
 *
 * @author michal
 */
public class Application {
    JFrame frame;
    MainPanel mainPanel;
    
    public Application(){
//        frame= new JFrame();
//        frame.setSize(800, 450);
//       // frame.setResizable(false);
//        mainPanel=new MainPanel();
//        mainPanel.setLeftPanel(new LeftPanel());
//        mainPanel.setContentPanel(new DiagnosisPanel());
//        frame.add(mainPanel);
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

			  FileDataLoader dataLoader = new FileDataLoader("processed.cleveland.data");
			  DataSet data = dataLoader.loadData();
			  DataPreprocessor preprocessor = new DataPreprocessor();
			  preprocessor.preprocessData(data);

			  NeuralNetwork net = new NeuralNetwork(data.entries.get(0).patterns.size(), data.entries.get(0).targets.size(), 1, 7);
			  NeuralNetworkTrainer netTrainer = new NeuralNetworkTrainer(net);
			  netTrainer.trainNetwork(data, data, data);

               // frame.setVisible(true);
               // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }

    
}
