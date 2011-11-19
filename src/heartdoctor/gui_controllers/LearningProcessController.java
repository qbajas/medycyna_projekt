/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.gui_controllers;

import heartdoctor.DataModel.NetworkStatistics;
import heartdoctor.GUI.NetworkStats;
import heartdoctor.ann.NeuralNetwork;
import heartdoctor.ann.NeuralNetworkOptymalizator;
import heartdoctor.ann.NeuralNetworkTrainer;
import heartdoctor.ann.NeuralNetworkTrainingListener;
import heartdoctor.application.AppController;
import javax.swing.JOptionPane;

/**
 *
 * @author michal
 */
public class LearningProcessController implements NeuralNetworkTrainingListener{
    NetworkStatistics current;
    NetworkStatistics best;
    
    NeuralNetwork bestNet, currentNet;
    NeuralNetworkOptymalizator optymalizer;
    NetworkStats statsGUI;
    
    private int epoch;
    private boolean running=false;
    
    public LearningProcessController(){    
    }
    
    public void startLearning(){
        current=new NetworkStatistics();
        best=new NetworkStatistics();
        epoch=1;
        updateGUI();
        
        AppController.getFrame().setStatus("Learning in progress");
        running=true;
        statsGUI.startButton.setText("Interrupt");
        optymalizer=new NeuralNetworkOptymalizator();
        optymalizer.setController(this);
        new Thread(optymalizer).start();  
    }
    
    public void processLearningExceptions(Exception ex){
            JOptionPane.showMessageDialog(AppController.getFrame(),
                "ERRORS occured. Error message: "+ex.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
            optymalizer.interrupt();
            running=false;
            statsGUI.startButton.setText("Start learning");
            AppController.getFrame().setStatus("Interrupted");
    }
    
    public void startButtonClick(){
        if(running){
            interruptLearning();
        } else
            startLearning();
    }
    
    public void saveButtonClick(){
        
    }
    
    public void interruptLearning(){
        int option=JOptionPane.showConfirmDialog(AppController.getFrame(),"Are you sure"
                + "to stop learning process?","Confirm",JOptionPane.YES_NO_OPTION);
        System.out.println("option========="+option);
        System.out.println("JOPTION======="+JOptionPane.YES_OPTION);
        
        if(option==JOptionPane.YES_OPTION){
            optymalizer.interrupt();
            running=false;
            statsGUI.startButton.setText("Start learning");
            AppController.getFrame().setStatus("Interrupted");
        }    
    }
    
    public void notifyFinished(){
        AppController.get().showOptionPaneOutsideEDT("Success", "ANN learning completed");
        AppController.getFrame().setStatus("Finished");
        running=false;
        statsGUI.startButton.setText("Start learning");
    }

    public void setCurrentNet(NeuralNetwork net){
        currentNet=net;
        current.perceptrons=net.getNumNeuronsPerHiddenLayer();
        current.hidden=net.getNumHiddenLayers();
        updateGUI();
    }
    
    public void updateValidationParams(double accuracy, double mse){
        current.valAcc=accuracy;
        current.valMSE=mse;
        if(best.valAcc<current.valAcc){
            try{
                best= current.clone();
                bestNet=currentNet;
            } 
            catch(CloneNotSupportedException ex){} //obsluga wyjatku przez ignorowanie
        }
        updateGUI();
    }
    
    public void updateGUI(){
        refreshGUI(new GUIProcess());
    }
    
    public void refreshGUI(Runnable runnable){
        javax.swing.SwingUtilities.invokeLater(runnable);
    }
    
    
    
    @Override
    public void updateTrainingSetAccuracy(double accuracy) {
        current.tsAcc=accuracy;
        updateGUI();
    }

    @Override
    public void updateTrainingSetMSE(double mse) {
        current.tsMSE=mse;
        updateGUI();
    }

    @Override
    public void updateGeneralizationSetAccuracy(double accuracy) {
        current.gsAcc=accuracy;
        updateGUI();
    }

    @Override
    public void updateGeneralizationSetMSE(double mse) {
        current.gsMSE=mse;
        updateGUI();
    }

    @Override
    public void nextEpoch() {
        ++epoch;
        updateGUI();
        
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
    
    public void setTrainer(NeuralNetworkTrainer trainer){
        trainer.addListener(statsGUI.getAdminChartPanel1());
    }
    
    private class GUIProcess implements Runnable{

        @Override
        public void run() {
            statsGUI.updateCurrent(current);
            statsGUI.updateBest(best);
            
        }
        
    }
}
