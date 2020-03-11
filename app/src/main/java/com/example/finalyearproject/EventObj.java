package com.example.finalyearproject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class EventObj {
    String id;
    String type;
    String location;
    String date;
    String endDate;
    String group;
    String createdBy;
    LinkedHashMap<String, String> eventLeaders;
    int availableSpaces;
    double lng;
    double lat;

    public EventObj(String id, String type, String location, String date, String endDate,  String group, String createdBy, LinkedHashMap<String, String> eventLeaders, int availableSpaces,double lng, double lat) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.date = date;
        this.endDate = endDate;
        this.group = group;
        this.createdBy = createdBy;
        this.eventLeaders = eventLeaders;
        this.availableSpaces = availableSpaces;
        this.lng = lng;
        this.lat = lat;

    }

    public EventObj() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LinkedHashMap<String, String> getEventLeaders() {
        return eventLeaders;
    }

    public void setEventLeaders(LinkedHashMap<String, String> eventLeaders) {
        this.eventLeaders = eventLeaders;
    }

    public int getAvailableSpaces() {
        return availableSpaces;
    }

    public void setAvailableSpaces(int availableSpaces) {
        this.availableSpaces = availableSpaces;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
