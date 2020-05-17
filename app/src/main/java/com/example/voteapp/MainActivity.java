package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    String userUid;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.scrolLinearMeeting);
        // Get User Uid from register activity.
        Intent intent = getIntent();
        userUid = intent.getStringExtra("User Uid");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(userUid);
        // Update User Interface using data from database.
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user.meetings != null){
                    UpdateUI(user);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this,"Failed to get data.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Every time create or start this activity, we update user UI according to database.
     */
/*    @Override
    public void onStart(){
        super.onStart();
    }*/
    /**
     * Click add button, Go to Setup activity.
     * @param v Button Add
     */
    public void AddMeetingClicked(View v){
        Intent intent = new Intent(this, NewMeetingActivity.class);
        intent.putExtra("User Uid",userUid);
        startActivity(intent);
    }

    /**
     * After a user login, update UI according to its data stored in database. Add all the meetings detail belongs this user.
     * @param user Current user
     */
    public void UpdateUI(User user){
        final List<Meeting> meeting = user.meetings;

        for (Meeting m: meeting){
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
            llParams.setMargins(10,10,10,20);
            LinearLayout ll = new LinearLayout(this);
            ll.setLayoutParams(llParams);
            ll.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView title = new TextView(this);
            title.setLayoutParams(textParams);
            TextView location = new TextView(this);
            location.setLayoutParams(textParams);
            TextView time = new TextView(this);
            time.setLayoutParams(textParams);
            title.setText(m.title);
            location.setText(m.location);
            String allTime = "";
            for (String key: m.times.keySet()){
                allTime += (key + "/");
            }
            allTime = allTime.substring(0,allTime.length()-1);
            time.setText(allTime);
            ll.addView(title);
            ll.addView(location);
            ll.addView(time);
            linearLayout.addView(ll);

            final String meetingId = userUid + "/" + meeting.indexOf(m);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                    intent.putExtra("meetingId",meetingId);
                    startActivity(intent);
                }
            });
        }
    }
}

