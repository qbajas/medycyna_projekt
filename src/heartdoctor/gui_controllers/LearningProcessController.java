/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.gui_controllers;

import heartdoctor.DataModel.NetworkStatistics;
import heartdoctor.GUI.NetworkStats;
import heartdoctor.ann.NeuralNetworkOptymalizator;
import heartdoctor.ann.NeuralNetworkTrainingListener;
import heartdoctor.application.AppController;
import javax.swing.JOptionPane;

/**
 *
 * @author michal
 */
public class LearningProcessController implements NeuralNetworkTrainingListener{
    NetworkStatistics current=new NetworkStatistics();
    NetworkStatistics best=new NetworkStatistics();
    
    NeuralNetworkOptymalizator optymalizer;
    NetworkStats statsGUI;
    
    public LearningProcessController(){    
    }
    
    public void startLearning(){
        AppController.getFrame().setStatus("Learning in progress");
        new Thread(optymalizer).start();
    }
    
    public void interruptLearning(){
        int option=JOptionPane.showConfirmDialog(AppController.getFrame(),"Confirm","Are you sure"
                + "to stop learning process?",JOptionPane.YES_NO_OPTION);
        
        if(option==JOptionPane.YES_OPTION)
            optymalizer.interrupt();
    }
    
    public void notifyFinished(){
        AppController.get().showOptionPaneOutsideEDT("Success", "ANN learning completed");
        AppController.getFrame().setStatus("");
    }

    @Override
    public void updateTrainingSetAccuracy(double accuracy) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateTrainingSetMSE(double mse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateGeneralizationSetAccuracy(double accuracy) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateGeneralizationSetMSE(double mse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void nextEpoch() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NeuralNetworkOptymalizator getOptymalizer() {
        return optymalizer;
    }

    public void setOptymalizer(NeuralNetworkOptymalizator optymalizer) {
        this.optymalizer = optymalizer;
        optymalizer.setController(this);
    }

    public NetworkStats getStatsGUI() {
        return statsGUI;
    }

    public void setStatsGUI(NetworkStats statsGUI) {
        this.statsGUI = statsGUI;
        statsGUI.setLearnController(this);
    }
    
    
    
}
