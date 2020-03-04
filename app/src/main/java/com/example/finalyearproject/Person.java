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
public class Person {
    private String personID;
    private String personType;
    private String FirstName;
    private String LastName;
    private String DOB;
    private String group;
    private String Phone;
    private String Email;
    private String vettingDate;
    private String fcmToken;

    public Person() {
    }

    public Person(String personID, String personType, String firstName, String lastName, String DOB, String group, String phone, String email, String vettingDate, String fcmToken) {
        this.personID = personID;
        this.personType = personType;
        this.FirstName = firstName;
        this.LastName = lastName;
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

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
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
