/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.GUI;

import heartdoctor.ann.NeuralNetworkTrainingListener;
import java.awt.Color;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author michal
 */
public class AdminChartPanel extends JPanel implements NeuralNetworkTrainingListener{
    private XYSeries trainingSetAccuracy  = new XYSeries("Training set accuracy");
    private XYSeries trainingSetMSE = new XYSeries("Training set MSE");
    private XYSeries generalizationSetAccuracy = new XYSeries("Generalization set accuracy");
    private XYSeries generalizationSetMSE = new XYSeries("Generalization set MSE");
    
    private XYSeriesCollection dataset = new XYSeriesCollection();
    
    JFreeChart chart;
    ChartPanel panel;
    
    int tsaIter=0;
    int tsMSEIter=0;
    int gsaIter=0;
    int gsMSEIter=0;

    
    public AdminChartPanel(){
        super();
        dataset.addSeries(trainingSetAccuracy);
        dataset.addSeries(trainingSetMSE);
        dataset.addSeries(generalizationSetAccuracy);
        dataset.addSeries(generalizationSetMSE);
        
        trainingSetAccuracy.add(tsaIter, 0);
        trainingSetMSE.add(tsMSEIter, 0);
        generalizationSetAccuracy.add(gsaIter, 0);
        generalizationSetMSE.add(gsMSEIter, 0);
          
        chart=ChartFactory.createXYLineChart("ANN training","iteration",
                "value",dataset,PlotOrientation.VERTICAL,
                true,true,false);
        panel=new ChartPanel(chart);
        panel.setBackground(Color.CYAN);
        add(panel);
        setBackground(Color.LIGHT_GRAY);
        
    }

    @Override
    public void updateTrainingSetAccuracy(double accuracy) {
        trainingSetAccuracy.add(++tsaIter, accuracy);
    }

    @Override
    public void updateTrainingSetMSE(double mse) {
        trainingSetMSE.add(++tsMSEIter,mse);
    }

    @Override
    public void updateGeneralizationSetAccuracy(double accuracy) {
        generalizationSetAccuracy.add(++gsaIter, accuracy);
    }

    @Override
    public void updateGeneralizationSetMSE(double mse) {
        generalizationSetMSE.add(gsMSEIter, mse);
    }

    @Override
    public void nextEpoch() {
    }
}
