package com.queueTimes.Queue_Times.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.network.AsyncParkJsonParser;

public class ThemeParkList extends Activity {

    AsyncParkJsonParser parser;

    ListView listView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listView = (ListView) findViewById(R.id.list_view);
    }


    @Override
    protected void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();

        parser = new AsyncParkJsonParser(this, getApplicationContext(), findViewById(R.id.main_view));
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
            parser = new AsyncParkJsonParser(this, getApplicationContext(), findViewById(R.id.main_view));
            parser.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
