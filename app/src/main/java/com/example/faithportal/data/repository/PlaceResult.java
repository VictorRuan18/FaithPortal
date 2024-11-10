package com.example.faithportal.data.repository;

public class PlaceResult {
    private Geometry geometry;
    private String name;
    private String address; // Add this field
    private double distance; // Add this field

    public Geometry getGeometry() {
        return geometry;
    }
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}