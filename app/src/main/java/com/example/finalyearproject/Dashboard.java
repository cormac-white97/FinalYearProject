package com.example.finalyearproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Dashboard extends AppCompatActivity {
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
    private Person person;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_group, R.id.nav_events,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Person");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
//        View headerView = navigationView.getHeaderView(0);
//

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = mAuth.getInstance().getCurrentUser();

                View headerView = navigationView.getHeaderView(0);
                final String currentUserEmail = mUser.getEmail();


                TextView userName = headerView.findViewById(R.id.logedInUser);
                TextView userType = headerView.findViewById(R.id.logedInType);
                TextView userGroup = headerView.findViewById(R.id.logedInGroup);
                TextView userEmail = headerView.findViewById(R.id.logedInEmail);
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String email = ds.getValue(Person.class).getEmail();
                    String fName = ds.getValue(Person.class).getFirstName();
                    String lName = ds.getValue(Person.class).getLastName();
                    String type = ds.getValue(Person.class).getPersonType();
                    String group = ds.getValue(Person.class).getGroup();


                    if(currentUserEmail.equals(email)){
                        String loggedName = fName + " "  + lName;
                        userName.setText(loggedName);
                        userType.setText(type);
                        userGroup.setText(group);
                        userEmail.setText(email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void createNewLeader(View v) {
        final ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please Wait");
        mProgress.show();
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        dateOfBirth = (EditText) findViewById(R.id.DOB);
        phone = (EditText) findViewById(R.id.phoneNum);
        email = (EditText) findViewById(R.id.email);
        vetting = (EditText) findViewById(R.id.vettingDate);
        group = findViewById(R.id.groupType);


        final String firstNameValue = firstName.getText().toString();
        final String lastNameValue = lastName.getText().toString();
        final String passwordValue = password.getText().toString();
        final String confirmPassValue = confirmPassword.getText().toString();
        //final String txtGroup = group.getSelectedItem().toString();
        final String DOBvalue = dateOfBirth.getText().toString();
        final String phoneNumValue = phone.getText().toString();
        final String emailValue = email.getText().toString();
        final String vettingValue = vetting.getText().toString();
       // final String typeValue = spinner.getSelectedItem().toString();


        if (passwordValue.equals(confirmPassValue) && mAuth != null) {
            myRef = database.getInstance().getReference();
            mAuth.createUserWithEmailAndPassword(emailValue, passwordValue)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(Dashboard.this, "Worked", Toast.LENGTH_LONG).show();
//                                String id = UUID.randomUUID().toString();
                                String id = mUser.getUid();
                                person = new Person(id, "Leader", firstNameValue, lastNameValue, DOBvalue,"Ventures", phoneNumValue, emailValue, vettingValue, null);
                                myRef.child("Person").child(id).setValue(person);
                                mProgress.dismiss();

                            } else {
                                mProgress.dismiss();
                                Toast.makeText(Dashboard.this, "Didn't Work", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        } else {
            Toast.makeText(Dashboard.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }

    public void delete(View v){
        final String dateToDel = startDate.getText().toString();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ProgressDialog mProgress = new ProgressDialog(Dashboard.this);
                mProgress.setMessage("Removing Event");

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    final String key = ds.getValue(EventObj.class).getDate();
                    long longKey = Long.parseLong(key);
                    DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                    String formatted = dateFormat.format(longKey);

                    if(dateToDel.equals(formatted)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                        builder.setTitle("Warning");
                        builder.setMessage("Are you sure you want to delete this event?");
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                mProgress.show();
                                myRef.child(key).removeValue();
                                Toast.makeText(Dashboard.this, "Event was successfully removed.", Toast.LENGTH_LONG).show();

                            }});
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                        break;
                    }

                }

                mProgress.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void editTextWasClicked(MenuItem item){
        mAuth.signOut();
        Intent logout = new Intent(Dashboard.this, MainActivity.class);
        startActivity(logout);
        finish();
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