package com.example.voteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VoteActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    TextView title;
    TextView location;
    TextView duration;
    TextView deadline;
    DatabaseReference myRef;
    User user;
    String userId;
    String meetingIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        linearLayout = findViewById(R.id.linearForMeeting);
        title = findViewById(R.id.textTitle);
        location = findViewById(R.id.textLocation);
        duration = findViewById(R.id.textDuration);
        deadline = findViewById(R.id.textDeadlineVote);
        // Get meeting id(including user id and meeting index).
        Intent thisIntent = getIntent();
        String meetingId = thisIntent.getStringExtra("Meeting ID");
        String[] text = meetingId.split("/");
        userId = text[0];
        meetingIndex = text[1];
        // Get user detail from database.
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference(userId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // If user if null, check uid.
                user = dataSnapshot.getValue(User.class);
                try {
                    UpdateUI();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VoteActivity.this, "Database eooro: on cancelled.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Update vote UI according to meeting details(title, location, times, radio buttons to choose time)
     */
    public void UpdateUI() throws ParseException {
        Meeting meeting = user.meetings.get(Integer.parseInt(meetingIndex));
        String t = "Title: " + meeting.title;
        title.setText(t);
        String lo = "Location: " + meeting.location;
        location.setText(lo);
        String dura = "Duration: " + meeting.duration;
        duration.setText(dura);
        String d = "Vote deadline: " + meeting.deadline;
        deadline.setText(d);
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date deadlineDate = sdf.parse(meeting.deadline);
        if (deadlineDate.compareTo(now) > 0) {
            // Set title and location for the meeting
            int i = 0; // index for radio button.
            for (String key : meeting.times.keySet()) {
                // Create new horizontal linear layout to restore a meeting time.
                LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LinearLayout ll = new LinearLayout(this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setLayoutParams(llParams);
                // Create new time
                LinearLayout.LayoutParams llTime = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView time = new TextView(this);
                time.setLayoutParams(llTime);
                time.setText(key);
                // Create a button to choose a time
                CheckBox cb = new CheckBox(this);
                LinearLayout.LayoutParams llRb = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                cb.setLayoutParams(llRb);
                // Set ID for checkBox.
                cb.setId(30200517 + i);
                i++;
                // Add time and radio button to linear layout.
                ll.addView(time);
                ll.addView(cb);
                // Add new linear layout to vote page.
                linearLayout.addView(ll);
            }
        } else {
            String result = "";
            int voteTime = 0;
            for (String key : meeting.times.keySet()) {
                Long l = (Long) meeting.times.get(key);
                int time = l.intValue();
                if (time > voteTime) {
                    voteTime = time;
                    result = key;
                }
            }
            LinearLayout.LayoutParams llTime = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView time = new TextView(this);
            time.setLayoutParams(llTime);
            String r = "Final meeting date: " + result;
            time.setText(r);
            linearLayout.addView(time);

            LinearLayout ll = findViewById(R.id.linearForSubmit);
            Button submit = findViewById(R.id.buttonSubmit);
            ll.removeView(submit);
        }


    }

    /**
     * Click submit button, update vote info for the meeting.
     *
     * @param v Submit button
     */
    public void SubmitClicked(View v) {
        Meeting meeting = user.meetings.get(Integer.parseInt(meetingIndex));
        int i = 0; // index for radio button
        for (String key : meeting.times.keySet()) {
            CheckBox cb = findViewById(30200517 + i);
            Boolean checked = cb.isChecked();
            if (checked) {
                meeting.times.put(key, ((Long) meeting.times.get(key)).intValue() + 1);
            }
            i++;
        }
        myRef.setValue(user);
        Toast.makeText(this, "Submit success!", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Go back to main activity
     *
     * @param v Button back
     */
    public void BackClicked(View v) {
        finish();
    }
}
