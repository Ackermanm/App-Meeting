package com.example.voteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    EditText usernameedit;
    EditText userpasswordedit;
    String userUid;
    EditText meetingId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.ButtonRegister).setOnClickListener(this);//catch click
        findViewById(R.id.ButtonLogin).setOnClickListener(this);
        usernameedit = (EditText) findViewById(R.id.username);
        userpasswordedit = (EditText) findViewById(R.id.password);
        meetingId = findViewById(R.id.editMeetingID);

        mAuth = FirebaseAuth.getInstance();

    }

//Animation to make the app more interesting still being developed
    public void Mstar2(View view) {
        ImageView Star = (ImageView) findViewById(R.id.Mstar1);
        ObjectAnimator animator = ObjectAnimator.ofFloat(Star, "translationX", 900f);
        animator.setDuration(100);
        animator.reverse();
       animator.setRepeatCount(Animation.INFINITE);
        animator.start();
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Every time create or start this activity, clear text in username, password and meeting id.
        usernameedit.setText("");
        userpasswordedit.setText("");
        meetingId.setText("");
    }

    public void register() {
        String username = usernameedit.getText().toString().trim();
        String password = userpasswordedit.getText().toString().trim();
        if (username.equals("")) {
            usernameedit.setError("Please Enter Email");
            return;
        }
        if (password.equals("")) {
            userpasswordedit.setError("Please Enter password");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            usernameedit.setError("Please enter a vaid email");
            usernameedit.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // when the log in process are completed linked to yafeis profile need to add code
                    Toast.makeText(getApplicationContext(), "Successfully, Registered", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    userUid = user.getUid();
                    CreateNewUser();

                }else {
                    Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Create a new user when sign up successfully, and upload this new user to database.
     */
    public void CreateNewUser(){
        List<Meeting> meetings = new ArrayList<>();
        User user = new User(usernameedit.getText().toString(),meetings);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(userUid);
        myRef.setValue(user);
    }

    private void Login() { //method when press login button
        String username = usernameedit.getText().toString();
        String password = userpasswordedit.getText().toString();

        if (username.equals("")) {
            usernameedit.setError("Please Enter Email");
            return;
        }
        if (password.equals("")) {
            userpasswordedit.setError("Please Enter password");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            usernameedit.setError("Please enter a vaid email");
            usernameedit.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {//check the result with the server if successfully login do this
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    FirebaseUser user = mAuth.getCurrentUser();
                    intent.putExtra("User Uid",user.getUid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//when press back button
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ButtonRegister:
                register();
                break;
            case R.id.ButtonLogin:
//                System.out.println("yes");
                Login();
               /* startActivity(new Intent(this, MainActivity.class));*/
                break;
        }

    }

    /**
     * Test. enter meeting by meeting id(meeting id = User id + / + meeting index).
     * @param v
     */
    public void GoVoteClicked(View v){
        final Intent intent = new Intent(this,VoteActivity.class);
        // Get meeting id from edit text.
        final String text = meetingId.getText().toString();
        if (EligibleMeetingId(text)){
            String[] texts = text.split("/");
            String ref = texts[0];
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = firebaseDatabase.getReference(ref);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
//                        Log.d("Exist data: ","Yes!");
                        intent.putExtra("Meeting ID",text);
                        startActivity(intent);
                    }else {
//                        Log.d("Exist data: ","No!");
                        Toast.makeText(RegisterActivity.this,"Incorrect meeting ID", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Exist data: ","Go onCancelled.");
                }
            });
        }else {
            Toast.makeText(this,"Wrong meeting ID format",Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean EligibleMeetingId(String meetingID){
        if (!meetingID.contains("/")){
            return false;
        }
        String[] info = meetingID.split("/");
        return info.length == 2;
    }
}
