package com.queueTimes.Queue_Times.models;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RideInfo {
    Ride ride;

    HashMap<String, Double> lastWeekTimes = new HashMap<String, Double>();
    HashMap<String, Double> lastYearTimes = new HashMap<String, Double>();

    Queue longestQueue;
    Queue latestQueue;
    List<Queue> lastWeek = new ArrayList<Queue>();
    SparseArray<Double> averageDay = new SparseArray<Double>();

    public RideInfo(Ride ride) {
        this.ride = ride;
    }

    public HashMap<String, Double> getLastWeekTimes() {
        return lastWeekTimes;
    }

    public void setLastWeekTimes(HashMap<String, Double> lastWeekTimes) {
        this.lastWeekTimes = lastWeekTimes;
    }

    public HashMap<String, Double> getLastYearTimes() {
        return lastYearTimes;
    }

    public void setLastYearTimes(HashMap<String, Double> lastYearTimes) {
        this.lastYearTimes = lastYearTimes;
    }

    public Queue getLongestQueue() {
        return longestQueue;
    }

    public void setLongestQueue(Queue longestQueue) {
        this.longestQueue = longestQueue;
    }

    public Queue getLatestQueue() {
        return latestQueue;
    }

    public void setLatestQueue(Queue latestQueue) {
        this.latestQueue = latestQueue;
    }

    public List<Queue> getLastWeek() {
        return lastWeek;
    }

    public void setLastWeek(List<Queue> lastWeek) {
        this.lastWeek = lastWeek;
    }

    public SparseArray<Double> getAverageDay() {
        return averageDay;
    }

    public void setAverageDay(SparseArray<Double> averageDay) {
        this.averageDay = averageDay;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }
}
