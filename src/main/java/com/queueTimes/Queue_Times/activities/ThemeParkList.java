package com.queueTimes.Queue_Times.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.models.Park;
import com.queueTimes.Queue_Times.network.AsyncParkJsonParser;

import java.util.ArrayList;

public class ThemeParkList extends Activity {

    SwipeRefreshLayout swipeLayout;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }


    @Override
    protected void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
        AsyncParkJsonParser parser = new AsyncParkJsonParser(this, getApplicationContext(), findViewById(R.id.main_view));
        parser.execute();

    }




}
