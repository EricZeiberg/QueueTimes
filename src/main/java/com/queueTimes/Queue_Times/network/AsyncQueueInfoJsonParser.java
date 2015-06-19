package com.queueTimes.Queue_Times.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.adapters.RideListAdapter;
import com.queueTimes.Queue_Times.models.Park;
import com.queueTimes.Queue_Times.models.Queue;
import com.queueTimes.Queue_Times.models.Ride;
import com.queueTimes.Queue_Times.models.RideInfo;
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
                int waitTime = jObject.getInt("wait");
                boolean operational = jObject.getBoolean("operational");
                Queue q = new Queue(ride, unixTime, waitTime, operational, null);
                lastWeekArray.add(q);
            }
            // Latest Queue
            JSONObject latest = object.getJSONObject("latest_queue");
            long unixTime = latest.getLong("when");
            int waitTime = latest.getInt("wait");
            boolean operational = latest.getBoolean("operational");
            String vague = latest.getString("vague");
            latestQueue = new Queue(ride, unixTime, waitTime, operational, vague);

            //Longest Queue
            JSONObject longest = object.getJSONObject("longest_queue");
            long unixTime_longest = longest.getLong("when");
            int waitTime_longest = longest.getInt("wait");
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
            averageWeekMap.put("Sunday", averageWeek.getDouble("Sunday"));
            averageWeekMap.put("Monday", averageWeek.getDouble("Monday"));
            averageWeekMap.put("Tuesday", averageWeek.getDouble("Tuesday"));
            averageWeekMap.put("Wednesday", averageWeek.getDouble("Wednesday"));
            averageWeekMap.put("Thursday", averageWeek.getDouble("Thursday"));
            averageWeekMap.put("Friday", averageWeek.getDouble("Friday"));
            averageWeekMap.put("Saturday", averageWeek.getDouble("Saturday"));

            // Average Year
            JSONObject averageYear = object.getJSONObject("average_year");
            averageYear.put("January", averageYear.getDouble("January"));
            averageYear.put("February", averageYear.getDouble("February"));
            averageYear.put("March", averageYear.getDouble("March"));
            averageYear.put("April", averageYear.getDouble("April"));
            averageYear.put("May", averageYear.getDouble("May"));
            averageYear.put("June", averageYear.getDouble("June"));
            averageYear.put("July", averageYear.getDouble("July"));
            averageYear.put("August", averageYear.getDouble("August"));
            averageYear.put("September", averageYear.getDouble("September"));
            averageYear.put("October", averageYear.getDouble("October"));
            averageYear.put("November", averageYear.getDouble("November"));
            averageYear.put("December", averageYear.getDouble("December"));

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


        this.progressDialog.dismiss();

    }
}
