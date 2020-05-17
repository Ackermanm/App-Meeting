package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewMeetingActivity extends AppCompatActivity {

    EditText titleText;
    Spinner spinner;
    TextView returnTimeText;
    EditText deadlineText;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);

        titleText = findViewById(R.id.editTextTitle);
        deadlineText = findViewById(R.id.editDeadline);
        returnTimeText = findViewById(R.id.textTimeResult); // Show selected date from calendar(Time Activity).
        // Location drop down list
         spinner = findViewById(R.id.spinnerLocation); // Locations are put in a spinner widget.
        //  Adapter, connected from JAVA to UI. This adapter is used to connect Location spinner and items(XML).
        ArrayAdapter<String> locationAdapter =new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.locations));
        spinner.setAdapter(locationAdapter);
    }

    /**
     * Click cancel button, close this new adding.
     * @param v Button cancel.
     */
    public void CancelClicked(View v){

        finish();
    }

    /**
     *  Click time button, go to calendar(time activity).
     * @param v Button Time
     */
    public void TimeClicked(View v){
        Intent intent = new Intent(this,TimeActivity.class);
        startActivityForResult(intent,1);
    }

    /**
     * Get selected dates and show them in TextView.
     * @param requestCode Label the method that will go to get the result.
     * @param resultCode Check which result we get.
     * @param data The intent that wants to get extra.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                String[] times = data.getStringArrayExtra("time"); // times: selected dates.
                String allTime = "";
                for (int i = 0; i < times.length; i++){
                    allTime += times[i];
                    if (i != times.length-1){
                        allTime += "/";
                    }
                }
                returnTimeText.setText(allTime);
            }
        }
    }

    /**
     *  Click done button, deliver details of meeting to finish page(Done activiy).
     * @param v Done Button
     */
    public void DoneClicked(View v){
        if (CheckEligibleMeeting()){
            // Put all code in it.
        }else {
            Toast.makeText(this,"Non Eligible format.",Toast.LENGTH_SHORT).show();
        }
        // Go to Done Activity
        final Intent intent = new Intent(this,DoneActivity.class);
        // Create a new meeting according to info we get from edit texts.
        String title = titleText.getText().toString();
        String location = spinner.getSelectedItem().toString();
        String deadline = deadlineText.getText().toString();
        if (location.equals("location")){
            location = "";
        }
        String time = returnTimeText.getText().toString();
        String[] times = time.split("/");
        Map<String, Object> timess = new HashMap<String,Object>();
        for (String t: times){
            timess.put(t, 0);
        }
        // Variables must be declared as final can be used in on Data Change method.
        final Meeting meeting = new Meeting(title,location,deadline,timess);
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
                if (user.meetings == null){
                    user.meetings = new ArrayList<Meeting>();
                }
                user.meetings.add(meeting);
                myRef.setValue(user);
                intent.putExtra("User Uid",userUid);
                startActivity(intent);
                finish(); // Close new meeting activity after we finish this form.
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(NewMeetingActivity.this,"Failed to update UI",Toast.LENGTH_SHORT).show();
            }});
        //Method 1, all these methods must be written in on Data Change method.
        /*int index = user.meetings.size();
        Map<String,Object> values = new HashMap<>();
        values.put("/" + userUid + "/" + "/meetings/" + index, meeting);
        myRef.updateChildren(values);
        startActivity(intent);*/
        // Method 2
    }
    public Boolean CheckEligibleMeeting(){
        return true;
    }
}
