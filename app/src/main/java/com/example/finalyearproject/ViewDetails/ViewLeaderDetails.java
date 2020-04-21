package com.example.finalyearproject.ViewDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalyearproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.finalyearproject.Objects.Leader;

public class ViewLeaderDetails extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    EditText txtProfileName;
    EditText txtProfileEmail;
    EditText txtDOB;
    EditText txtVettingDate;
    EditText txtProfileGroup;
    EditText txtProfilePhoneNum;

    String intentType;
    String intentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leader_details);

        Intent i = getIntent();
        intentType = i.getStringExtra("type");
        intentId = i.getStringExtra("id");
        txtProfileName = findViewById(R.id.txtProfileName);
        txtProfileEmail = findViewById(R.id.txtProfileEmail);
        txtDOB = findViewById(R.id.txtProfileDOB);
        txtVettingDate = findViewById(R.id.profileVettingDate);
        txtProfileGroup = findViewById(R.id.profileGroup);
        txtProfilePhoneNum = findViewById(R.id.profilePhoneNum);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Person").child("Leader");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String id = ds.getValue(Leader.class).getPersonID();
                    String name = ds.getValue(Leader.class).getName();
                    String email = ds.getValue(Leader.class).getEmail();
                    String DOB = ds.getValue(Leader.class).getDOB();
                    String vettingDate = ds.getValue(Leader.class).getVettingDate();
                    String group = ds.getValue(Leader.class).getGroup();
                    String phone = ds.getValue(Leader.class).getPhone();

                    if (id.equals(intentId)) {
                        Toast.makeText(ViewLeaderDetails.this, "leader found", Toast.LENGTH_LONG).show();
                        txtProfileName.setText(name);
                        txtProfileEmail.setText(email);
                        txtDOB.setText(DOB);
                        txtVettingDate.setText(vettingDate);
                        txtProfileGroup.setText(group);
                        txtProfilePhoneNum.setText(phone);
                        break;
                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
