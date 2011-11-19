/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

import heartdoctor.gui_controllers.LearningProcessController;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Witek
 */
public class NeuralNetworkOptymalizator implements NeuralNetworkTrainingListener,
        Runnable{

    static private int INPUTS = 14,OUTPUTS = 1;

    private NeuralNetworkTrainer _nnTrainer;
    private NeuralNetwork _network;

    private int _minNumHiddenLayers = 1;
    private int _minNumNeuronsPerHiddenLayer = 1;
    private double[] _learningRateSet;
    private double[] _learningRateAdjustSet;
    private double[] _momentumSet;
    private double[] _forgettingRateSet;
 
    private int _maxNumHiddenLayers;
    private int _maxNumNeuronsPerHiddenLayer;

    public int optimumHiddenLayers;
    public int optimumNeuronsPerHiddenLayer;
    public double optimumLearningRate;
    public double optimumLearningRateAdjust;
    public double optimumMomentum;
    public double optimumForgettingRate;
    
    private boolean breakFlag=false;
    private LearningProcessController controller;

    DataSet trainingSet;
    DataSet generalizationSet;
    DataSet validationSet;

    public NeuralNetworkOptymalizator(){
        this(4,8,4,20,new double[]{0.1, 0.5 , 1, 1.5}, 
                new double[]{0.1, 0.5 , 1, 1.5},new double[]{0.1, 0.5 , 1, 1.5},
                new double[]{0.1, 0.5 , 1, 1.5} );
    }
    
    public NeuralNetworkOptymalizator(int nHL,int nNPH,double[] lR,double[] lRA,double[] m,double[] fR){
        _maxNumHiddenLayers = nHL;
        _maxNumNeuronsPerHiddenLayer = nNPH;
    }
    
    //Bardziej podupconego konstruktora sie nie dalo zrobic?
    //zrob jakis konstruktor domyslny czy cos, w koncu to ta klasa ma dobrac 
    //optymalne parametry a nie zeby ktos podawal zakresy
    //Wogole co to za zmienne?
    public NeuralNetworkOptymalizator(int minHL,int maxHL,int minNPH,int maxNPH,double[] LR,double[] LRA,double[] M,double[] FR){
        _minNumHiddenLayers = minHL;
        _minNumNeuronsPerHiddenLayer = minNPH;
        _learningRateSet = LR;
        _learningRateAdjustSet = LRA;
        _momentumSet = M;
        _forgettingRateSet = FR;
        
        _maxNumHiddenLayers = maxHL;
        _maxNumNeuronsPerHiddenLayer = maxNPH;
    }

    @Override
    public void run(){
        DataSet data= new DBDataLoader().loadData();
        int i=data.entries.size();

        Collections.shuffle(data.entries);
        trainingSet=new DataSet();
        generalizationSet=new DataSet();
        validationSet=new DataSet();
        
        trainingSet.entries =new ArrayList<DataEntry>();
        trainingSet.entries.addAll( data.entries.subList(0, (int) (0.6*i)) );
        
        generalizationSet.entries = new ArrayList<DataEntry>();
        generalizationSet.entries.addAll( data.entries.subList((int) (0.6*i+1), (int) (0.8*i)) );
        
        validationSet.entries = new ArrayList<DataEntry>();
        validationSet.entries.addAll( data.entries.subList((int) (0.8*i+1), (i-1)) );
        
        //TAKIE RZUTOWANIE NIE PRZEJDZIE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //generalizationSet.entries = (ArrayList<DataEntry>) data.entries.subList((int)0.6*i+1, (int)0.8*i);
        

        
        double _validationAcc, _validationMSE;
        
        for(int h = _minNumHiddenLayers; h <= _maxNumHiddenLayers; h++)
            for(int p = _minNumNeuronsPerHiddenLayer; p <= _maxNumNeuronsPerHiddenLayer; p++)
                for(int lr = 0 ; lr < _learningRateSet.length; lr++)
                    for(int lra = 0; lra <= _learningRateAdjustSet.length; lra++)
                        for(int m = 0; m <= _momentumSet.length; m++)
                            for(int fr = 0; fr <= _forgettingRateSet.length ; fr++){
                        // DO SOMETHING AMAZING!!! -----------------------------
                                if(breakFlag){
                                    clean();
                                    return;
                                }
                                _network = new NeuralNetwork(INPUTS, OUTPUTS, h, p);
                                _nnTrainer = new NeuralNetworkTrainer(_network);
                                
                                controller.setCurrentNet(_network);
                                controller.setTrainer(_nnTrainer);
                                
                                _nnTrainer.setLearningRate(lr);
                                _nnTrainer.setLearningRateAdjust(lra);
                                _nnTrainer.setMomentumConst(m);
                                _nnTrainer.setForgettingRate(fr);
                                _nnTrainer.addListener(this);
                                _nnTrainer.addListener(controller);
                                _nnTrainer.trainNetwork(trainingSet, generalizationSet, validationSet);
                                _validationAcc= _nnTrainer.getValidationSetAccuracy();
                                _validationMSE= _nnTrainer.getValidationSetMSE();
                                
                                controller.updateValidationParams(_validationAcc, _validationMSE);
                            }
        
        controller.notifyFinished();
    }

    
    /*
     * przy przerwaniu przez uzytkownika, jesli trzeba cos posprzatac
     */
    public void clean(){
        
    }
    
    public void interrupt(){
        breakFlag=true;
    }

    public void updateTrainingSetAccuracy(double accuracy) {
        throw new UnsupportedOperationException("Not supported yet.");
        // RYSYJEMY ZAJEBISTE WYKRESYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY!!!!!!!!!!!!
    }

    public void updateTrainingSetMSE(double mse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateGeneralizationSetAccuracy(double accuracy) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateGeneralizationSetMSE(double mse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void nextEpoch() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public LearningProcessController getController() {
        return controller;
    }

    public void setController(LearningProcessController controller) {
        this.controller = controller;
    }

    public NeuralNetwork getNetwork() {
        return _network;
    }

}
