package com.queueTimes.Queue_Times.network;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AsyncParkJsonParser extends AsyncTask<Context, String, List<Park>> {

    Context c;
    View rootView;

    String PARK_ENDPOINT = "https://queue-times.com/parks.json";

    public AsyncParkJsonParser(Context c, View rootView){
        this.c = c;
        this.rootView = rootView;
    }


    private ProgressDialog progressDialog = new ProgressDialog(c);
    InputStream inputStream = null;
    String result = "";

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Downloading your data...");
        progressDialog.show();
        progressDialog.setOnCancelListener(arg0 -> AsyncParkJsonParser.this.cancel(true));
    }

    @Override
    protected List<Park> doInBackground(Context... params) {

        ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        try {
            // Set up HTTP post

            // HttpClient is more then less deprecated. Need to change to URLConnection
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(PARK_ENDPOINT);
            httpPost.setEntity(new UrlEncodedFormEntity(param));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

            // Read content & Log
            inputStream = httpEntity.getContent();
        } catch (UnsupportedEncodingException e1) {
            Log.e("UnsupportedEncodingE", e1.toString());
            e1.printStackTrace();
        } catch (ClientProtocolException e2) {
            Log.e("ClientProtocolException", e2.toString());
            e2.printStackTrace();
        } catch (IllegalStateException e3) {
            Log.e("IllegalStateException", e3.toString());
            e3.printStackTrace();
        } catch (IOException e4) {
            Log.e("IOException", e4.toString());
            e4.printStackTrace();
        }
        // Convert response to string using String Builder
        try {
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder sBuilder = new StringBuilder();

            String line = null;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }

            inputStream.close();
            result = sBuilder.toString();

        } catch (Exception e) {
            Log.e("JSON", "Error converting result " + e.toString());
        }
        //parse JSON data
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

    @Override
    protected void onPostExecute(List<Park> parks){
        this.progressDialog.dismiss();
        // Send list to UI thread for display

        final ExpandableLayoutListView expandableLayoutListView = (ExpandableLayoutListView) rootView.findViewById(R.id.listview);

        ParkAdapter adapter = new ParkAdapter(c, (ArrayList<Park>) parks);
        // Attach the adapter to a ListView
        expandableLayoutListView.setAdapter(adapter);
    }
}
