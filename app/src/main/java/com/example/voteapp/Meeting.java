package com.example.voteapp;

import java.util.List;

public class Meeting {
    String title;
    String location;
    List<String> times;

    public Meeting(String title, String location, List<String> times) {
        this.title = title;
        this.location = location;
        this.times = times;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }
}
