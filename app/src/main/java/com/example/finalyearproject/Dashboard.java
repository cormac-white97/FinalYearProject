package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {
    private DataSnapshot dataSnapshot = null;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private AppBarConfiguration mAppBarConfiguration;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText confirmPassword;
    private EditText dateOfBirth;
    private EditText phone;
    private EditText email;
    private EditText vetting;
    private Leader leader;
    private DatabaseReference mLEaderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_group, R.id.nav_events,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Intent leaderLogin = getIntent();
        String message = leaderLogin.getStringExtra(MainActivity.leaderKey);

    }


    public void createNewLeader(View v) {

       // myRef.keepSynced(true);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        dateOfBirth = (EditText) findViewById(R.id.DOB);
        phone = (EditText) findViewById(R.id.phoneNum);
        email = (EditText) findViewById(R.id.email);
        vetting = (EditText) findViewById(R.id.vettingDate);

        final String firstNameValue = firstName.getText().toString();
        final String lastNameValue = lastName.getText().toString();
        final String passwordValue = password.getText().toString();
        final String confirmPassValue = confirmPassword.getText().toString();
        final String DOBvalue = dateOfBirth.getText().toString();
        final String phoneNumValue = phone.getText().toString();
        final String emailValue = email.getText().toString();
        final String vettingValue = vetting.getText().toString();

        if (passwordValue.equals(confirmPassValue) && mAuth != null) {
            myRef = database.getInstance().getReference();
            mAuth.createUserWithEmailAndPassword(emailValue, passwordValue)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(Dashboard.this, "Worked", Toast.LENGTH_LONG).show();
                                leader = new Leader(2, firstNameValue, lastNameValue, DOBvalue, phoneNumValue, emailValue, vettingValue);
                                myRef.child("Leader").child("Leader").setValue(leader);
                            } else {
                                Toast.makeText(Dashboard.this, "Didn't Work", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }

    public void readDatabaseName(View v){
/*
        final TextView mTextView = (TextView) findViewById(R.id.txtName);
        String value = dataSnapshot.getValue(String.class);

        mTextView.setText("Value is: " + value);
*/

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}