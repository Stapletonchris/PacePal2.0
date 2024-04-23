package com.ssd.gps_30;


public class RunHistoryItem {
    //private String date;
    private double distance; // In kilometers
    private String time;

    // Constructor
    public RunHistoryItem(String date, double distance, String time) {
        //this.date = date;
        this.distance = distance;
        this.time = time;
    }

    // Getters
    //public String getDate() { return date; }
    public double getDistance() { return distance; }
    public String getTime() { return time; }
}
