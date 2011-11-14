/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heartdoctor.ann;

import java.util.ArrayList;

/**
 *
 * @author Witek
 */
public class NeuralNetworkOptymalizator implements NeuralNetworkTrainingListener{

    private NeuralNetworkTrainer _nnTrainer;
    private NeuralNetwork _network;

    private int _minNumInputs = 1;
    private int _minNumOutputs = 1;
    private int _minNumHiddenLayers = 1;
    private int _minNumNeuronsPerHiddenLayer = 1;
  
    private int _maxNumInputs;
    private int _maxNumOutputs;
    private int _maxNumHiddenLayers;
    private int _maxNumNeuronsPerHiddenLayer;

    public int optimumInputs;
    public int optimumOutputs;
    public int optimumHiddenLayers;
    public int optimumNeuronsPerHiddenLayer;

    DataSet trainingSet;
    DataSet generalizationSet;
    DataSet validationSet;

    public NeuralNetworkOptymalizator(int nI,int nO, int nHL,int nNPH,DataSet data){
        _maxNumInputs = nI;
        _maxNumOutputs = nO;
        _maxNumHiddenLayers = nHL;
        _maxNumNeuronsPerHiddenLayer = nNPH;
        
        int i=data.entries.size();
        trainingSet.entries = (ArrayList<DataEntry>) data.entries.subList(0, (int)0.6*i);
        generalizationSet.entries = (ArrayList<DataEntry>) data.entries.subList((int)0.6*i+1, (int)0.8*i);
        validationSet.entries = (ArrayList<DataEntry>) data.entries.subList((int)0.8*i+1, i-1);
    }
    
    public NeuralNetworkOptymalizator(int minI, int maxI,int minO, int maxO, int minHL,int maxHL,int minNPH,int maxNPH,DataSet data){
        _minNumInputs = minI;
        _minNumOutputs = minO;
        _minNumHiddenLayers = minHL;
        _minNumNeuronsPerHiddenLayer = minNPH;
        
        _maxNumInputs = maxI;
        _maxNumOutputs = maxO;
        _maxNumHiddenLayers = maxHL;
        _maxNumNeuronsPerHiddenLayer = maxNPH;

        int i=data.entries.size();
        trainingSet.entries = (ArrayList<DataEntry>) data.entries.subList(0, (int)0.6*i);
        generalizationSet.entries = (ArrayList<DataEntry>) data.entries.subList((int)0.6*i+1, (int)0.8*i);
        validationSet.entries = (ArrayList<DataEntry>) data.entries.subList((int)0.8*i+1, i-1);
    }

    public void optimalize(){
        for(int i = _minNumInputs;i<=_maxNumInputs;i++)
            for(int o = _minNumOutputs;o<=_maxNumOutputs; o++)
                for(int h = _minNumHiddenLayers;h<=_maxNumHiddenLayers; h++)
                    for(int p = _minNumNeuronsPerHiddenLayer;p<=_maxNumNeuronsPerHiddenLayer; p++){
                        // DO SOMETHING AMAZING!!! -----------------------------
                        _network = new NeuralNetwork(_minNumInputs, _minNumOutputs, _minNumHiddenLayers, _minNumNeuronsPerHiddenLayer);
                        _nnTrainer = new NeuralNetworkTrainer(_network);
                        _nnTrainer.setListener(this);
                        _nnTrainer.trainNetwork(trainingSet, generalizationSet, validationSet);
                    }
    }

    public void updateTrainingSetAccuracy(double accuracy) {
        throw new UnsupportedOperationException("Not supported yet.");
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

}
