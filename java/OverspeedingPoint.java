package com.example.ergasia2;

public class OverspeedingPoint {


    private int overspeedingPoint_id;
    private String overspeedingPoint_latitude;
    private String overspeedingPoint_longitude;
    private String overspeedingPoint_speed;
    private String overspeedingPoint_timestamp;

    public OverspeedingPoint(int overspeedingPoint_id, String overspeedingPoint_latitude, String overspeedingPoint_longitude, String overspeedingPoint_speed, String overspeedingPoint_timestamp) {
        this.overspeedingPoint_id = overspeedingPoint_id;
        this.overspeedingPoint_latitude = overspeedingPoint_latitude;
        this.overspeedingPoint_longitude = overspeedingPoint_longitude;
        this.overspeedingPoint_speed = overspeedingPoint_speed;
        this.overspeedingPoint_timestamp = overspeedingPoint_timestamp;
    }

    public OverspeedingPoint(String overspeedingPoint_latitude, String overspeedingPoint_longitude,
                             String overspeedingPoint_speed, String overspeedingPoint_timestamp) {
        this.overspeedingPoint_latitude = overspeedingPoint_latitude;
        this.overspeedingPoint_longitude = overspeedingPoint_longitude;
        this.overspeedingPoint_speed = overspeedingPoint_speed;
        this.overspeedingPoint_speed = overspeedingPoint_speed;
        this.overspeedingPoint_timestamp = overspeedingPoint_timestamp;
    }

    public int getOverspeedingPoint_id() {
        return overspeedingPoint_id;
    }

    public String getoverspeedingPoint_latitude() {
        return overspeedingPoint_latitude;
    }

    public String getoverspeedingPoint_longitude() {
        return overspeedingPoint_longitude;
    }

    public String getOverspeedingPoint_speed() {
        return overspeedingPoint_speed;
    }

    public String getOverspeedingPoint_timestamp() {
        return overspeedingPoint_timestamp;
    }
}
