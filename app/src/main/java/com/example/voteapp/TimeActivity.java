package com.example.voteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;


public class TimeActivity extends AppCompatActivity {

    CalendarView calendarView;
    LinearLayout timeLayout;
    LinearLayout timeLayoutDelete;
    int[] id;
    String[] times;
    Boolean reachLimit;
    Boolean existTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        id = new int[5];
        times = new String[5];
        timeLayout = findViewById(R.id.linearChosenDate);
        timeLayoutDelete = findViewById(R.id.linearChosenDateDelete);
        calendarView = findViewById(R.id.calendarTime);
        Arrays.fill(id, 0);
        Arrays.fill(times, "");
        /**
         * When change date, it will add a textView to show selected date and a delete button which can delete the textView.
         */
        calendarView.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Check whether already selected 5 date(limit 5)
                reachLimit = true;
                for (int value : id) {
                    if (value == 1) {
                        reachLimit = true;
                    } else {
                        reachLimit = false;
                        break;
                    }
                }
                // Check whether selected time exist.
                existTime = false;
                for (String time : times) {
                    String date = dayOfMonth + "-" + (month + 1) + "-" + year;
                    if (time.equals(date)) {
                        existTime = true;
                        break;
                    }
                }

                if (reachLimit) {
                    Toast.makeText(TimeActivity.this, "5 times limit", Toast.LENGTH_SHORT).show();
                } else if (existTime) {
                    Toast.makeText(TimeActivity.this, "Existed date", Toast.LENGTH_SHORT).show();
                } else { // Add new time textView and button delete.
                    String date = dayOfMonth + "-" + (month + 1) + "-" + year;
                    // Deploy new textView to show selected date
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 100);
                    TextView textView = new TextView(TimeActivity.this);
                    textView.setLayoutParams(textParams);
                    textView.setText(date);
                    // Deploy new button corresponding selected view to delete it.
                    LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 100);
                    Button button = new Button(TimeActivity.this);
                    button.setLayoutParams(buttonParams);
                    button.setText("delete");
                    // Set textView and button id.
                    for (int i = 0; i < id.length; i++) {
                        if (id[i] == 0) {
                            id[i] = 1; // The ith id has been occupied.
                            times[i] = date;
                            textView.setId(20200509 + i);
                            button.setId(10200509 + i);
                            break;
                        }
                    }
                    // Set on click delete button. delete textView, the button and their id.
                    final int i = button.getId();
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            timeLayout.removeView(findViewById(i + 10000000));
                            timeLayoutDelete.removeView(findViewById(i));
                            id[i - 10200509] = 0; // The ith id has been released.
                            times[i - 10200509] = "";
                        }
                    });

                    timeLayout.addView(textView);
                    timeLayoutDelete.addView(button);
                    Intent intent = new Intent(TimeActivity.this, NewMeetingActivity.class);

                    intent.putExtra("selectedTime", date);
                    textView.setText(date);

                }

            }
        });

        /**
         * Show time picker when click switch button
         */
        Switch switchButton = (Switch) findViewById(R.id.switchAddTime);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "timePicker");
                }
            }
        });
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
        String[] valid = new String[5];
        int all = 0;
        for (int i = 0; i < id.length; i++) {
            if (id[i] == 1) {
                int textViewId = 20200509 + i;
                TextView text = findViewById(textViewId);
                valid[all] = text.getText().toString();
                all++;
            }
        }
        String[] validTime = new String[all];

        for (int i = 0; i < validTime.length; i++) {
            validTime[i] = valid[i];
        }
        Intent data = this.getIntent().putExtra("time", validTime);
        setResult(RESULT_OK, data);
        finish();
    }
}
