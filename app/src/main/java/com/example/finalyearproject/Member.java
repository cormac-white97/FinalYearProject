package com.example.finalyearproject;


public class Member {
    String id;
    String name;
    String email;
    String pass;
    String group;
    String memDob;
    String memDom;
    String notes;
    String fcmToken;

    public Member() {

    }

    public Member(String id, String name, String email, String group, String memDob, String memDom, String notes, String fcmToken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.group = group;
        this.memDob = memDob;
        this.memDom = memDom;
        this.notes = notes;
        this.fcmToken = fcmToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMemDob() {
        return memDob;
    }

    public void setMemDob(String memDob) {
        this.memDob = memDob;
    }

    public String getMemDom() {
        return memDom;
    }

    public void setMemDom(String memDom) {
        this.memDom = memDom;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
