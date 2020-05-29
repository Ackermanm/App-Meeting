package com.example.voteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
    /**
     * The interface after create a new meeting, there will be the meeting id and back button.
     * */
public class DoneActivity extends AppCompatActivity {
    TextView textView;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        textView = findViewById(R.id.textDetail);
        textView.isSelected();
        Intent intent = getIntent();
        String userUid = intent.getStringExtra("User Uid");
        // Following methods using firebase are adopted form firebase website.
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = firebaseDatabase.getReference(userUid);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Create meeting id by concatenate user id and meeting index.
                user = dataSnapshot.getValue(User.class);
                int index = user.meetings.size() - 1;
                String text = myRef.getKey() + "/" + index;
                textView.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /**
     * Go directly back to main interface, jump the new meeting interface.
     * */
    public void DoneClicked(View v) {
        finish();
    }
}
