package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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

import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.UUID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public class CreateNewEvent extends AppCompatActivity {
    Spinner eventSpinner;
    Spinner groupSpinner;
    EditText txtLocation;
    EditText txtStartDate;
    EditText txtEndDate;
    ImageView btnReturn;
    TextView leaderName;
    DatePickerDialog picker;
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
    String item;
    long epochEndDate;
    boolean leaderAvailable = true;
    boolean selectedAvailable = true;
    ArrayList<EventObj> existingEvents = new ArrayList<>();
    ArrayList<Person> leaders = new ArrayList<>();
    LinkedHashMap<String, String> allListItems = new LinkedHashMap<>();
    ArrayList<String> eventLeaders = new ArrayList<>();
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    String[] list;
    TextView mItemSelected;
    Calendar myCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_new_event);
        Intent newEvent = getIntent();
        final String location = newEvent.getStringExtra(viewLocations.locationKey);
        Startdate = newEvent.getStringExtra(viewLocations.dateKey);
        txtEndDate = findViewById(R.id.txtEndDate);
        mProgress = new ProgressDialog(getApplicationContext());
        eventSpinner = (Spinner) findViewById(R.id.txtEvent);
        groupSpinner = findViewById(R.id.txtGroup);
        txtLocation = findViewById(R.id.txtLocation);
        txtStartDate = findViewById(R.id.txtStartDate);
        btnReturn = findViewById(R.id.returnArrow);
        mItemSelected = findViewById(R.id.leaderSelected);

        mItemSelected.setVisibility(View.GONE);

        database = FirebaseDatabase.getInstance();
        personRef = database.getReference("Person");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myCalendar = Calendar.getInstance();


        personRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = mAuth.getInstance().getCurrentUser();

                final String currentUserEmail = mUser.getEmail();


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String personID = ds.getValue(Person.class).getPersonID();
                    String personType = ds.getValue(Person.class).getPersonType();
                    String firstName = ds.getValue(Person.class).getFirstName();
                    String lastName = ds.getValue(Person.class).getLastName();
                    String DOB = ds.getValue(Person.class).getDOB();
                    String group = ds.getValue(Person.class).getGroup();
                    String phone = ds.getValue(Person.class).getPhone();

                    String email = ds.getValue(Person.class).getEmail();
                    String vettingDate = ds.getValue(Person.class).getVettingDate();
                    String fcmToken = ds.getValue(Person.class).getFcmToken();

                    Person p = new Person(personID, personType, firstName, lastName, DOB, group, phone, email, vettingDate, fcmToken);
                    leaders.add(p);

                    if (currentUserEmail.equals(email)) {
                        createdBy = personID;

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database = FirebaseDatabase.getInstance();
        eventRef = database.getReference("Event");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = mAuth.getInstance().getCurrentUser();

                final String currentUserEmail = mUser.getEmail();


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String eventCreatedBy = ds.getValue(EventObj.class).getCreatedBy();
                    String eventStartDate = ds.getValue(EventObj.class).getDate();
                    String eventEndDate = ds.getValue(EventObj.class).getEndDate();
                    String eventGroup = ds.getValue(EventObj.class).getGroup();
                    String eventId = ds.getValue(EventObj.class).getId();
                    String eventLocation = ds.getValue(EventObj.class).getLocation();
                    String eventType = ds.getValue(EventObj.class).getType();
                    ArrayList<String> eventLeaders = ds.getValue(EventObj.class).getEventLeaders();
                    int availableSpaces = ds.getValue(EventObj.class).getAvailableSpaces();
                    EventObj evObj = new EventObj(eventId, eventType, eventLocation, eventStartDate, eventEndDate, eventGroup, eventCreatedBy, eventLeaders, availableSpaces);
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
        final String groupTypeList[] = new String[]{"Please Select", "Beavers", "Cubs", "Scouts", "Ventures", "Rovers"};


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

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String groupSelected = groupTypeList[position].toString();
                ArrayList<String> listItemsByGroup = new ArrayList<>();


                listItemsByGroup.clear();
                if (!groupSelected.equals("Please Select")) {
                    list = null;

                    for (Person p : leaders) {
                        String leaderGroup = p.getGroup();
                        String name = p.getFirstName() + " " + p.getLastName();
                        allListItems.put(name, leaderGroup);
                    }
                    //Toast.makeText(parent.getContext(), groupSelected, Toast.LENGTH_SHORT).show();
                    mItemSelected.setVisibility(View.VISIBLE);

                    int i = -1;

                    for (String value : allListItems.values()) {
                        i++;
                        if (value.equals(groupSelected)) {
                            String name = allListItems.keySet().toArray()[i].toString();
                            listItemsByGroup.add(allListItems.keySet().toArray()[i].toString());
                        }

                    }
                    list = GetStringArray(listItemsByGroup);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

    }

    public boolean isLeaderAvailable(String startDate, String endDate, String leaderId) {

        boolean idMatch = false;
        LinkedHashMap<Long, ArrayList<String>> dbEventDays = new LinkedHashMap<>();
        ArrayList<Long> newEventDays = new ArrayList<>();
        long startDateLong = Long.parseLong(startDate);
        long endDateLong = Long.parseLong(endDate);

        for (EventObj e : existingEvents) {
            long dbStartDate = Long.parseLong(e.getDate());
            long dbEndDate = Long.parseLong(e.getEndDate());
            String dbID = e.getCreatedBy();
            ArrayList<String> eventLeaders = e.getEventLeaders();

            boolean dateReached = false;


            String uID = e.getCreatedBy();

            do {
                //add all the days in each existing event in the database to an arraylist
                    if (dbStartDate == dbEndDate) {
                        dbEventDays.put(dbStartDate, eventLeaders);
                        dbStartDate = dbStartDate + 86400000L;
                        dateReached = true;
                    } else {
                        dbEventDays.put(dbStartDate, eventLeaders);
                        dbStartDate = dbStartDate + 86400000L;
                    }

            } while (dateReached == false);


        }
        boolean newDateReached = false;

        do {
            //add all the days in the new event to an arraylist
            if (startDateLong == endDateLong) {
                newEventDays.add(startDateLong);
                startDateLong = startDateLong + 86400000L;
                newDateReached = true;
            } else {
                newEventDays.add(startDateLong);
                startDateLong = startDateLong + 86400000L;
            }

        } while (newDateReached == false);

        //loop through the existing dates in the database and if they match break out of the loop
        // and display a toast message explaining that the leader is not available on these dates
        int i = -1;
        for (Long day : dbEventDays.keySet()) {
            i++;
            String id = dbEventDays.get(day).toString();
            if (newEventDays.contains(day) && id.contains(leaderId)) {
                leaderAvailable = false;
                break;
            }
        }
        return leaderAvailable;
    }


    public static String[] GetStringArray(ArrayList<String> arr) {
        // declaration and initialise String Array
        String str[] = new String[arr.size()];

        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {

            // Assign each value to String array
            str[j] = arr.get(j);
        }

        String[] returnList = str;

        return str;
    }


    public void addLeadersToEvent(View v) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Available Leaders");

        mBuilder.setMultiChoiceItems(list, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    if (!mUserItems.contains(which)) {
                        mUserItems.add(which);
                    } else {
                        mUserItems.remove(which);
                    }
                }

            }
        });

        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                item = "";
                mItemSelected.setText(item);

                for (int i = 0; i < mUserItems.size(); i++) {
                    if (mUserItems != null) {
                        item = item + " " + list[mUserItems.get(i)] + ",";
                    }

                }

                for (Person p1 : leaders) {
                    String name = p1.getFirstName() + " " + p1.getLastName();
                    if (item.contains(name)) {
                        eventLeaders.add(p1.getPersonID());
                    }

                }

                mItemSelected.setText(item);
            }
        });

        mBuilder.setCancelable(false);

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    public void createNewEvent(View v) {
        final ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please Wait");
        mProgress.show();

        type = eventSpinner.getSelectedItem().toString();
        group = groupSpinner.getSelectedItem().toString();
        boolean idMatch = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date endDateVal = sdf.parse(txtEndDate.getText().toString());
            epochEndDate = endDateVal.getTime();
        } catch (ParseException e) {
            Toast.makeText(this, "Please enter a correctly formatted date", Toast.LENGTH_SHORT).show();
        }


        if (type.equals("Please Select") || group.equals("Please Select") || txtEndDate.getText().toString().equals("")) {
            mProgress.dismiss();
            Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_LONG).show();
        } else {
            String eventType = type;
            String loc = txtLocation.getText().toString();
            String dateVal = Startdate;
            String endDateValue = Long.toString(epochEndDate);
            String groupType = group;
            String id = UUID.randomUUID().toString();
            int availableSpaces;

            //if a leader is creating an event they should
            //automatically be assigned to the event
            eventLeaders.add(createdBy);

            leaderAvailable = isLeaderAvailable(dateVal, endDateValue, createdBy);

            for (String leaderID : eventLeaders) {
                selectedAvailable = isLeaderAvailable(dateVal, endDateValue, leaderID);
                if(selectedAvailable == false){
                    break;
                }
            }

            if (leaderAvailable == true) {
                // Write a message to the database

                //TODO - send email based on leader availability
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Event");
                myRef.keepSynced(true);


                //post to database
                mProgress.setMessage("Adding to Events");
                mProgress.show();

                if(groupType.equals("Scouts") || groupType.equals("Venutes")){
                    availableSpaces = 2 * eventLeaders.size();
                }
                else{
                    availableSpaces = 3 * eventLeaders.size();
                }

                //start uploading....


                EventObj e = new EventObj(id, eventType, loc, dateVal, endDateValue, groupType, createdBy, eventLeaders, availableSpaces);
                myRef.child(id).setValue(e);
                mProgress.dismiss();
                Intent intent = new Intent(getApplicationContext(), EventFragment.class);
                startActivity(intent);
                finish();
            } else if (leaderAvailable == false) {
                mProgress.dismiss();
                Toast.makeText(getApplicationContext(), "A leader you have selected is not available on the dates you have selected", Toast.LENGTH_LONG).show();


            }
        }
    }
}





