package com.example.voteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;


public class TimeActivity extends AppCompatActivity {

    CalendarView calendarView;
    LinearLayout timeLayout;
    LinearLayout timeLayoutDelete;
    int id; // Id for new added meeting time.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        timeLayout = findViewById(R.id.linearChosenDate);
        timeLayoutDelete = findViewById(R.id.linearChosenDateDelete);
        calendarView = findViewById(R.id.calendarTime);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {



            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth+"-"+(month+1)+"-"+year;
                view.getDate();

                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,100);
                TextView textView = new TextView(TimeActivity.this);
                textView.setLayoutParams(textParams);
                textView.setText(date);
                textView.setId(20200509+id);

                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,100);
                Button button = new Button(TimeActivity.this);
                button.setLayoutParams(buttonParams);
                button.setText("delete");
                button.setId(10200509+id);
                final int i = button.getId();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeLayout.removeView(findViewById(i + 10000000));
                        timeLayoutDelete.removeView(findViewById(i));
                    }
                });

                timeLayout.addView(textView);
                timeLayoutDelete.addView(button);
                Intent intent = new Intent(TimeActivity.this,SetUpActivity.class);

                intent.putExtra("selectedTime",date);
                textView.setText(date);
            }
        });

        /*Long l = calendarView.getDate();
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(l);
        textView.setText(dateString);*/
    }



}
