package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * Click add button, Go to Setup activity.
     * @param v Button Add
     */
    public void AddMeetingClicked(View v){
        Intent intent = new Intent(this, NewMeetingActivity.class);
        startActivity(intent);
    }
}
