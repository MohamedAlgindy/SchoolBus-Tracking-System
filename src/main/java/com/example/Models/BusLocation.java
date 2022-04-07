package com.example.Models;

public class BusLocation {
    String userId;
    double busLatitude;
    double busLongitude;

    public BusLocation(){
        super();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getBusLatitude() {
        return busLatitude;
    }

    public void setBusLatitude(double busLatitude) {
        this.busLatitude = busLatitude;
    }

    public double getBusLongitude() {
        return busLongitude;
    }

    public void setBusLongitude(double busLongitude) {
        this.busLongitude = busLongitude;
    }

    public BusLocation(String userId, double busLatitude, double busLongitude) {
        this.userId = userId;
        this.busLatitude = busLatitude;
        this.busLongitude = busLongitude;
    }
}
