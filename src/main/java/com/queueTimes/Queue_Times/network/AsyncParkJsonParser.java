package com.queueTimes.Queue_Times.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.activities.ThemeParkList;
import com.queueTimes.Queue_Times.adapters.ParkAdapter;
import com.queueTimes.Queue_Times.models.Park;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AsyncParkJsonParser extends AsyncTask<Context, String, List<Park>> {

    Activity c;
    View rootView;

    String PARK_ENDPOINT = "http://queue-times.com/parks.json";
    private ProgressDialog progressDialog;

    public AsyncParkJsonParser(Activity c, View rootView){
        this.c = c;
        this.rootView = rootView;
        progressDialog = new ProgressDialog(c);
    }



    InputStream inputStream = null;

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Downloading your data...");
        progressDialog.show();
    }

    @Override
    protected List<Park> doInBackground(Context... params) {
        String result = "";
        HttpResponse response;
        HttpClient myClient = new DefaultHttpClient();
        HttpPost myConnection = new HttpPost(PARK_ENDPOINT);

        try {
            response = myClient.execute(myConnection);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Park> parks = new ArrayList<>();
        Log.e("[INFO]", result + "\n");
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

    @Override
    protected void onPostExecute(List<Park> parks){
        // Send list to UI thread for display

        for (Park p : parks){
            //Log.e("[INFO]", p.getName());
        }
        final ExpandableLayoutListView expandableLayoutListView = (ExpandableLayoutListView) rootView.findViewById(R.id.listview);

        ParkAdapter adapter = new ParkAdapter(c, (ArrayList<Park>) parks);
        // Attach the adapter to a ListView
        expandableLayoutListView.setAdapter(adapter);

        this.progressDialog.dismiss();
    }
}
