package utils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class GenerateChart extends ApplicationFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6408030248549523851L;
	private static DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
	
	private TimeSeries total;
	
	public GenerateChart(String s) {
		super(s);
		JPanel jpanel = createDemoPanel();
		jpanel.setPreferredSize(new Dimension(700, 270));
		setContentPane(jpanel);
	}


	public static void createDataset(double d,String s) {
		defaultcategorydataset.addValue(d, "a", s);
	}

	private static JFreeChart createChart(CategoryDataset categorydataset) {
		JFreeChart jfreechart = ChartFactory.createLineChart(
				"Java Standard Class Library", null, "Class Count",
				categorydataset, PlotOrientation.VERTICAL, false, true, false);
		jfreechart.addSubtitle(new TextTitle("Number of Classes By Release"));
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		categoryplot.setRangePannable(true);
		categoryplot.setRangeGridlinesVisible(false);
		NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		ChartUtilities.applyCurrentTheme(jfreechart);
		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot
				.getRenderer();
		lineandshaperenderer.setBaseShapesVisible(false);
		lineandshaperenderer.setDrawOutlines(true);
		lineandshaperenderer.setUseFillPaint(true);
		lineandshaperenderer.setBaseFillPaint(Color.white);
		return jfreechart;
	}

	public static JPanel createDemoPanel() {
		JFreeChart jfreechart = createChart(defaultcategorydataset);
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}


	public static void main(String args[]) {
		GenerateChart chart = new GenerateChart(
				"JFreeChart: LineChartDemo1.java");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
