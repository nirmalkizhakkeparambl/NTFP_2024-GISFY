package com.gisfy.ntfp.Utils;

import android.content.Context;
import android.graphics.Color;

import com.gisfy.ntfp.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;

public class BarChartInit {
    public static void setSkillGraph(Context context,HorizontalBarChart barChart, String title, ArrayList<BarEntry> entries){
//        barChart.setDrawBarShadow(false);
//        Description description =new Description();
//        description.setText(title);
//        barChart.setDescription(description);
//        barChart.getLegend().setEnabled(true);
//        barChart.setPinchZoom(false);
//        barChart.setDoubleTapToZoomEnabled(false);
//        barChart.setDrawValueAboveBar(false);
//
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setDrawGridLines(false);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setEnabled(false);
//        xAxis.setDrawAxisLine(false);
//
//
//        YAxis yLeft = barChart.getAxisLeft();
////        yLeft.setAxisMaximum(100f);
//        yLeft.setAxisMinimum(0.100f);
////        yLeft.setEnabled(false);
//        yLeft.setValueFormatter(new IntValueFormatter());
//        xAxis.setLabelCount(5);
//
//
//
//        YAxis yRight = barChart.getAxisRight();
//        yRight.setDrawAxisLine(false);
//        yRight.setDrawGridLines(false);
//        yRight.setEnabled(false);
//        barChart.animateY(2000);



//        BarDataSet bardataset = new BarDataSet(entries, context.getString(R.string.transitpass));
//
//        bardataset.setColors(
//                ContextCompat.getColor(context, R.color.green),
//                ContextCompat.getColor(context, R.color.red_payment),
//                ContextCompat.getColor(context, R.color.yellow));
//
//        barChart.setDrawBarShadow(true);
//        bardataset.setBarShadowColor(Color.argb(40, 150, 150, 150));
//        BarData data =new BarData(bardataset);
//
//        //Set the bar width
//        //Note : To increase the spacing between the bars set the value of barWidth to < 1f
//        data.setBarWidth(0.9f);
//
//        //Finally set the data and refresh the graph
//        barChart.setData( data);
//        barChart.invalidate();
    }
}
