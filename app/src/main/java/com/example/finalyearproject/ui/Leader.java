package com.example.finalyearproject.ui;

public class Leader {
    private int leaderId;
    private String leaderFirstName;
    private String leaderLastName;
    private String leaderDOB;
    private String leaderPhone;
    private String leaderEmail;
    private String vettingDate;

    public Leader(String leaderFirstName, String leaderLastName, String leaderDOB, String leaderPhone, String leaderEmail, String vettingDate) {
        this.leaderFirstName = leaderFirstName;
        this.leaderLastName = leaderLastName;
        this.leaderDOB = leaderDOB;
        this.leaderPhone = leaderPhone;
        this.leaderEmail = leaderEmail;
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

    public String getLeaderAge() {
        return leaderDOB;
    }

    public void setLeaderAge(String leaderDOB) {
        this.leaderDOB = leaderDOB;
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

    public String isVetted() {
        return vettingDate;
    }

    public void setVetted(String vettingDate) {
        vettingDate = vettingDate;
    }
}
