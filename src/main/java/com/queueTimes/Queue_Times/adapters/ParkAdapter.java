package com.queueTimes.Queue_Times.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.models.Park;

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.title);
        TextView header = (TextView) convertView.findViewById(R.id.lower_text);
        // Populate the data into the template view using the data object
       name.setText(park.getName());
       name.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        header.setText(park.getCountry());
        //header.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        //description.setText(park.getDescription());
        // Return the completed view to render on screen
        return convertView;
    }


}
