package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.finalyearproject.ui.event.EventFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class CreateNewEvent extends AppCompatActivity {
    Spinner eventSpinner;
    Spinner groupSpinner;
    EditText txtLocation;
    EditText txtStartDate;
    ImageView btnReturn;
    String type;
    String group;
    String dateVal;
    String date;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);
        Intent newEvent = getIntent();
        String location = newEvent.getStringExtra(viewLocations.locationKey);
        date = newEvent.getStringExtra(viewLocations.dateKey);
        mProgress = new ProgressDialog(this);
        eventSpinner = (Spinner) findViewById(R.id.txtEvent);
        groupSpinner = findViewById(R.id.txtGroup);
        txtLocation = findViewById(R.id.txtLocation);
        txtStartDate = findViewById(R.id.txtStartDate);
        btnReturn = findViewById(R.id.returnArrow);

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

        dateVal = sdf.format(Float.parseFloat(date));

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

        if (type.equals("Please Select") || group.equals("Please Select")) {
            Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_LONG).show();
        } else {
            // Write a message to the database
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Event");
            myRef.keepSynced(true);

            //post to database
            mProgress.setMessage("Adding to Events");
            mProgress.show();
            String eventType = type;
            String loc = txtLocation.getText().toString();
            String dateVal = date;
            String groupType = group;
            //start uploading....
            EventObj e = new EventObj(eventType, loc, dateVal, groupType);
            myRef.child(dateVal).setValue(e);
            mProgress.dismiss();
            Intent intent = new Intent(this, EventFragment.class);
            startActivity(intent);
            finish();
        }
    }


}

