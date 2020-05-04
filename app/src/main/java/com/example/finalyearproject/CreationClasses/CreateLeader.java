package com.example.finalyearproject.CreationClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.finalyearproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.finalyearproject.Objects.Leader;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateLeader extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private AppBarConfiguration mAppBarConfiguration;
    private EditText firstName;
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
    private Button btnCreate;
    private Button btnUpdate;
    Activity activity = this;

    private String leaderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_leader);
        mAuth = FirebaseAuth.getInstance();
        firstName = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        dateOfBirth = (EditText) findViewById(R.id.DOB);
        phone = (EditText) findViewById(R.id.phoneNum);
        email = (EditText) findViewById(R.id.email);
        vetting = (EditText) findViewById(R.id.vettingDate);
        group = findViewById(R.id.groupType);
        btnCreate = findViewById(R.id.create_account);
        btnUpdate = findViewById(R.id.update_account);
        Spinner groupSpinner = findViewById(R.id.groupType);
        final String groupTypeList[] = new String[]{"Please Select", "Beavers", "Cubs", "Scouts", "Ventures", "Rovers"};

// Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groupTypeList);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        groupSpinner.setAdapter(adapter);

        Intent details = getIntent();
        String type = details.getStringExtra("type");

        if (type.equals("update")) {
            btnCreate.setVisibility(View.GONE);
            leaderId = details.getStringExtra("leaderID");

            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Person").child("Leader");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String id = ds.getValue(Leader.class).getLeaderId();
                        String name = ds.getValue(Leader.class).getName();
                        String dbEmail = ds.getValue(Leader.class).getEmail();
                        String DOB = ds.getValue(Leader.class).getDOB();
                        String vettingDate = ds.getValue(Leader.class).getVettingDate();
                        String dbGroup = ds.getValue(Leader.class).getGroup();
                        String dbPhone = ds.getValue(Leader.class).getPhone();

                        if (id.equals(leaderId)) {
                            firstName.setText(name);
                            email.setText(dbEmail);
                            password.setVisibility(View.GONE);
                            confirmPassword.setVisibility(View.GONE);
                            dateOfBirth.setText(DOB);
                            vetting.setText(vettingDate);
                            int groupPosition = adapter.getPosition(dbGroup);
                            group.setSelection(groupPosition);
                            phone.setText(dbPhone);


                            break;
                        }

                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            btnUpdate.setVisibility(View.GONE);
        }

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateLeader.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String dateString = dateFormat.format(c.getTime());

                                dateOfBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        vetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateLeader.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String dateString = dateFormat.format(c.getTime());

                                vetting.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    public void createNewLeader(View v) {


        final String nameValue = firstName.getText().toString();
        final String passwordValue = password.getText().toString();
        final String confirmPassValue = confirmPassword.getText().toString();
        final String DOBvalue = dateOfBirth.getText().toString();
        final String phoneNumValue = phone.getText().toString();
        final String emailValue = email.getText().toString();
        final String vettingValue = vetting.getText().toString();
        final String groupValue = group.getSelectedItem().toString();


        if (nameValue.equals("") || DOBvalue.equals("") || phoneNumValue.equals("") || emailValue.equals("") || vettingValue.equals("") || groupValue.equals("Please Select")) {
            Toast.makeText(this, "Please fill in all details", Toast.LENGTH_LONG).show();
            if (nameValue.equals("")) {
                firstName.setBackgroundResource(R.drawable.error_border);
            }
            if (DOBvalue.equals("")) {
                dateOfBirth.setBackgroundResource(R.drawable.error_border);
            }
            if (phoneNumValue.equals("")) {
                phone.setBackgroundResource(R.drawable.error_border);
            }
            if (emailValue.equals("")) {
                email.setBackgroundResource(R.drawable.error_border);
            }
            if (vettingValue.equals("")) {
                vetting.setBackgroundResource(R.drawable.error_border);
            }
            if (groupValue.equals("Please Select")) {
                group.setBackgroundResource(R.drawable.error_border);
            }
        } else {

            mUser = mAuth.getCurrentUser();
            final ProgressDialog mProgress = new ProgressDialog(this);
            mProgress.setMessage("Please Wait");
            mProgress.show();
            if (passwordValue.equals(confirmPassValue) && mAuth != null) {
                myRef = database.getInstance().getReference();
                mAuth.createUserWithEmailAndPassword(emailValue, passwordValue)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(CreateLeader.this, "The account was created", Toast.LENGTH_LONG).show();

                                    String id = mUser.getUid();
                                    leader = new Leader(id, nameValue, DOBvalue, groupValue, phoneNumValue, emailValue, vettingValue);
                                    myRef.child("Person").child("Leader").child(id).setValue(leader);
                                    mProgress.dismiss();
                                    activity.finish();


                                } else {
                                    mProgress.dismiss();
                                    Toast.makeText(CreateLeader.this, "An Error Occured", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            } else {
                Toast.makeText(CreateLeader.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateLeader(View v) {
        mUser = mAuth.getCurrentUser();


        final String nameValue = firstName.getText().toString();
        final String DOBvalue = dateOfBirth.getText().toString();
        final String phoneNumValue = phone.getText().toString();
        final String emailValue = email.getText().toString();
        final String vettingValue = vetting.getText().toString();
        final String groupValue = group.getSelectedItem().toString();


        if (nameValue.equals("") || DOBvalue.equals("") || phoneNumValue.equals("") || emailValue.equals("") || vettingValue.equals("") || groupValue.equals("Please Select")) {
            Toast.makeText(this, "Please fill in all details", Toast.LENGTH_LONG).show();
            if (nameValue.equals("")) {
                firstName.setBackgroundResource(R.drawable.error_border);
            }
            if (DOBvalue.equals("")) {
                dateOfBirth.setBackgroundResource(R.drawable.error_border);
            }
            if (phoneNumValue.equals("")) {
                phone.setBackgroundResource(R.drawable.error_border);
            }
            if (emailValue.equals("")) {
                email.setBackgroundResource(R.drawable.error_border);
            }
            if (vettingValue.equals("")) {
                vetting.setBackgroundResource(R.drawable.error_border);
            }
            if (groupValue.equals("Please Select")) {
                group.setBackgroundResource(R.drawable.error_border);
            }
        }
        else {

            final ProgressDialog mProgress = new ProgressDialog(this);
            mProgress.setMessage("Please Wait");
            mProgress.show();
            if (mAuth != null) {


                myRef = database.getInstance().getReference();

                leader = new Leader(leaderId, nameValue, DOBvalue, groupValue, phoneNumValue, emailValue, vettingValue);
                myRef.child("Person").child("Leader").child(leaderId).setValue(leader);
                mProgress.dismiss();
                activity.finish();

                mUser.updateEmail(emailValue)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                mProgress.dismiss();
                finish();

            } else {
                mProgress.dismiss();
                Toast.makeText(CreateLeader.this, "An Error Occured", Toast.LENGTH_LONG).show();

            }
        }
    }
}

