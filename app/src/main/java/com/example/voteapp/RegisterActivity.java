package com.example.voteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailText;
    EditText passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.editEmail);
        passwordText = findViewById(R.id.editPassword);
    }

    public void SignInClicked(View v){
        final String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        /**
         * The following method is Adapted from firebase website.
         * - Web: https://firebase.google.com/docs/auth/android/password-auth
         */
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(RegisterActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        FirebaseUser user = mAuth.getCurrentUser();
                        intent.putExtra("User Uid",user.getUid());
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(RegisterActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public void SignUpClicked(View v){
        final String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        /**
         * The following method is Adapted from firebase website.
         * - Web: https://firebase.google.com/docs/auth/android/password-auth
         */
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(RegisterActivity.this, "Sign up success!.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    FirebaseUser user = mAuth.getCurrentUser();
                    intent.putExtra("User Uid",user.getUid());
                    startActivity(intent);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(RegisterActivity.this, "Sign up failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
