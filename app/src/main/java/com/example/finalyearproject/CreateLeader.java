package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Objects.Leader;

public class CreateLeader extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private AppBarConfiguration mAppBarConfiguration;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText confirmPassword;
    private Spinner groupType;
    private EditText dateOfBirth;
    private EditText phone;
    private EditText email;
    private EditText vetting;
    private EditText startDate;
    private Spinner group;
    private Leader leader;
    Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_leader);
        mAuth = FirebaseAuth.getInstance();
        Spinner groupSpinner = findViewById(R.id.groupType);
        final String groupTypeList[] = new String[]{"Please Select", "Beavers", "Cubs", "Scouts", "Ventures", "Rovers"};

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groupTypeList);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        groupSpinner.setAdapter(adapter);
    }

    public void createNewLeader(View v) {
        mUser = mAuth.getCurrentUser();
        final ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please Wait");
        mProgress.show();
        firstName = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        dateOfBirth = (EditText) findViewById(R.id.DOB);
        phone = (EditText) findViewById(R.id.phoneNum);
        email = (EditText) findViewById(R.id.email);
        vetting = (EditText) findViewById(R.id.vettingDate);
        group = findViewById(R.id.groupType);


        final String nameValue = firstName.getText().toString();
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
                                Toast.makeText(CreateLeader.this, "Worked", Toast.LENGTH_LONG).show();

                                String id = mUser.getUid();
                                leader = new Leader(id, "Leader", nameValue, DOBvalue,"Ventures", phoneNumValue, emailValue, vettingValue, null);
                                myRef.child("Person").child("Leader").child(id).setValue(leader);
                                mProgress.dismiss();
                                activity.finish();


                            } else {
                                mProgress.dismiss();
                                Toast.makeText(CreateLeader.this, "Didn't Work", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        } else {
            Toast.makeText(CreateLeader.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }
}
