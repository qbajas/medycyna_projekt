/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.GUI;

import heartdoctor.ann.NeuralNetworkTrainingListener;
import java.awt.Color;
import java.util.Locale;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;
import org.jfree.data.general.DatasetGroup;
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
    
    private XYSeriesCollection datasetA = new XYSeriesCollection();
    private XYSeriesCollection datasetMSE = new XYSeriesCollection();

    JFreeChart chart;
    ChartPanel panel;
    
    int tsaIter=0;
    int tsMSEIter=0;
    int gsaIter=0;
    int gsMSEIter=0;

    
    public AdminChartPanel(){
        super();
        
        resetSeries();
        
        
        
        datasetA.addSeries(trainingSetAccuracy);
        datasetMSE.addSeries(trainingSetMSE);
        datasetA.addSeries(generalizationSetAccuracy);
        datasetMSE.addSeries(generalizationSetMSE);  
        
        
        chart=ChartFactory.createXYLineChart("ANN training","iteration",
                "ACC",datasetA,PlotOrientation.VERTICAL,
                true,true,false);
        
        
        panel=new ChartPanel(chart,true,true,false,true,true);
        panel.setLocale(Locale.UK);
        
        final XYPlot plot = chart.getXYPlot();
        final NumberAxis axis2 = new NumberAxis("MSE");
        axis2.setAutoRangeIncludesZero(false);
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, datasetMSE);
        
        plot.mapDatasetToRangeAxis(1, 1);
        final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
        renderer2.setSeriesPaint(0, Color.black);
        renderer2.setSeriesPaint(1, Color.ORANGE);

        plot.setRenderer(1, renderer2);
        
        

        panel.setBackground(Color.CYAN);
        add(panel);
        setBackground(Color.LIGHT_GRAY);
    }

    public ChartPanel getChartPanel(){
        return panel;
    }
    
    public final void resetSeries(){
        
        tsaIter=0;
        tsMSEIter=0;
        gsaIter=0;
        gsMSEIter=0;
        
        trainingSetAccuracy.clear();
        trainingSetMSE.clear();
        generalizationSetAccuracy.clear();
        generalizationSetMSE.clear();
        
        trainingSetAccuracy.add(tsaIter, 0);
        trainingSetMSE.add(tsMSEIter, 0);
        generalizationSetAccuracy.add(gsaIter, 0);
        generalizationSetMSE.add(gsMSEIter, 0);
        
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
        generalizationSetMSE.add(++gsMSEIter, mse);
    }

    @Override
    public void nextEpoch() {
    }
}
