package com.example.voteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import java.sql.Time;


public class TimeActivity extends AppCompatActivity {

    CalendarView calendarView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);


        textView = findViewById(R.id.textTest);
        calendarView = findViewById(R.id.calendarTime);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(TimeActivity.this,SetUpActivity.class);
                String date = dayOfMonth+"-"+month+"-"+year;
                intent.putExtra("selectedTime",date);
                textView.setText(date);
            }
        });

        /*Long l = calendarView.getDate();
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(l);
        textView.setText(dateString);*/
    }

}
