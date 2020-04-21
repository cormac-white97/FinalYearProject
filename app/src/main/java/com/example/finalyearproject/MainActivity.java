package com.example.finalyearproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.finalyearproject.Objects.Leader;


public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "MainActivity";
    private EditText email;
    private EditText password;
    private Button signIn;
    private String emailValue;
    private String passwordValue;
    private Leader leader;
    private TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        forgotPass = findViewById(R.id.forgotPassword);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {
                    //user is signed in
                    Log.d(TAG, "User signed in");
                } else {
                    //user is signed out
                    Log.d(TAG, "User signed out");

                }
            }
        };
    }

    public void signIn(View v) {
        emailValue = email.getText().toString();
        passwordValue = password.getText().toString();
        database = FirebaseDatabase.getInstance();
        final ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please Wait");
        mProgress.show();

        if (!emailValue.equals("") && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed sign in", Toast.LENGTH_LONG).show();
                        mProgress.dismiss();
                    } else {
                        mProgress.dismiss();
                        Intent login = new Intent(MainActivity.this, Dashboard.class);
                        startActivity(login);
                        finish();

                    }

                }
            });


        }
        else{
            Toast.makeText(MainActivity.this, "Fill all details.", Toast.LENGTH_LONG);
        }
    }

    public void forgotPassword(View v){
        Intent forgotPass = new Intent(this, forgotPassword.class);
        startActivity(forgotPass);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}