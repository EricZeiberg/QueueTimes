package com.queueTimes.Queue_Times.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.models.Park;
import com.queueTimes.Queue_Times.models.Ride;
import com.queueTimes.Queue_Times.network.AsyncRideListJsonParser;

import java.util.ArrayList;
import java.util.List;

public class RideList extends Activity{

    List<Ride> rides = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_view);

        Intent intent = getIntent();
        String parkToString = intent.getExtras().getString("park");
        Park p = Park.fromString(parkToString);

        AsyncRideListJsonParser parser = new AsyncRideListJsonParser(this, getApplicationContext(), findViewById(R.id.ride_view), p);
        parser.execute();
    }
}
