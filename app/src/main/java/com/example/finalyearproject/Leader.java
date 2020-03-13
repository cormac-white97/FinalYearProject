package com.example.finalyearproject;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

//TODO - change to Leader Object and add leader group variable
public class Leader {
    private String personID;
    private String personType;
    private String name;
    private String LastName;
    private String DOB;
    private String group;
    private String Phone;
    private String Email;
    private String vettingDate;
    private String fcmToken;

    public Leader() {
    }

    public Leader(String personID, String personType, String name,  String DOB, String group, String phone, String email, String vettingDate, String fcmToken) {
        this.personID = personID;
        this.personType = personType;
        this.name = name;
        this.DOB = DOB;
        this.group = group;
        this.Phone = phone;
        this.Email = email;
        this.vettingDate = vettingDate;
        this.fcmToken = fcmToken;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getName() {
        return name;
    }

    public void setName(String firstName) {
        name = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
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
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getVettingDate() {
        return vettingDate;
    }

    public void setVettingDate(String vettingDate) {
        this.vettingDate = vettingDate;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    //TODO - valueEventListener not being hit

}
