package com.example.finalyearproject;

public class Leader {
    private int leaderId;
    private String leaderFirstName;
    private String leaderLastName;
    private String leaderDOB;
    private String leaderPhone;
    private String leaderEmail;
    private String vettingDate;

    public Leader(int leaderId, String leaderFirstName, String leaderLastName, String leaderDOB, String leaderPhone, String leaderEmail, String vettingDate) {
        this.leaderId = leaderId;
        this.leaderFirstName = leaderFirstName;
        this.leaderLastName = leaderLastName;
        this.leaderDOB = leaderDOB;
        this.leaderPhone = leaderPhone;
        this.leaderEmail = leaderEmail;
        this.vettingDate = vettingDate;
    }

    public String getLeaderDOB() {
        return leaderDOB;
    }

    public void setLeaderDOB(String leaderDOB) {
        this.leaderDOB = leaderDOB;
    }

    public String getVettingDate() {
        return vettingDate;
    }

    public void setVettingDate(String vettingDate) {
        this.vettingDate = vettingDate;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderFirstName() {
        return leaderFirstName;
    }

    public void setLeaderFirstName(String leaderFirstName) {
        this.leaderFirstName = leaderFirstName;
    }

    public String getLeaderLastName() {
        return leaderLastName;
    }

    public void setLeaderLastName(String leaderLastName) {
        this.leaderLastName = leaderLastName;
    }

    public String getLeaderPhone() {
        return leaderPhone;
    }

    public void setLeaderPhone(String leaderPhone) {
        this.leaderPhone = leaderPhone;
    }

    public String getLeaderEmail() {
        return leaderEmail;
    }

    public void setLeaderEmail(String leaderEmail) {
        this.leaderEmail = leaderEmail;
    }


}
