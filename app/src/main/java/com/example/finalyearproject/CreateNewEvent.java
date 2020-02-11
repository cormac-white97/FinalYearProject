package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.EventLog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.ui.event.EventFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class CreateNewEvent extends AppCompatActivity {
    Spinner eventSpinner;
    Spinner groupSpinner;
    EditText txtLocation;
    EditText txtStartDate;
    EditText txtEndDate;
    ImageView btnReturn;
    TextView leaderName;
    private ProgressDialog mProgress;

    private FirebaseDatabase database;
    private DatabaseReference personRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private DatabaseReference eventRef;

    String type;
    String group;
    String dateVal;
    String Startdate;
    Date endDate;
    String createdBy;
    String loggedType;
    long epochEndDate;
    boolean leaderAvailable = true;
    ArrayList<EventObj> existingEvents = new ArrayList<>();
    ArrayList<Long> dbEventDays = new ArrayList<>();
    ArrayList<Long> newEventDays = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);
        Intent newEvent = getIntent();
        String location = newEvent.getStringExtra(viewLocations.locationKey);

        Startdate = newEvent.getStringExtra(viewLocations.dateKey);
        txtEndDate = findViewById(R.id.txtEndDate);
        mProgress = new ProgressDialog(this);
        eventSpinner = (Spinner) findViewById(R.id.txtEvent);
        groupSpinner = findViewById(R.id.txtGroup);
        txtLocation = findViewById(R.id.txtLocation);
        txtStartDate = findViewById(R.id.txtStartDate);
        btnReturn = findViewById(R.id.returnArrow);

        database = FirebaseDatabase.getInstance();
        personRef = database.getReference("Person");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        personRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = mAuth.getInstance().getCurrentUser();

                final String currentUserEmail = mUser.getEmail();


                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String email = ds.getValue(Person.class).getEmail();
                    String ID = ds.getValue(Person.class).getPersonID();

                    if(currentUserEmail.equals(email)){
                        createdBy = ID;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database = FirebaseDatabase.getInstance();
        personRef = database.getReference("Event");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        personRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = mAuth.getInstance().getCurrentUser();

                final String currentUserEmail = mUser.getEmail();


                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                   String eventCreatedBy = ds.getValue(EventObj.class).getCreatedBy();
                   String eventStartDate = ds.getValue(EventObj.class).getDate();
                   String eventEndDate = ds.getValue(EventObj.class).getEndDate();
                   String eventGroup = ds.getValue(EventObj.class).getGroup();
                   String eventId = ds.getValue(EventObj.class).getId();
                   String eventLocation = ds.getValue(EventObj.class).getLocation();
                   String eventType = ds.getValue(EventObj.class).getType();

                    EventObj evObj = new EventObj(eventId, eventType, eventLocation, eventStartDate, eventEndDate, eventGroup, eventCreatedBy);
                    existingEvents.add(evObj);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewEvent.this, viewLocations.class);
                startActivity(intent);
                finish();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.createNewToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        dateVal = sdf.format(Float.parseFloat(Startdate));

        txtStartDate.setText(dateVal);
        txtLocation.setText(location);
        String eventTypeList[] = new String[]{"Please Select", "Camp", "Hike"};
        String groupTypeList[] = new String[]{"Please Select", "Beavers", "Cubs", "Scouts", "Ventures", "Rovers"};


// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, eventTypeList);

        ArrayAdapter<String> groupAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, groupTypeList);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        eventSpinner.setAdapter(adapter);
        groupSpinner.setAdapter(groupAdapter);
    }

    public void createNewEvent(View v) {

        type = eventSpinner.getSelectedItem().toString();
        group = groupSpinner.getSelectedItem().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date endDateVal = sdf.parse(txtEndDate.getText().toString());
            epochEndDate = endDateVal.getTime();
        } catch (ParseException e) {
            Toast.makeText(this, "Please enter a correctly formatted date", Toast.LENGTH_SHORT).show();
        }


        if (type.equals("Please Select") || group.equals("Please Select") || txtEndDate.getText().toString().equals("")) {
            Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_LONG).show();
        } else {

            for (EventObj e : existingEvents){
                long dbStartDate = Long.parseLong(e.getDate());
                long dbEndDate = Long.parseLong(e.getEndDate());
                boolean dateReached = false;

                do{
                    if(dbStartDate == dbEndDate){
                        dbEventDays.add(dbStartDate);
                        dbStartDate = dbStartDate + 86400000L;
                        dateReached = true;
                    }
                    else{
                        dbEventDays.add(dbStartDate);
                        dbStartDate = dbStartDate + 86400000L;
                    }

                }while (dateReached == false);

                Long newStartDate = Long.parseLong(Startdate);
                Long newEndDate = epochEndDate;

                boolean newDateReached = false;

                do{
                    if(newStartDate.equals(newEndDate)){
                        newEventDays.add(newStartDate);
                        newStartDate = newStartDate + 86400000L;
                        newDateReached = true;
                    }
                    else{
                        newEventDays.add(newStartDate);
                        newStartDate = newStartDate + 86400000L;
                    }

                }while (newDateReached == false);
            }


            for (Long day : dbEventDays){
                if(newEventDays.contains(day)){
                    leaderAvailable = false;
                    break;
                }
            }
            if(leaderAvailable == true){
                // Write a message to the database
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Event");
                myRef.keepSynced(true);

                //post to database
                mProgress.setMessage("Adding to Events");
                mProgress.show();
                String eventType = type;
                String loc = txtLocation.getText().toString();
                String dateVal = Startdate;
                String endDateValue = Long.toString(epochEndDate);
                String groupType = group;
                String id = UUID.randomUUID().toString();

                //start uploading....
                EventObj e = new EventObj(id, eventType, loc, dateVal,endDateValue, groupType, createdBy);
                myRef.child(id).setValue(e);
                mProgress.dismiss();
                Intent intent = new Intent(getApplicationContext(), EventFragment.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), "Leader is already assigned to an event for this time", Toast.LENGTH_LONG).show();
            }

        }
    }


}

