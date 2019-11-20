package com.example.finalyearproject;

import java.util.Date;

public class ScoutEvent {
    int eventId;
    String location;
    String startDate;
    String endDate;
    String eventType;

    public ScoutEvent(int eventId, String location, String startDate, String endDate, String eventType) {
        this.eventId = eventId;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventType = eventType;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
