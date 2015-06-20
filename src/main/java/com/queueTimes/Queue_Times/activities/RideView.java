package com.queueTimes.Queue_Times.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.gson.Gson;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.models.Park;
import com.queueTimes.Queue_Times.models.Ride;
import com.queueTimes.Queue_Times.network.AsyncQueueInfoJsonParser;
import com.queueTimes.Queue_Times.network.AsyncRideListJsonParser;
import com.queueTimes.Queue_Times.popups.MessageDialog;

public class RideView extends Activity{


    // Current queue times, longest queue time, graphs: last week, average for days of week, average for months of the year

    Ride r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_details_view);

        Intent intent = getIntent();
        String parkToString = intent.getExtras().getString("ride");
        Gson gson = new Gson();
        r = gson.fromJson(parkToString, Ride.class);

        AsyncQueueInfoJsonParser parser = new AsyncQueueInfoJsonParser(this, getApplicationContext(), findViewById(R.id.ride_view), r);
        parser.execute();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.refresh_setting) {
            AsyncQueueInfoJsonParser parser = new AsyncQueueInfoJsonParser(this, getApplicationContext(), findViewById(R.id.ride_view), r);
            parser.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
