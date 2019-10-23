package com.example.finalyearproject.ui;

public class Leader {
    private int leaderId;
    private String leaderFirstName;
    private String leaderLastName;
    private int leaderAge;
    private String leaderPhone;
    private String leaderEmail;
    private boolean isVetted;

    public Leader(String leaderFirstName, String leaderLastName, int leaderAge, String leaderPhone, String leaderEmail, boolean isVetted) {
        this.leaderFirstName = leaderFirstName;
        this.leaderLastName = leaderLastName;
        this.leaderAge = leaderAge;
        this.leaderPhone = leaderPhone;
        this.leaderEmail = leaderEmail;
        this.isVetted = isVetted;
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

    public int getLeaderAge() {
        return leaderAge;
    }

    public void setLeaderAge(int leaderAge) {
        this.leaderAge = leaderAge;
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

    public boolean isVetted() {
        return isVetted;
    }

    public void setVetted(boolean vetted) {
        isVetted = vetted;
    }
}
