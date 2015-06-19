package com.queueTimes.Queue_Times.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.gson.Gson;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.models.Park;
import com.queueTimes.Queue_Times.models.Ride;
import com.queueTimes.Queue_Times.network.AsyncQueueInfoJsonParser;
import com.queueTimes.Queue_Times.network.AsyncRideListJsonParser;

public class RideView extends Activity{


    // Current queue times, longest queue time, graphs: last week, average for days of week, average for months of the year

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_details_view);

        Intent intent = getIntent();
        String parkToString = intent.getExtras().getString("ride");
        Gson gson = new Gson();
        Ride r = gson.fromJson(parkToString, Ride.class);

        AsyncQueueInfoJsonParser parser = new AsyncQueueInfoJsonParser(this, getApplicationContext(), findViewById(R.id.ride_view), r);
        parser.execute();
    }
}
