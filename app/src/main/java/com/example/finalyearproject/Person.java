package com.example.finalyearproject;

import java.util.UUID;

public class Person {
    private String personID;
    private String personType;
    private String FirstName;
    private String LastName;
    private String DOB;
    private String Phone;
    private String Email;
    private String vettingDate;

    public Person() {
    }

    public Person(String personID, String personType, String firstName, String lastName, String DOB, String phone, String email, String vettingDate) {
        this.personID = personID;
        this.personType = personType;
        FirstName = firstName;
        LastName = lastName;
        this.DOB = DOB;
        Phone = phone;
        Email = email;
        this.vettingDate = vettingDate;
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
}
