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
public class NeuralNetworkOptymalizator implements Runnable{

    static private int INPUTS = 13,OUTPUTS = 1;

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

    public double _validationAcc, _validationMSE;
    
    private boolean breakFlag=false;
    private LearningProcessController controller;

    DataSet trainingSet;
    DataSet generalizationSet;
    DataSet validationSet;

    public NeuralNetworkOptymalizator(){
    // Konstruktor domyślny ustawia parametry do przetestowania
        _minNumHiddenLayers = 1;
        _maxNumHiddenLayers = 1;
        _minNumNeuronsPerHiddenLayer = 5;
        _maxNumNeuronsPerHiddenLayer = 8;

        _learningRateSet = new double[]{0.05};
        _learningRateAdjustSet = new double[]{0.003};
        _momentumSet = new double[]{0.4};
        _forgettingRateSet = new double[]{0.000001};
    }

    @Override
    public void run(){
        try{
    // Wczytanie danych testowych z bazy danych i i ch podział na 3 grupy
            DataSet data= new DBDataLoader().loadData();
            DataPreprocessor preprocessor = new DataPreprocessor();
            preprocessor.preprocessData(data);

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
            int q=1;
            for(int h = _minNumHiddenLayers; h <= _maxNumHiddenLayers; h++)
                for(int p = _minNumNeuronsPerHiddenLayer; p <= _maxNumNeuronsPerHiddenLayer; p++)
                    for(int lr = 0 ; lr < _learningRateSet.length; lr++)
                        for(int lra = 0; lra < _learningRateAdjustSet.length; lra++)
                            for(int m = 0; m < _momentumSet.length; m++)
                                for(int fr = 0; fr < _forgettingRateSet.length ; fr++){
        // Testowanie sieci dla podanych parametrów
                                    if(breakFlag){
                                        clean();
                                        return;
                                    }
                                    System.out.println("Iteracja : "+q+"\n Hidden Layers: "+h+"\n Perceptrony: "+p+"\n LearningRate: "+
                                            _learningRateSet[lr]+"\n LearningRateAdj: "+_learningRateAdjustSet[lra]+
                                            "\n Momentum: "+_momentumSet[m]+"\n ForgottingRate: "+_forgettingRateSet[fr]);

                                    _network = new NeuralNetwork(INPUTS, OUTPUTS, h, p);
                                    _nnTrainer = new NeuralNetworkTrainer(_network);

                                    controller.setCurrentNet(_network);
                                    controller.setTrainer(_nnTrainer);

                                    _nnTrainer.setLearningRate(_learningRateSet[lr]);
                                    _nnTrainer.setLearningRateAdjust(_learningRateAdjustSet[lra]);
                                    _nnTrainer.setMomentumConst(_momentumSet[m]);
                                    _nnTrainer.setForgettingRate(_forgettingRateSet[fr]);

                                    _nnTrainer.addListener(controller);

                                    _nnTrainer.trainNetwork(trainingSet, generalizationSet, validationSet);

                                    _validationAcc= _nnTrainer.getValidationSetAccuracy();
                                    _validationMSE= _nnTrainer.getValidationSetMSE();
                                    System.out.println("accuracy: "+_validationAcc+" MSE: "+_validationMSE);
                                    controller.updateValidationParams(_validationAcc, _validationMSE);
                                    q++;
                                }
            controller.notifyFinished();
        } catch(Exception ex){
            System.out.println("Error: "+ex.toString());
            //controller.processLearningExceptions(ex);
        }
    }  
    /*
     * przy przerwaniu przez uzytkownika, jesli trzeba cos posprzatac
     */
    public void clean(){
        
    }
    
    public void interrupt(){
        breakFlag=true;
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
