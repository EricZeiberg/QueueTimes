package com.queueTimes.Queue_Times.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.Gson;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.activities.RideList;
import com.queueTimes.Queue_Times.activities.RideView;
import com.queueTimes.Queue_Times.activities.ThemeParkList;
import com.queueTimes.Queue_Times.adapters.ParkAdapter;
import com.queueTimes.Queue_Times.adapters.RideListAdapter;
import com.queueTimes.Queue_Times.models.Park;
import com.queueTimes.Queue_Times.models.Ride;
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

public class AsyncRideListJsonParser extends AsyncTask<Context, String, List<Ride>> {

    Activity a;
    Context c;
    View rootView;
    Park park;

    String SPECIFIC_PARK_ENDPOINT = "http://api.queue-times.com/park/";
    private ProgressDialog progressDialog;

    public AsyncRideListJsonParser(Activity a, Context c , View rootView, Park park){
        this.c = c;
        this.a = a;
        this.rootView = rootView;
        progressDialog = new ProgressDialog(a);
        this.park = park;
    }

    InputStream inputStream = null;

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Hold tight! Fetching data....");
        progressDialog.show();
    }

    @Override
    protected List<Ride> doInBackground(Context... params) {
        String result = getJSON(SPECIFIC_PARK_ENDPOINT + park.getUri() + ".json");
        List<Ride> rides = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(result);
            JSONArray jArray = object.getJSONArray("rides");
            for(int i=0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                int id = jObject.getInt("id");
                String name = jObject.getString("name");
                String description = jObject.getString("description");
                String uri = jObject.getString("uri");
                Ride r = new Ride(park, name, id, description, uri, null);
                rides.add(r);
            } // End Loop

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
                Intent i = new Intent(c, RideView.class);
                Gson gson = new Gson();
                i.putExtra("ride", gson.toJson(r));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            }
        });
    }
}
