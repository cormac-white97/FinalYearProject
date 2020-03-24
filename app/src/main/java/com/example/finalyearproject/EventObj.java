package com.example.finalyearproject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class EventObj {
    String id;
    String type;
    String location;
    String date;
    String endDate;
    String group;
    double price;
    String createdBy;
    HashMap<String, String> eventLeaders;
    ArrayList<String> paymentList;
    int availableSpaces;
    double lng;
    double lat;
    String approved;

    public EventObj(String id, String type, String location, String date, String endDate,  String group, double price, String createdBy, HashMap<String, String> eventLeaders, ArrayList<String> paymentList, int availableSpaces,double lng, double lat, String approved) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.date = date;
        this.endDate = endDate;
        this.group = group;
        this.price = price;
        this.createdBy = createdBy;
        this.eventLeaders = eventLeaders;
        this.paymentList = paymentList;
        this.availableSpaces = availableSpaces;
        this.lng = lng;
        this.lat = lat;
        this.approved = approved;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public HashMap<String, String> getEventLeaders() {
        return eventLeaders;
    }

    public void setEventLeaders(HashMap<String, String> eventLeaders) {
        this.eventLeaders = eventLeaders;
    }

    public ArrayList<String> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(ArrayList<String> paymentList) {
        this.paymentList = paymentList;
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

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }
}
