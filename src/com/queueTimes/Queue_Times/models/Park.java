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
}
