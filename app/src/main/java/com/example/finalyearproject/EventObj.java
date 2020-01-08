package com.example.finalyearproject;

public class EventObj {
    String type;
    String location;
    String date;
    String group;

    public EventObj(String type, String location, String date, String group) {
        this.type = type;
        this.location = location;
        this.date = date;
        this.group = group;
    }

    public EventObj() {
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
