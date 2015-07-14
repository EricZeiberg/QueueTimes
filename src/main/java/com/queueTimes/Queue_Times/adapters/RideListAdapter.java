package com.queueTimes.Queue_Times.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.queueTimes.Queue_Times.R;
import com.queueTimes.Queue_Times.models.Park;
import com.queueTimes.Queue_Times.models.Ride;

import java.util.List;

public class RideListAdapter extends ArrayAdapter<Ride> {

    public RideListAdapter(Context context, List<Ride> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ride ride = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.title);
       TextView header = (TextView) convertView.findViewById(R.id.lower_text);
        name.setText(ride.getName());
       name.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        if (ride.getRideInfo().getLatestQueue().isOperational()){
            header.setText("Current wait time: " + ride.getRideInfo().getLatestQueue().getWaitTime());
        }
        else {
            header.setText("This ride is currently closed");
        }

        return convertView;
    }
}
