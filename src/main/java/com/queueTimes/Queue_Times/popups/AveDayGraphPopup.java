package com.queueTimes.Queue_Times.popups;

import android.app.Activity;
import android.os.Bundle;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.data.DataObject;
import com.queueTimes.Queue_Times.models.RideInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AveDayGraphPopup extends Activity{

    LineChart chart;
    RideInfo rideInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ave_day_graph);

        rideInfo = (RideInfo) DataObject.getObject();

        chart = (LineChart) findViewById(R.id.ave_day_chart);

        chart.setDescription("Average Daily Wait Time");

        List<Entry> entryList = new ArrayList<>();
        List<String> xValues = new ArrayList<>();
        for (int i = 0; i < rideInfo.getAverageDay().size(); i++){
            Entry e = new Entry(rideInfo.getAverageDay().valueAt(i).floatValue(), i);
            entryList.add(e);
            xValues.add(String.valueOf(rideInfo.getAverageDay().keyAt(i)) + ":00");
        }


        LineDataSet dataSet = new LineDataSet(entryList, "Wait time in minutes");

        dataSet.setCircleColor(getResources().getColor(R.color.red));
        LineData data = new LineData(xValues, dataSet);

        chart.setData(data);
        chart.invalidate();

    }
}
