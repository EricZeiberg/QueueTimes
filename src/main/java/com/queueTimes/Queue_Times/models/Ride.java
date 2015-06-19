package com.queueTimes.Queue_Times.models;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ride {

    Park park;

    String name;
    int ID;
    String description;
    String uri;

    RideInfo rideInfo;

    public Ride(Park park, String name, int ID, String description, String uri, RideInfo rideInfo) {
        this.park = park;
        this.name = name;
        this.ID = ID;
        this.description = description;
        this.uri = uri;
        this.rideInfo = rideInfo;
    }

    public Park getPark() {
        return park;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public String getDescription() {
        return description;
    }

    public String getUri() {
        return uri;
    }

    public RideInfo getRideInfo() {
        return rideInfo;
    }

    public void setRideInfo(RideInfo rideInfo) {
        this.rideInfo = rideInfo;
    }
}
