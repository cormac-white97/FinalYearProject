package com.example.finalyearproject.Objects;


public class Leader {
    private String leaderId;
    private String name;
    private String DOB;
    private String group;
    private String Phone;
    private String email;
    private String vettingDate;

    public Leader() {
    }

    public Leader(String leaderId, String name, String DOB, String group, String phone, String email, String vettingDate) {
        this.leaderId = leaderId;
        this.name = name;
        this.DOB = DOB;
        this.group = group;
        Phone = phone;
        this.email = email;
        this.vettingDate = vettingDate;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVettingDate() {
        return vettingDate;
    }

    public void setVettingDate(String vettingDate) {
        this.vettingDate = vettingDate;
    }
}
