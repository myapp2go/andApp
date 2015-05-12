package com.pc.androidapp;

import java.util.HashMap;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.demo.DemoView;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.plot.XYPlot;
import org.afree.data.category.CategoryDataset;
import org.afree.data.xy.XYDataset;
import org.afree.data.xy.XYSeries;
import org.afree.data.xy.XYSeriesCollection;
import org.afree.graphics.SolidColor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;

public class TaxLineChartView extends DemoView {

	public TaxLineChartView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		SharedPreferences pref = context.getSharedPreferences("MyPref", 0); 
		Editor editor = pref.edit();
		String str = pref.getString("agi1", null);
		System.out.println("*****************************************agi522 " + str);
		
        AFreeChart chart = createChart(createDataset());

        setChart(chart);
	}

	private AFreeChart createChart(XYDataset dataset) {
		// TODO Auto-generated method stub
        AFreeChart chart = ChartFactory.createXYLineChart(
                "Paul by AFreeChart Line Chart",      // chart title
                "AGI",               // domain axis label
                "Value",         // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
            );
        
        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));
        
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaintType(new SolidColor(Color.LTGRAY));
        plot.setDomainGridlinePaintType(new SolidColor(Color.WHITE));
        plot.setRangeGridlinePaintType(new SolidColor(Color.WHITE));
        
        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
//       domain.setRange(30000.00, 40000.00);
        
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setRange(0.00, 25000.00);
        
		return chart;
	}

	private XYDataset createDataset() {
		// TODO Auto-generated method stub
				
        final XYSeries series1 = new XYSeries("First");
        float base = 22000;
        float delta = 2000;
        float inc = 0;
        float dect = 12400 + 7900;
        int count = 25;
        float[] diff1 = new float[count];
        for (int i = 0; i < count; i++) {
        	diff1[i] = getTax(base+inc, dect);
        	series1.add(inc, diff1[i]);
        	inc += delta;
        }

        final XYSeries series2 = new XYSeries("Second");
        float base2 = 92000;
        float inc2 = 0;
        float[] diff2 = new float[count];
        dect = 22400 + 7900;
        for (int i = 0; i < count; i++) {
        	diff2[i] = getTax(base2-inc2, dect);
        	series2.add(inc2, diff2[i]);
        	inc2 += delta;
        }

        final XYSeries series3 = new XYSeries("Third");

        float inc3 = 0;
        for (int i = 0; i < count; i++) {
        	series3.add(inc3, diff2[0]-diff2[i]);
        	inc3 += delta;
        }
        
        final XYSeries series4 = new XYSeries("Four");

        float inc4 = 0;
        for (int i = 0; i < count; i++) {
        	series4.add(inc4, diff2[0]-diff2[i]-diff1[i]);
        	inc4 += delta;
        }
        
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
        dataset.addSeries(series4);
        
        return dataset;
	}

	private float getTax(float agi, float dect) {
		double tax = 0;
		
		float taxable = agi - dect;
		 if (taxable > 73801) {
			 tax += (taxable - 73800) * 0.25;
			 taxable = 73800;
		 }
		 if (taxable > 18151) {
			 tax += (taxable - 18150) * 0.15;
			 taxable = 18150;
		 }		
		 if (taxable > 0) {
			 tax += taxable * 0.1;
		 }	
		 
		 float care = getCare(agi);
		 
		 tax += care;
		 
		 double credit = 0;
		 if (agi <= 36000) {
			 credit = agi * 0.05;
		 }
		 if (agi > 36000 && agi <= 39000) {
			 credit = agi * 0.02;
		 }
		 if (agi > 39000 && agi <= 60000) {
			 credit = agi * 0.01;
		 }
		 
		 tax -= credit;
		 
		 System.out.println("AGI TAX : " + agi + " ==> " + tax);
		 
		 return (float)tax;
	}

	private float getTax2(float agi) {
		return agi;
	}
	
	HashMap map = new HashMap<String, String>() {
        {
        	put("141", "0.0347");
            put("154", "0.0418");
            put("167", "0.0478");
            put("180", "0.0538");
            put("193", "0.0598");
            put("206", "0.0651");
            put("219", "0.0697");
            put("232", "0.0742");
            put("245", "0.0788");
            put("257", "0.0825");
            put("270", "0.0863");
            put("283", "0.0901");
            put("296", "0.0938");
            put("309", "0.0950");
            put("322", "0.0950");
            put("335", "0.0950");
            put("348", "0.0950");
            put("361", "0.0950");
            put("373", "0.0950");
            put("386", "0.0950");
            put("399", "0.0950");
        }
	};
	
	private float getCare(float agi) {
		if (agi > 62000) {
			return 13132;
		}
		// TODO Auto-generated method stub
		int value = (int) (agi / 15510 * 100.0);
		
		float rate = Float.valueOf(map.get(""+value).toString());
//		System.out.println("RATR111 " + rate);
		return agi * rate;
	}
}
