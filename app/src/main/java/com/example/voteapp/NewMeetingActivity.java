package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewMeetingActivity extends AppCompatActivity {

    EditText titleText;
    EditText locationText; // spinner for location choice
    EditText durationText;
    TextView returnTimeText;
    EditText deadlineText;
    User user;
    String allTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);

        titleText = findViewById(R.id.editTextTitle);
        deadlineText = findViewById(R.id.editDeadline);
        returnTimeText = findViewById(R.id.textTimeResult); // Show selected date from calendar(Time Activity).
        // Location
        locationText = findViewById(R.id.editNewLocation);
        durationText = findViewById(R.id.editMeetingDuration);
        allTime = "";
    }

    /**
     * Click cancel button, close this new adding.
     *
     * @param v Button cancel.
     */
    public void CancelClicked(View v) {

        finish();
    }

    /**
     * Click Add button, go to calendar(time activity).
     *
     * @param v Button Time
     */
    public void AddClicked(View v) {
        Intent intent = new Intent(this, TimeActivity.class);
        startActivityForResult(intent, 1);
    }

    /**
     * Get selected dates and show them in TextView.
     *
     * @param requestCode Label the method that will go to get the result.
     * @param resultCode  Check which result we get.
     * @param data        The intent that wants to get extra.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String time = data.getStringExtra("time"); // times: selected dates.
                allTime += time + "/";
                /**
                 * Following two lines are adapted from online code
                 * Source: https://android--examples.blogspot.com/2015/01/textview-new-line-multiline-in-android.html
                 */
                if (returnTimeText.getText().toString().equals("")) {
                    returnTimeText.setText(time);
                } else {
                    returnTimeText.append(System.getProperty("line.separator"));
                    returnTimeText.append(time);
                }
            }
        }
    }

    /**
     * Click done button, deliver details of meeting to finish page(Done activiy).
     *
     * @param v Done Button
     */
    public void DoneClicked(View v) {
        // Create a new meeting according to info we get from edit texts.
        String title = titleText.getText().toString();
        String location = locationText.getText().toString();
        String duration = durationText.getText().toString();
        String deadline = deadlineText.getText().toString();
        String[] times = allTime.split("/");
        if (!CheckEligibleMeeting(title, location, duration, deadline, times)) {

        } else {
            // Go to Done Activity
            final Intent intent = new Intent(this, DoneActivity.class);
            Map<String, Object> timess = new HashMap<String, Object>();
            for (String t : times) {
                timess.put(t, 0);
            }
            // Variables must be declared as final can be used in on Data Change method.
            final Meeting meeting = new Meeting(title, location, duration, deadline, timess);
            Intent thisIntent = getIntent();
            // Get User Uid from Main activity.
            final String userUid = thisIntent.getStringExtra("User Uid");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Get current user's reference in database.
            final DatabaseReference myRef = database.getReference(userUid);
            // Add a new meeting to this user's database.
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    if (user.meetings == null) {
                        user.meetings = new ArrayList<Meeting>();
                    }
                    user.meetings.add(meeting);
                    myRef.setValue(user);
                    intent.putExtra("User Uid", userUid);
                    startActivity(intent);
                    finish(); // Close new meeting activity after we finish this form.
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Toast.makeText(NewMeetingActivity.this, "Failed to update UI", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Check correct format of meeting information.
     *
     * @param title    title of meeting
     * @param location location of meeting
     * @param deadline deadline of meeting
     * @param times    dates of meeting
     * @return true if all information are correct format.
     */
    public Boolean CheckEligibleMeeting(String title, String location, String duration, String deadline, String[] times) {
        String[] arrayDl = deadline.split("-");
        if (title.equals("")) {
            Toast.makeText(this, "Empty title", Toast.LENGTH_SHORT).show();
            return false;
        } else if (location.equals("")) {
            Toast.makeText(this, "Empty location", Toast.LENGTH_SHORT).show();
            return false;
        } else if (duration.equals("")) {
            Toast.makeText(this, "Empty duration", Toast.LENGTH_SHORT);
            return false;
        } else if (deadline.equals("")) {
            Toast.makeText(this, "Empty deadline", Toast.LENGTH_SHORT).show();
            return false;
        } else if (arrayDl.length != 3) {
            Toast.makeText(this, "Wrong deadline format", Toast.LENGTH_SHORT).show();
            return false;
        } else if (times.length == 0 || times[0].equals("Selected dates")) {
            Toast.makeText(this, "Empty schedule", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            if (Integer.parseInt(arrayDl[0]) <= 0 || Integer.parseInt(arrayDl[0]) > 31) {
                Toast.makeText(this, "Wrong deadline format", Toast.LENGTH_SHORT).show();
                return false;
            } else if (Integer.parseInt(arrayDl[1]) <= 0 || Integer.parseInt(arrayDl[1]) > 12) {
                Toast.makeText(this, "Wrong deadline format", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Wrong deadline format", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
