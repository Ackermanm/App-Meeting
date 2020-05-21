package com.example.voteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeActivity extends AppCompatActivity {

    CalendarView calendarView;
    TextView startDate;
    String clockString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        calendarView = findViewById(R.id.calendarTime);
        startDate = findViewById(R.id.startDate);

        /**
         * When change date, it will add a textView to show selected date and a delete button which can delete the textView.
         */
        calendarView.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = (month + 1) + "-" + dayOfMonth + "-" + year;
                Date now = Calendar.getInstance().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                String nowString = sdf.format(now);
                try {
                    Date nowDate = sdf.parse(nowString);
                    Date chosenDate = sdf.parse(date);
                    if (chosenDate.compareTo(nowDate) >= 0){
                        startDate.setText(date);
                    }else {
                        Toast.makeText(TimeActivity.this,"Meeting date must be after or equal today",Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * Show time picker when click switch button
         */
        final Switch switchButton = (Switch) findViewById(R.id.switchAddTime);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DialogFragment newFragment = new TimePickerFragment();
                if (isChecked) {
                    newFragment.show(getSupportFragmentManager(), "timePicker");
                } else {
                    TextView textView = findViewById(R.id.startTime);
                    clockString = "";
                    textView.setText(clockString);
                }
            }
        });
    }

    /**
     * The result of time picker when click ok
     */
    public void showTimePickerResult(int hour, int minute) {
        clockString = Integer.toString(hour) + ":" + Integer.toString(minute);
        TextView textView = findViewById(R.id.startTime);
        textView.setText(clockString);
    }

    /**
     * The result of time picker when click Cancel
     */
    public void clickCancelTimePicker() {
        Switch switchbutton = (Switch) findViewById(R.id.switchAddTime);
        switchbutton.setChecked(false);
    }

    /**
     * Click cancel button, cancel to select any date and return SetUp activity.
     *
     * @param v Button Cancel.
     */
    public void CancelTimeClicked(View v) {

        String[] times = new String[0];
        Intent data = this.getIntent().putExtra("time", times);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * Click done button, deliver selected date(times string array)
     *
     * @param v Button Done
     */
    public void DoneTimeClicked(View v) {
        TextView timeTextView = findViewById(R.id.startTime);
        String date = startDate.getText().toString();
        String time = timeTextView.getText().toString();
        String validTime = date + " " + time;
        Intent data = this.getIntent().putExtra("time", validTime);
        setResult(RESULT_OK, data);
        finish();
    }
}
