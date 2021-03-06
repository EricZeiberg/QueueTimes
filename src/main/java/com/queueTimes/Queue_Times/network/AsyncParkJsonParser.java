package com.queueTimes.Queue_Times.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.activities.RideList;
import com.queueTimes.Queue_Times.adapters.ParkAdapter;
import com.queueTimes.Queue_Times.models.Park;
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

public class AsyncParkJsonParser extends AsyncTask<Context, String, List<Park>> {

    Activity a;
    Context c;
    View rootView;

    String PARK_ENDPOINT = "http://api.queue-times.com/parks.json";
    private ProgressDialog progressDialog;

    public AsyncParkJsonParser(Activity a, Context c , View rootView){
        this.c = c;
        this.a = a;
        this.rootView = rootView;
        progressDialog = new ProgressDialog(a);
    }



    InputStream inputStream = null;

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Hold tight! Fetching data....");
        progressDialog.show();
    }

    @Override
    protected List<Park> doInBackground(Context... params) {
        String result = getJSON(PARK_ENDPOINT);
        List<Park> parks = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(result);
            for(int i=0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                String name = jObject.getString("name");
                String description = jObject.getString("description");
                String country = jObject.getString("country");
                String uri = jObject.getString("uri");
                int ID = jObject.getInt("id");
                Park newPark = new Park(name, description, uri, country, ID);
                parks.add(newPark);
            } // End Loop

        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        } // catch (JSONException e)
        return parks;
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
    protected void onPostExecute(List<Park> parks){
        // Send list to UI thread for display

        final ListView listView = (ListView) rootView.findViewById(R.id.list_view);

        this.progressDialog.dismiss();
        ParkAdapter adapter = new ParkAdapter(rootView.getContext(), parks);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Park p = parks.get(position);
                Intent i = new Intent(c, RideList.class);
                i.putExtra("park", p.toString());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            }
        });
    }
}
