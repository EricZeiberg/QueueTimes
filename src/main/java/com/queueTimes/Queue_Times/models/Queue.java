package com.queueTimes.Queue_Times.models;

public class Queue {
    Ride ride;

    long unixTime;
    int waitTime;
    boolean operational;
    String vagueTime;

    public Queue(Ride ride, long unixTime, int waitTime, boolean operational, String vagueTime) {
        this.ride = ride;
        this.unixTime = unixTime;
        this.waitTime = waitTime;
        this.operational = operational;
        this.vagueTime = vagueTime;
    }

    public Ride getRide() {
        return ride;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public boolean isOperational() {
        return operational;
    }

    public String getVagueTime() {
        return vagueTime;
    }
}
