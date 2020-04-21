package com.example.finalyearproject.Objects;


public class Member {
    String memberId;
    String name;
    String group;
    String memDob;
    String memDom;
    String notes;

    public Member() {

    }

    public Member(String memberId, String name, String group, String memDob, String memDom, String notes) {
        this.memberId = memberId;
        this.name = name;
        this.group = group;
        this.memDob = memDob;
        this.memDom = memDom;
        this.notes = notes;
    }

    public String getId() {
        return memberId;
    }

    public void setId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
