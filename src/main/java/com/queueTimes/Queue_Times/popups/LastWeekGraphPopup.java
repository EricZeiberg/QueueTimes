package com.queueTimes.Queue_Times.popups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.*;
import com.google.gson.Gson;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.data.DataObject;
import com.queueTimes.Queue_Times.models.Queue;
import com.queueTimes.Queue_Times.models.Ride;
import com.queueTimes.Queue_Times.models.RideInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LastWeekGraphPopup extends Activity{

    LineChart chart;
    RideInfo rideInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_week_graph);


        rideInfo = (RideInfo) DataObject.getObject();

        chart = (LineChart) findViewById(R.id.last_week_chart);

        chart.setDescription("Last Week Wait Times");

        List<Entry> entryList = new ArrayList<>();
        List<String> xValues = new ArrayList<>();
        for (int i = 0; i < rideInfo.getLastWeek().size(); i++){
           Entry e = new Entry(rideInfo.getLastWeek().get(i).getWaitTime(), i);
            entryList.add(e);
            Date d = new Date(rideInfo.getLastWeek().get(i).getUnixTime() * 1000);
            xValues.add(new SimpleDateFormat("EE").format(d));
        }


       LineDataSet dataSet = new LineDataSet(entryList, "Wait time in minutes");
        dataSet.setCircleColor(getResources().getColor(R.color.red));

        LineData data = new LineData(xValues, dataSet);

        chart.setData(data);
        chart.invalidate();


    }
}
