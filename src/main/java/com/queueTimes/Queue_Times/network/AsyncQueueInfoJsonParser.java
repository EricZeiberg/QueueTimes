package com.queueTimes.Queue_Times.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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



        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        } // catch (JSONException e)
        return rides;
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
    protected void onPostExecute(List<Ride> rides){
        // Send list to UI thread for display


        final ListView listView = (ListView) rootView.findViewById(R.id.ride_list_view);

        this.progressDialog.dismiss();
        RideListAdapter adapter = new RideListAdapter(rootView.getContext(), rides);
        listView.setAdapter(adapter);

        this.progressDialog.dismiss();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ride r = rides.get(position);

            }
        });
    }
}
