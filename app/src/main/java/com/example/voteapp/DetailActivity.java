package com.example.voteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    TextView title;
    TextView location;
    TextView deadline;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        linearLayout = findViewById(R.id.linearForDetail);
        title = findViewById(R.id.textTitle);
        location = findViewById(R.id.textLocation);
        deadline = findViewById(R.id.textDeadline);
        TextView meetingID = findViewById(R.id.textMeetingID);
        Intent intent = getIntent();
        String meeting = intent.getStringExtra("meetingId");
        // Show meeting id.
        meetingID.setText(meeting);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String[] info = meeting.split("/");
        final String userId = info[0];
        final String meetingIndex = info[1];
        DatabaseReference myRef = firebaseDatabase.getReference(userId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                UpdateUI(user, meetingIndex);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void UpdateUI(User user, String meetingIndex){
        Meeting meeting = user.meetings.get(Integer.parseInt(meetingIndex));
        title.setText(meeting.title);
        String l = "Location: " + meeting.location;
        location.setText(l);
        String dl = "Deadline: " + meeting.deadline;
        deadline.setText(dl);

        for (String key: meeting.times.keySet()){
            TextView textView = new TextView(this);
            String text = "Time: " + key + ", Vote: " + meeting.times.get(key);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llParams.setMargins(0,0,20,20);
            textView.setText(text);
            textView.setLayoutParams(llParams);
            linearLayout.addView(textView);
        }
    }
    public void BackClicked(View v){
        finish();
    }
}
