package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    String userUid;
    User user;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.scrolLinearMeeting);
        // Get User Uid from register activity.
        Intent intent = getIntent();
        userUid = intent.getStringExtra("User Uid");
    }


    /**
     * Every time create or start this activity, we update user UI according to database.
     */
    @Override
    public void onStart() {
        super.onStart();
        linearLayout.removeAllViews();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(userUid);
        // Update User Interface using data from database.
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user.meetings != null) {
                    UpdateUI(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, "Failed to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Click add button, Go to Setup activity.
     *
     * @param v Button Add
     */
    public void AddMeetingClicked(View v) {
        Intent intent = new Intent(this, NewMeetingActivity.class);
        intent.putExtra("User Uid", userUid);
        startActivity(intent);
    }

    /**
     * After a user login, update UI according to its data stored in database. Add all the meetings detail belongs this user.
     *
     * @param user Current user
     */
    @SuppressLint("SetTextI18n")
    public void UpdateUI(User user) {
        final List<Meeting> meeting = user.meetings;
        int indexOfMeeting = 0;
        for (final Meeting m : meeting) {
            if (m != null) { // Do not make UI for those deleted meeting.
                LinearLayout.LayoutParams llHorizontal = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                llHorizontal.setMargins(20, 10, 20, 10);
                // Set linear layout
                LinearLayout.LayoutParams llVerticalText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                llVerticalText.weight = 1;
                LinearLayout.LayoutParams llVerticalDelete = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

                final LinearLayout llH = new LinearLayout(this);
                llH.setOrientation(LinearLayout.HORIZONTAL);
                llH.setLayoutParams(llHorizontal);
                // Set linear layout includes info.
                LinearLayout llVT = new LinearLayout(this);
                llVT.setOrientation(LinearLayout.VERTICAL);
                llVT.setLayoutParams(llVerticalText);
                // Include delete button.
                LinearLayout llVD = new LinearLayout(this);
                llVD.setOrientation(LinearLayout.VERTICAL);
                llVD.setLayoutParams(llVerticalDelete);

                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonParams.setMargins(0, 40, 0, 0);
                // Set title
                TextView title = new TextView(this);
                title.setLayoutParams(textParams);
                title.setText(m.title);
                title.setTextSize(18);
                // Set location
                TextView location = new TextView(this);
                location.setLayoutParams(textParams);
                location.setTextSize(18);
                location.setText(m.location);
                // Set time
                TextView time = new TextView(this);
                time.setLayoutParams(textParams);
                String allTime = "";
                for (String key : m.times.keySet()) {
                    allTime += (key + "/");
                }
                allTime = allTime.substring(0, allTime.length() - 1);
                time.setText(allTime);
                // Set delete button
                Button button = new Button(this);
                button.setText("Delete");
                button.setLayoutParams(buttonParams);
                final int finalIndexOfMeeting = indexOfMeeting;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogShow(llH, finalIndexOfMeeting);
                    }
                });
                // Add all views into linear layout, finally add into this page
                llVT.addView(title);
                llVT.addView(location);
                llVT.addView(time);
                llVD.addView(button);
                llH.addView(llVT);
                llH.addView(llVD);
                linearLayout.addView(llH);
                // Go to the detail of meeting by clicking the meeting.
                final String meetingId = userUid + "/" + meeting.indexOf(m);
                llVT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("meetingId", meetingId);
                        startActivity(intent);
                    }
                });
            }
            indexOfMeeting++;
        }
    }


    public void UpdateDatabase(int index) {
        List<Meeting> meeting = user.meetings;
        meeting.set(index, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        myRef.setValue(user);
    }

    /**
     * Dialog, yes to delete meeting, no cancel.
     *
     * @param v                   The linear layout that includes the meeting detail.
     * @param finalIndexOfMeeting Index of the meeting.
     */
    public void dialogShow(final View v, final int finalIndexOfMeeting) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Do you want to delete this meeting?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                linearLayout.removeView(v);
                UpdateDatabase(finalIndexOfMeeting);
            }
        });
        dialog.setNegativeButton("No", null);
        dialog.show();
    }
}

