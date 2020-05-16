package com.example.voteapp;

import java.util.List;

public class User {
    String email;
    Meeting meetings;

    public User(String email, Meeting meetings) {
        this.email = email;
        this.meetings = meetings;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Meeting getMeetings() {
        return meetings;
    }

    public void setMeetings(Meeting meetings) {
        this.meetings = meetings;
    }
}