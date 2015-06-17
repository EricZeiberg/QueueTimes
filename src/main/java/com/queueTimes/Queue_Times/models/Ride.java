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

    HashMap<String, Double> lastWeekTimes = new HashMap<String, Double>();
    HashMap<String, Double> lastYearTimes = new HashMap<String, Double>();

    Queue longestQueue;
    Queue latestQueue;
    List<Queue> lastWeek = new ArrayList<Queue>();
    SparseArray<Double> averageDay = new SparseArray<Double>();

    public Ride(Park park, String name, int ID, String description, String uri, HashMap<String, Double> lastWeekTimes, HashMap<String, Double> lastYearTimes, Queue longestQueue, Queue latestQueue, List<Queue> lastWeek, SparseArray<Double> averageDay) {
        this.park = park;
        this.name = name;
        this.ID = ID;
        this.description = description;
        this.uri = uri;
        this.lastWeekTimes = lastWeekTimes;
        this.lastYearTimes = lastYearTimes;
        this.longestQueue = longestQueue;
        this.latestQueue = latestQueue;
        this.lastWeek = lastWeek;
        this.averageDay = averageDay;
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

    public HashMap<String, Double> getLastWeekTimes() {
        return lastWeekTimes;
    }

    public HashMap<String, Double> getLastYearTimes() {
        return lastYearTimes;
    }

    public Queue getLongestQueue() {
        return longestQueue;
    }

    public Queue getLatestQueue() {
        return latestQueue;
    }

    public List<Queue> getLastWeek() {
        return lastWeek;
    }

    public SparseArray<Double> getAverageDay() {
        return averageDay;
    }
}
