
package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.finalyearproject.ui.event.EventFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewEvent extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference personRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private EditText txtGroup;
    private EditText txtLocation;
    private EditText txtAttending;
    private EditText txtSpaces;

    private DatabaseReference eventRef;

    ArrayList<String> leaderNames = new ArrayList<>();
    ArrayList<String> eventLeaders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        txtGroup = findViewById(R.id.txtViewGroup);
        txtLocation = findViewById(R.id.txtViewLocation);
        txtAttending = findViewById(R.id.txtViewLeaders);
        txtSpaces = findViewById(R.id.txtViewSpaces);



        database = FirebaseDatabase.getInstance();
        eventRef = database.getReference("Event");

        personRef = database.getReference("Person");



        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = mAuth.getInstance().getCurrentUser();
                Intent showMap = getIntent();
                final String clickedID = showMap.getStringExtra(MyAdapter.ID_KEY);

                final String currentUserEmail = mUser.getEmail();


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String eventCreatedBy = ds.getValue(EventObj.class).getCreatedBy();
                    String eventStartDate = ds.getValue(EventObj.class).getDate();
                    String eventEndDate = ds.getValue(EventObj.class).getEndDate();
                    String eventGroup = ds.getValue(EventObj.class).getGroup();
                    String eventId = ds.getValue(EventObj.class).getId();
                    int eventSpaces = ds.getValue(EventObj.class).getAvailableSpaces();
                    String eventLocation = ds.getValue(EventObj.class).getLocation();
                    String eventType = ds.getValue(EventObj.class).getType();
                    eventLeaders = ds.getValue(EventObj.class).getEventLeaders();
                    int availableSpaces = ds.getValue(EventObj.class).getAvailableSpaces();
                    EventObj evObj = new EventObj(eventId, eventType, eventLocation, eventStartDate, eventEndDate, eventGroup, eventCreatedBy, eventLeaders, availableSpaces);

                    if(eventId.equals(clickedID)){
                        txtGroup.setText(eventGroup);
                        txtLocation.setText(eventLocation);
                        //txtAttending.setText(leaderNames.toString());
                        txtSpaces.setText("test");
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
