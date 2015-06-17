package com.queueTimes.Queue_Times.models;

import java.util.ArrayList;
import java.util.List;

public class Park {

    String name;
    String description;
    String uri;
    String country;
    int ID;

    List<Ride> rides = new ArrayList<Ride>();

    public Park(String name, String description, String uri, String country, int ID) {
        this.name = name;
        this.description = description;
        this.uri = uri;
        this.country = country;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUri() {
        return uri;
    }

    public String getCountry() {
        return country;
    }

    public int getID() {
        return ID;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }
}
