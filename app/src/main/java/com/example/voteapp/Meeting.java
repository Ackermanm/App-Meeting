package com.example.voteapp;

import java.util.List;
import java.util.Map;

public class Meeting {
    String title;
    String location;
    String duration;
    String deadline;
    // Map string for date, object for poll.
    Map<String, Object> times;
//    List<String> times;

//    public Meeting(String title, String location, List<String> times) {
//        this.title = title;
//        this.location = location;
//        this.times = times;
//    }


    public Meeting(String title, String location, String duration, String deadline, Map<String, Object> times) {
        this.title = title;
        this.location = location;
        this.duration = duration;
        this.deadline = deadline;
        this.times = times;
    }

    // Empty constructor is used for generate instance from database.
    public Meeting() {
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Map<String, Object> getTimes() {
        return times;
    }

    public void setTimes(Map<String, Object> times) {
        this.times = times;
    }
}
