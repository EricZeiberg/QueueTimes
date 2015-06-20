package com.queueTimes.Queue_Times.popups;

import android.app.Activity;
import android.os.Bundle;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.*;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.data.DataObject;
import com.queueTimes.Queue_Times.models.RideInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AveYearGraphPopup extends Activity {

    BarChart chart;
    RideInfo rideInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ave_year_graph);


        rideInfo = (RideInfo) DataObject.getObject();

        chart = (BarChart) findViewById(R.id.ave_year_chart);

        chart.setDescription("Average Yearly Wait Time");

        List<BarEntry> entryList = new ArrayList<>();
        List<String> xValues = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Double> entry : rideInfo.getLastYearTimes().entrySet()){
            BarEntry e = new BarEntry((float) entry.getValue().doubleValue(), i);
            entryList.add(e);
            xValues.add(entry.getKey());
            i++;
        }


        BarDataSet dataSet = new BarDataSet(entryList, "Wait time in minutes");

        BarData data = new BarData(xValues, dataSet);

        chart.setData(data);
        chart.invalidate();

    }
}
