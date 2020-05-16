package com.example.voteapp;

import java.util.List;

public class User {
    String email;
    List<Meeting> meetings;

    public User(String email, List<Meeting> meetings) {
        this.email = email;
        this.meetings = meetings;
    }
    // Empty constructor is used for generate instance from database.
    public User(){
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }
}