package com.queueTimes.Queue_Times.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.data.DataObject;
import com.queueTimes.Queue_Times.models.Queue;
import com.queueTimes.Queue_Times.models.Ride;
import com.queueTimes.Queue_Times.models.RideInfo;
import com.queueTimes.Queue_Times.popups.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AsyncQueueInfoJsonParser  extends AsyncTask<Context, String, RideInfo> {
    Activity a;
    Context c;
    View rootView;
    Ride ride;

    String SPECIFIC_PARK_ENDPOINT = "http://api.queue-times.com/park/";
    private ProgressDialog progressDialog;

    public AsyncQueueInfoJsonParser(Activity a, Context c , View rootView, Ride ride){
        this.c = c;
        this.a = a;
        this.rootView = rootView;
        progressDialog = new ProgressDialog(a);
        this.ride = ride;
    }

    InputStream inputStream = null;

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Hold tight! Fetching data....");
        progressDialog.show();
    }

    @Override
    protected RideInfo doInBackground(Context... params) {
        String result = getJSON(SPECIFIC_PARK_ENDPOINT + ride.getPark().getUri() + "/" + ride.getUri() + ".json");
        List<Queue> lastWeekArray = new ArrayList<>();
        Queue latestQueue;
        Queue longestQueue;
        SparseArray<Double> averageDayArray = new SparseArray<>();
        HashMap<String, Double> averageWeekMap = new HashMap<>();
        HashMap<String, Double> averageYearMap = new HashMap<>();

        RideInfo rideInfo = null;
        try {
            JSONObject object = new JSONObject(result);
            JSONArray lastWeek = object.getJSONArray("last_week");
            for(int i=0; i < lastWeek.length(); i++) {
                JSONObject jObject = lastWeek.getJSONObject(i);
                long unixTime = jObject.getLong("when");
                int waitTime = jObject.getInt("wait_time");
                boolean operational = jObject.getBoolean("operational");
                Queue q = new Queue(ride, unixTime, waitTime, operational, null);
                lastWeekArray.add(q);
            }
            // Latest Queue
            JSONObject latest = object.getJSONObject("latest_queue");
            long unixTime = latest.getLong("when");
            int waitTime = latest.getInt("wait_time");
            boolean operational = latest.getBoolean("operational");
            String vague = latest.getString("vague");
            latestQueue = new Queue(ride, unixTime, waitTime, operational, vague);

            //Longest Queue
            JSONObject longest = object.getJSONObject("longest_queue");
            long unixTime_longest = longest.getLong("when");
            int waitTime_longest = longest.getInt("wait_time");
            boolean operational_longest = longest.getBoolean("operational");
            String vague_longest = latest.getString("vague");
            longestQueue = new Queue(ride, unixTime_longest, waitTime_longest, operational_longest, vague_longest);

            // Average Day
            JSONArray averageDay = object.getJSONArray("average_day");
            for(int i=0; i < averageDay.length(); i++) {
                JSONObject obj = averageDay.getJSONObject(i);
                int hour = obj.getInt("hour");
                double average = obj.getDouble("average");
                averageDayArray.append(hour, average);
            }

            // Average Week
            JSONObject averageWeek = object.getJSONObject("average_week");
            Iterator<String> iter = averageWeek.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    double value = averageWeek.getDouble(key);
                    averageWeekMap.put(key, value);
                } catch (JSONException e) {
                    // Something went wrong!
                }
            }

            // Average Year
            JSONObject averageYear = object.getJSONObject("average_year");
            Iterator<String> iter1 = averageYear.keys();
            while (iter1.hasNext()) {
                String key = iter1.next();
                try {
                    double value = averageYear.getDouble(key);
                    averageYearMap.put(key, value);
                } catch (JSONException e) {
                    // Something went wrong!
                }
            }

            rideInfo = new RideInfo(ride);
            rideInfo.setAverageDay(averageDayArray);
            rideInfo.setLastWeek(lastWeekArray);
            rideInfo.setLastWeekTimes(averageWeekMap);
            rideInfo.setLastYearTimes(averageYearMap);
            rideInfo.setLatestQueue(latestQueue);
            rideInfo.setLongestQueue(longestQueue);

            ride.setRideInfo(rideInfo);

        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());

        } // catch (JSONException e)
        return rideInfo;
    }

    public String getJSON(String address){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(address);
        try{
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }
            } else {
                Log.e("ERROR","Failed to get JSON object");
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return builder.toString();
    }

    @Override
    protected void onPostExecute(RideInfo rideInfo){
        // Send list to UI thread for display

        Button currentQueueTime;
        Button longestQueueTime;
        Button lastWeek;
        Button aveLastWeek;
        Button aveLastMonth;
        Button aveYear;

        Button aveDaily;

        Button openStatus;

        currentQueueTime = (Button) a.findViewById(R.id.current_wait_time_button);
        currentQueueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog dialog = new MessageDialog("The current wait time is " + rideInfo.getLatestQueue().getWaitTime() + " minutes.");
                dialog.show(a.getFragmentManager(), "Current Wait Time");
            }
        });
        longestQueueTime = (Button) a.findViewById(R.id.longest_wait_time_button);
        longestQueueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog dialog = new MessageDialog("The longest queue time EVER was " + rideInfo.getLongestQueue().getWaitTime() + " minutes.");
                dialog.show(a.getFragmentManager(), "Longest Wait Time");
            }
        });
        aveDaily = (Button) a.findViewById(R.id.average_daily_wait_time_button);
        aveDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c, AveDayGraphPopup.class);
                DataObject.setObject(rideInfo);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            }
        });
        lastWeek = (Button) a.findViewById(R.id.last_week_wait_time_button);
        lastWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c, LastWeekGraphPopup.class);
                DataObject.setObject(rideInfo);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            }
        });
        aveLastWeek = (Button) a.findViewById(R.id.average_wait_time_last_week_button);
        aveLastWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c, AveWeekGraphPopup.class);
                DataObject.setObject(rideInfo);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            }
        });
        aveLastMonth = (Button) a.findViewById(R.id.average_wait_time_last_month_button);
        aveLastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog dialog = new MessageDialog("This feature is coming soon!");
                dialog.show(a.getFragmentManager(), "Message");
            }
        });
        aveYear = (Button) a.findViewById(R.id.average_wait_time_last_year_button);
        aveYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c, AveYearGraphPopup.class);
                DataObject.setObject(rideInfo);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            }
        });
        openStatus = (Button) a.findViewById(R.id.open_status_button);

        if (!rideInfo.getLatestQueue().isOperational()){
            openStatus.setBackgroundResource(R.drawable.red_gradient);
            openStatus.setText("This ride is currently closed!");
        }

        this.progressDialog.dismiss();

    }
}
