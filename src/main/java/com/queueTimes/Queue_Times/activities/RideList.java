package com.queueTimes.Queue_Times.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.models.Park;
import com.queueTimes.Queue_Times.models.Ride;
import com.queueTimes.Queue_Times.network.AsyncParkJsonParser;
import com.queueTimes.Queue_Times.network.AsyncRideListJsonParser;

import java.util.ArrayList;
import java.util.List;

public class RideList extends Activity{

    List<Ride> rides = new ArrayList<>();
    TextView titleText;

    Park p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_view);

        titleText = (TextView) findViewById(R.id.ride_list_textview);

        Intent intent = getIntent();
        String parkToString = intent.getExtras().getString("park");
         p = Park.fromString(parkToString);

        titleText.setText("Ride List for " + p.getName());

        AsyncRideListJsonParser parser = new AsyncRideListJsonParser(this, getApplicationContext(), findViewById(R.id.ride_view), p);
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
            AsyncRideListJsonParser parser = new AsyncRideListJsonParser(this, getApplicationContext(), findViewById(R.id.ride_view), p);
            parser.execute();
            return true;
        }
        else if (id == R.id.link_to_website){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://queue-times.com"));
            startActivity(browserIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
