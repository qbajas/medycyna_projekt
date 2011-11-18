/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heartdoctor.GUI;

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
public class AdminChartPanel extends JPanel {
    private XYSeries trainingSetAccuracy  = new XYSeries("Training set accuracy");
    private XYSeries trainingSetMSE = new XYSeries("Training set MSE");
    private XYSeries generalizationSetAccuracy = new XYSeries("Generalization set accuracy");
    private XYSeries generalizationSetMSE = new XYSeries("Generalization set MSE");
    
    private XYSeriesCollection dataset = new XYSeriesCollection();
    
    JFreeChart chart;
    ChartPanel panel;
    int iter=0;

    
    public AdminChartPanel(){
        super();
        dataset.addSeries(trainingSetAccuracy);
        dataset.addSeries(trainingSetMSE);
        dataset.addSeries(generalizationSetAccuracy);
        dataset.addSeries(generalizationSetMSE);
        
        trainingSetAccuracy.add(iter, 0);
        trainingSetMSE.add(iter, 0);
        generalizationSetAccuracy.add(iter, 0);
        generalizationSetMSE.add(iter, 0);
          
        chart=ChartFactory.createXYLineChart("ANN training","iteration",
                "value",dataset,PlotOrientation.VERTICAL,
                true,true,false);
        panel=new ChartPanel(chart);
        panel.setBackground(Color.CYAN);
        add(panel);
        setBackground(Color.LIGHT_GRAY);
        
    }
}
