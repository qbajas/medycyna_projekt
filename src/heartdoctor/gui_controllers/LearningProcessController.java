package heartdoctor.gui_controllers;

import heartdoctor.DataModel.NetworkStatistics;
import heartdoctor.GUI.NetworkStats;
import heartdoctor.Util.ANNSerializer;
import heartdoctor.ann.NeuralNetwork;
import heartdoctor.ann.NeuralNetworkOptymalizator;
import heartdoctor.ann.NeuralNetworkTrainer;
import heartdoctor.ann.NeuralNetworkTrainingListener;
import heartdoctor.application.AppController;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.ValueAxis;

/**
 * Klasa kontrolujaca proces uczenia się sieci neuronowej. Odpowiedzialna za 
 * uruchamianie i zatrzymywanie nauki, zdarzenia z GUI oraz klasy odpowiedzialne
 * za trening sieci neuronowej wywołują funkcje tego kontrolera
 * @author michal
 */
public class LearningProcessController implements NeuralNetworkTrainingListener{
    NetworkStatistics current;
    NetworkStatistics best;
    
    NeuralNetwork bestNet, currentNet;
    NeuralNetworkOptymalizator optymalizer;
    NeuralNetworkTrainer trainer;
    NetworkStats statsGUI;
    
    private int epoch;
    private static boolean running=false;
    private static LearningProcessController instance;
    
    /**
     * Zwraca instancję kontrolera, w całej aplikacji jest tylko jeden kontroler.
     * Można w ten sposób sprawdzić w innych wątkach czy proces nauki nadal trwa.
     * @return Instancja LearningProcessController
     */
    public static LearningProcessController get(){
        if(instance==null)
            instance=new LearningProcessController();
        return instance;
    }
    
    private LearningProcessController(){    
    }
    
    /**
     * Uruchamia proces uczenia się sieci, aktualizuje GUI i statystyki sieci.
     */
    public void startLearning(){
        current=new NetworkStatistics();
        best=new NetworkStatistics();
        epoch=1;
        updateGUI();
        
        AppController.getFrame().setStatus("Learning in progress");
        running=true;
        statsGUI.startButton.setText("Interrupt");
        statsGUI.saveButton.setEnabled(false);
        optymalizer=new NeuralNetworkOptymalizator();
        optymalizer.setController(this);
        statsGUI.getAdminChartPanel1().resetSeries();
        new Thread(optymalizer).start();  
    }
    
    /**
     * Obsluga wyjątków w trakcie nauki. Jeżeli w którymś momencie jest łapany
     * wyjątek, którego nie można obsłużyć i nie można kontynuować nauki należy
     * wywołać tą funkcję w bloku catch. Kontroler przerwie naukę, zaktualizuje GUI
     * i powiadomi użytkownika o problemach.
     * @param ex Wyjątek który wystąpił w trakcie nauki sieci
     */
    public void processLearningExceptions(Exception ex){
            JOptionPane.showMessageDialog(AppController.getFrame(),
                "ERRORS occured. Error message: "+ex.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
            optymalizer.interrupt();
            running=false;
            statsGUI.startButton.setText("Start learning");
            AppController.getFrame().setStatus("Interrupted");
            statsGUI.saveButton.setEnabled(false);
    }
    
    /**
     * Funkcja wywoływana z GUI przy nacisnięciu przycisku Start/Interrupt.
     * W zależności od stanu programu przerywa albo uruchamia naukę sieci.
     */
    public void startButtonClick(){
        if(running){
            interruptLearning();
        } else
            startLearning();
    }
    
    /**
     * Funkcja wywoływana przy wciśnięciu guzika "Save". Zapisuje do bazy sieć, która
     * uzyskała najlepszy wynik w trakcie nauki
     */
    public void saveButtonClick(){
        try{
            AppController.getFrame().setStatus("Saving...");
            ANNSerializer.writeANN(bestNet);
            AppController.getFrame().setStatus("Saved...");
        } catch(SQLException ex){
            AppController.getFrame().setStatus("");
            JOptionPane.showMessageDialog(AppController.getFrame(),
                "ERRORS occured. Error message: "+ex.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * Przerywa wykonanie wątku uczącego sieć
     */
    public void interrupt(){
        optymalizer.interrupt();
        trainer.interrupt();
        running=false;
    }
    
    /**
     * Sprawdza czy trwa obecnie nauka sieci
     * @return true jeśli wątek nauki sieci jest aktywny, false jeśli się 
     * zakończył lub został przerwany
     */
    public static boolean isRunning(){
        return running;
    }
    
    /**
     * Wywoływana po wciśnięciu guzika start/interrupt. Wyświetla komunikat z 
     * prośbą o potwierdzenie. W przypadku potwierdzenia przerywa natychmiast
     * naukę sieci i dezaktywuje guzik do zapisu sieci.
     */
    private void interruptLearning(){
        int option=JOptionPane.showConfirmDialog(AppController.getFrame(),"Are you sure"
                + "to stop learning process?","Confirm",JOptionPane.YES_NO_OPTION);
        
        if(option==JOptionPane.YES_OPTION){
            interrupt();
            statsGUI.startButton.setText("Start learning");
            if(bestNet!=null)
                statsGUI.saveButton.setEnabled(true);
            AppController.getFrame().setStatus("Interrupted");
        }    
    }
    
    /**
     * Wywoływane przez funkcje klas uczących, w celu poinformowania kontrolera
     * o zakończeniu wykonywania wątku uczącego sieć.
     */
    public void notifyFinished(){
        AppController.get().showOptionPaneOutsideEDT("Success", "ANN learning completed");
        AppController.getFrame().setStatus("Finished");
        running=false;
        statsGUI.saveButton.setEnabled(true);
        statsGUI.startButton.setText("Start learning");
    }

    /**
     * Ustawia aktualnie uczoną sieć
     * @param net 
     */
    public void setCurrentNet(NeuralNetwork net){
        currentNet=net;
        current.perceptrons=net.getNumNeuronsPerHiddenLayer();
        current.hidden=net.getNumHiddenLayers();
        updateGUI();
    }
    
    /**
     * Odswieza parametry zbioru walidacyjnego dla danej sieci
     * @param accuracy Skuteczność dla zbioru walidacyjnego
     * @param mse  Błąd średniokwadratowy dla zbioru walidacyjnego
     */
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
    
    /**
     * Odświeża GUI
     */
    public void updateGUI(){
        refreshGUI(new GUIProcess());
    }
    
    public void refreshGUI(Runnable runnable){
        javax.swing.SwingUtilities.invokeLater(runnable);
    }
    
    
    /**
     * Odswieża skuteczność zbioru treningowego
     * @param accuracy Skuteczność zbioru treningowego
     */
    @Override
    public void updateTrainingSetAccuracy(double accuracy) {
        current.tsAcc=accuracy;
        updateGUI();
    }

    /**
     * Odswieża błąd średniokwadratowy zbioru treningowego
     * @param mse Błąd średnikwadratowy zbioru treningowego
     */
    @Override
    public void updateTrainingSetMSE(double mse) {
        current.tsMSE=mse;
        updateGUI();
    }

    /**
     * Odświeża skuteczność zbioru generalizacyjnego
     * @param accuracy Skuteczność zbioru generalizacyjnego
     */
    @Override
    public void updateGeneralizationSetAccuracy(double accuracy) {
        current.gsAcc=accuracy;
        updateGUI();
    }

    /**
     * Odswieża błąd średniokwadratowy zbioru generalizacyjnego
     * @param mse Błąd średnikwadratowy zbioru generalizacyjnego
     */
    @Override
    public void updateGeneralizationSetMSE(double mse) {
        current.gsMSE=mse;
        updateGUI();
    }

    /**
     * Przechodzi do następnej epoki, odświeża odpowiednie parametry w GUI
     */
    @Override
    public void nextEpoch() {
        ++epoch;
        updateGUI();
        if(epoch>200){
            ChartPanel panel= statsGUI.getAdminChartPanel1().getChartPanel();
            ValueAxis axis=panel.getChart().getXYPlot().getDomainAxis();
            axis.setRange(epoch-100, epoch+100);
        }
            
    }

    /**
     * Zwraca optymalizatora sieci neuronowej
     * @return Optymalizator sieci
     */
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
        this.trainer = trainer;
        trainer.addListener(statsGUI.getAdminChartPanel1());
    }
    
    private class GUIProcess implements Runnable{

        @Override
        public void run() {
            statsGUI.updateCurrent(current);
            statsGUI.updateBest(best);
            statsGUI.epoch.setText(""+epoch);
        }
        
    }
}
