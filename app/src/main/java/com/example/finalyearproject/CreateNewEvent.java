package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.ui.profile.ProfileFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import Objects.EventObj;
import Objects.Leader;

public class CreateNewEvent extends AppCompatActivity {
    Spinner eventSpinner;
    Spinner groupSpinner;
    EditText txtLocation;
    EditText txtStartDate;
    EditText txtEndDate;
    EditText price;
    ImageView btnReturn;
    TextView leaderName;
    DatePickerDialog picker;
    private ProgressDialog mProgress;

    private FirebaseDatabase database;
    private DatabaseReference personRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference eventRef;

    String eventType;
    String group;
    String dateVal;
    String Startdate;
    Date endDate;
    String createdBy;
    double txtPrice;
    String loggedType;
    String item;
    double lng;
    double lat;
    long epochEndDate;
    boolean leaderAvailable = true;
    boolean selectedAvailable = true;
    ArrayList<EventObj> existingEvents = new ArrayList<>();

    ArrayList<Leader> leaders = new ArrayList<>();
    LinkedHashMap<String, String> allListItems = new LinkedHashMap<>();
    LinkedHashMap<String, String> eventLeaders = new LinkedHashMap<>();
    boolean[] checkedItems;

    //The position of the selected item in the dialog box
    ArrayList<Integer> mUserItems = new ArrayList<>();
    String[] list;
    TextView lblSelectedLeaders;
    Calendar myCalendar;
    private final int MY_PERMISSION_REQUEST_SEND_SMS = 0;

    Intent newEvent;
    String type;
    EventObj editEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_new_event);
        newEvent = getIntent();
        type = newEvent.getStringExtra("type");
        editEvent = newEvent.getParcelableExtra("editEvent");
        final String location = newEvent.getStringExtra(viewLocations.locationKey);

        txtEndDate = findViewById(R.id.txtEndDate);
        mProgress = new ProgressDialog(getApplicationContext());
        eventSpinner = (Spinner) findViewById(R.id.txtEvent);
        groupSpinner = findViewById(R.id.txtGroup);
        txtLocation = findViewById(R.id.txtLocation);
        txtStartDate = findViewById(R.id.txtStartDate);
        btnReturn = findViewById(R.id.returnArrow);
        lblSelectedLeaders = findViewById(R.id.leaderSelected);
        price = findViewById(R.id.txtPrice);

        lblSelectedLeaders.setVisibility(View.GONE);

        database = FirebaseDatabase.getInstance();
        personRef = database.getReference("Person").child("Leader");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myCalendar = Calendar.getInstance();

        personRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = mAuth.getInstance().getCurrentUser();
                createdBy = mUser.getUid();

                final String currentUserEmail = mUser.getEmail();


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String personID = ds.getValue(Leader.class).getPersonID();
                    String name = ds.getValue(Leader.class).getName();
                    String DOB = ds.getValue(Leader.class).getDOB();
                    String group = ds.getValue(Leader.class).getGroup();
                    String phone = ds.getValue(Leader.class).getPhone();

                    String email = ds.getValue(Leader.class).getEmail();
                    String vettingDate = ds.getValue(Leader.class).getVettingDate();

                    Leader p = new Leader(personID, name, DOB, group, phone, email, vettingDate);
                    //Adding all leader objects to an arraylist to select leaders to assign to event
                    leaders.add(p);

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

        //Reading all events from the db to check that all leaders are not already assigned to
        //and event on the selected dates
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
                    double price = ds.getValue(EventObj.class).getPrice();
                    String eventType = ds.getValue(EventObj.class).getType();
                    HashMap<String, String> eventLeaders = ds.getValue(EventObj.class).getEventLeaders();
                    ArrayList<String> paymentList = ds.getValue(EventObj.class).getPaymentList();
                    int availableSpaces = ds.getValue(EventObj.class).getAvailableSpaces();
                    double lat = ds.getValue(EventObj.class).getLat();
                    double lng = ds.getValue(EventObj.class).getLat();
                    String approved = ds.getValue(EventObj.class).getApproved();
                    HashMap<String, String> parentReviews = ds.getValue(EventObj.class).getParentReviews();
                    EventObj evObj = new EventObj(eventId, eventType, eventLocation, eventStartDate, eventEndDate, eventGroup, price, eventCreatedBy, eventLeaders, parentReviews, paymentList, availableSpaces, lng, lat, approved);
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


        Startdate = newEvent.getStringExtra(viewLocations.dateKey);
        LatLng latLng = newEvent.getParcelableExtra("location");
        lng = latLng.longitude;
        lat = latLng.latitude;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        dateVal = sdf.format(Float.parseFloat(Startdate));

        txtStartDate.setText(dateVal);
        txtLocation.setText(location);

        Button btnUpdate = findViewById(R.id.btnUpdateEvent);
        btnUpdate.setVisibility(View.GONE);


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
                    //Create leader option list based on the selected group
                    for (Leader p : leaders) {
                        String leaderGroup = p.getGroup();
                        String name = p.getName();
                        allListItems.put(name, leaderGroup);
                    }

                    lblSelectedLeaders.setVisibility(View.VISIBLE);

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

            }
        });


    }

    public boolean isLeaderAvailable(String startDate, String endDate, String leaderId) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date dobDate = new Date(Long.parseLong(endDate));

        boolean idMatch = false;
        LinkedHashMap<Long, ArrayList<String>> dbEventDays = new LinkedHashMap<>();
        ArrayList<Long> newEventDays = new ArrayList<>();
        long startDateLong = Long.parseLong(startDate);
        long endDateLong = Long.parseLong(endDate);

        for (EventObj e : existingEvents) {
            long dbStartDate = Long.parseLong(e.getDate());
            long dbEndDate = Long.parseLong(e.getEndDate());
            String dbID = e.getCreatedBy();
            HashMap<String, String> eventLeaders = e.getEventLeaders();

            boolean dateReached = false;


            String uID = e.getCreatedBy();
            ArrayList<String> leaderIdKeyset = new ArrayList<String>(eventLeaders.keySet());

            do {
                //add all the days in each existing event in the database to an arraylist
                if (dbStartDate == dbEndDate) {
                    dbEventDays.put(dbStartDate, leaderIdKeyset);
                    dbStartDate = dbStartDate + 86400000L;
                    dateReached = true;
                } else {
                    dbEventDays.put(dbStartDate, leaderIdKeyset);
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
        lblSelectedLeaders.setText("");
        eventLeaders.clear();
        mUserItems.clear();
        item = "";
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Available Leaders");

        mBuilder.setMultiChoiceItems(list, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                //add leader name to selected leaders if not already in list
                //else remove leader if name is unchecked
                //TODO - this is crashing with an index out of bounds exception when adding and removing names
                if (isChecked) {
                    if (!mUserItems.contains(which)) {
                        mUserItems.add(which);
                    }
                }

            }
        });

        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                for (int i = 0; i < mUserItems.size(); i++) {
                    if (!mUserItems.isEmpty()) {
                        item = item + " " + list[mUserItems.get(i)] + ",";
                    }

                }

                for (Leader p1 : leaders) {
                    String name = p1.getName();
                    if (item.contains(name)) {
                        eventLeaders.put(p1.getPersonID(), "pending");
                    }
                }

                lblSelectedLeaders.setText(item);
            }
        });

        mBuilder.setCancelable(true);

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    public void createNewEvent(View v) {
        final ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please Wait");
        mProgress.show();

        eventType = eventSpinner.getSelectedItem().toString();
        group = groupSpinner.getSelectedItem().toString();
        boolean idMatch = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        //if a leader is creating an event they should
        //automatically be assigned to the event
        eventLeaders.put(createdBy, "Approved");

        if (eventLeaders.size() >= 2) {
            try {
                Date endDateVal = sdf.parse(txtEndDate.getText().toString());
                epochEndDate = endDateVal.getTime();
                if (eventType.equals("Please Select") || group.equals("Please Select") || txtEndDate.getText().toString().equals("")) {
                    mProgress.dismiss();
                    Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_LONG).show();
                } else {
                    String eventType = this.eventType;
                    String loc = txtLocation.getText().toString();
                    String dateVal = Startdate;
                    String endDateValue = Long.toString(epochEndDate);
                    String groupType = group;
                    String id = UUID.randomUUID().toString();
                    txtPrice = Double.parseDouble(price.getText().toString());
                    int availableSpaces = 0;

                    if (epochEndDate < Long.parseLong(dateVal)) {
                        mProgress.dismiss();
                        Toast.makeText(this, "The end date cannot be before the start date", Toast.LENGTH_LONG).show();
                    } else {


                        leaderAvailable = isLeaderAvailable(dateVal, endDateValue, createdBy);

                        for (int i = 0; i < eventLeaders.size(); i++) {
                            String leaderID = eventLeaders.keySet().toArray()[i].toString();
                            selectedAvailable = isLeaderAvailable(dateVal, endDateValue, leaderID);
                            if (selectedAvailable == false) {
                                break;
                            }
                        }

                        if (leaderAvailable == true) {
                            // Write a message to the database

                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Event");
                            myRef.keepSynced(true);

                            //check if the permission is not granted
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                //Check if the user has not denied the request
                                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage("0858402059", null, "sms message", null, null);
                                } else {
                                    //Ask the user for permission
                                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SEND_SMS);
                                }
                                try {

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            //post to database
                            mProgress.setMessage("Adding to Events");
                            mProgress.show();

                            if (groupType.equals("Scouts") || groupType.equals("Ventures")) {
                                availableSpaces = 8 * eventLeaders.size();
                            } else if (groupType.equals("Beavers") || groupType.equals("Cubs")) {
                                availableSpaces = 5 * eventLeaders.size();
                            } else if (groupType.equals("Rovers")) {
                                //Maximum number of members in any group
                                //As Rovers are adults there is no set ratio
                                availableSpaces = 30;
                            }

                            ArrayList<String> paymentList = new ArrayList<>();
                           HashMap<String, String> parentReviews = new HashMap<>();
                            parentReviews.put("Empty", "Empty");
                            paymentList.add("Empty");
                            //start uploading....
                            EventObj e = new EventObj(id, eventType, loc, dateVal, endDateValue, groupType, txtPrice, createdBy, eventLeaders, parentReviews, paymentList, availableSpaces, lng, lat, "pending");
                            myRef.child(id).setValue(e);
                            mProgress.dismiss();
                            Intent intent = new Intent(getApplicationContext(), ProfileFragment.class);
                            startActivity(intent);
                            finish();
                        } else if (leaderAvailable == false) {
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "A leader you have selected is not available on the dates you have selected", Toast.LENGTH_LONG).show();


                        }
                    }

                }
            } catch (ParseException e) {
                mProgress.dismiss();
                Toast.makeText(this, "Please enter a correctly formatted date", Toast.LENGTH_SHORT).show();
            }
        } else {
            mProgress.dismiss();
            Toast.makeText(this, "A minimum of two leaders are required to run an event", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_SEND_SMS: {
                //check whether the length of grantResults is greater than 0 and is equal to PERMISSIONS_GRANTED
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("0852393910", null, "sms message", null, null);
                } else {
                    Toast.makeText(this, "Please allow the app to send SMS text messages", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}





