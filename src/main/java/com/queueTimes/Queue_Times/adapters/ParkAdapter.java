package com.queueTimes.Queue_Times.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.andexert.expandablelayout.library.ExpandableLayoutItem;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.models.Park;

import java.util.ArrayList;
import java.util.List;

public class ParkAdapter extends ArrayAdapter<Park> {
    public ParkAdapter(Context context, List<Park> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Park park = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.main, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.text);
        TextView header = (TextView) convertView.findViewById(R.id.header_text);
        Button b = (Button) convertView.findViewById(R.id.button);
        // Populate the data into the template view using the data object
       name.setText(park.getName());
       name.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        header.setText(park.getName());
        header.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        b.setVisibility(View.VISIBLE);
        //description.setText(park.getDescription());
        // Return the completed view to render on screen
        return convertView;
    }
}
