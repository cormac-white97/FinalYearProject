
package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalyearproject.ui.event.EventFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ViewEvent extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseDatabase database;
    private DatabaseReference personRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private EditText txtGroup;
    private EditText txtLocation;
    private EditText txtAttending;
    private EditText txtSpaces;
    private Button btnGoing;
    private Button btnNotGoing;

    private DatabaseReference eventRef;

    ArrayList<String> leaderNames = new ArrayList<>();
    private String eventId;
    HashMap<String, String> eventLeaders = new LinkedHashMap<>();
    GoogleMap mMap;
    double lng;
    double lat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        txtGroup = findViewById(R.id.txtViewGroup);
        txtLocation = findViewById(R.id.txtViewLocation);
        txtAttending = findViewById(R.id.txtViewLeaders);
        txtSpaces = findViewById(R.id.txtViewSpaces);
        btnGoing = findViewById(R.id.btnGoing);
        btnNotGoing = findViewById(R.id.btnNo);
        btnGoing.setVisibility(View.INVISIBLE);
        btnNotGoing.setVisibility(View.INVISIBLE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.viewMap);
        mapFragment.getMapAsync(ViewEvent.this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        database = FirebaseDatabase.getInstance();
        eventRef = database.getReference("Event");
        personRef = database.getReference("Leader");

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = mAuth.getInstance().getCurrentUser();

                Intent intent = getIntent();
                final String clickedID = intent.getStringExtra(MyAdapter.ID_KEY);

                final String currentUserEmail = mUser.getEmail();


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String eventCreatedBy = ds.getValue(EventObj.class).getCreatedBy();
                    String eventStartDate = ds.getValue(EventObj.class).getDate();
                    String eventEndDate = ds.getValue(EventObj.class).getEndDate();
                    String eventGroup = ds.getValue(EventObj.class).getGroup();
                   eventId = ds.getValue(EventObj.class).getId();
                    int eventSpaces = ds.getValue(EventObj.class).getAvailableSpaces();
                    String eventLocation = ds.getValue(EventObj.class).getLocation();
                    String eventType = ds.getValue(EventObj.class).getType();
                    eventLeaders = ds.getValue(EventObj.class).getEventLeaders();
                    int availableSpaces = ds.getValue(EventObj.class).getAvailableSpaces();

                    LinkedHashMap<String, String> leadersAttending =new LinkedHashMap<>(eventLeaders);

                    for(int i = 0; i < leadersAttending.size(); i++){
                        String id = leadersAttending.keySet().toArray()[i].toString();
                        String approval = leadersAttending.values().toArray()[i].toString();
                        if (id.equals(mUser.getUid()) && approval.equals("pending")) {
                            btnGoing.setVisibility(View.VISIBLE);
                            btnNotGoing.setVisibility(View.VISIBLE);
                            break;
                        }
                    }


                    if (eventId.equals(clickedID)) {
                        txtGroup.setText(eventGroup);
                        txtLocation.setText(eventLocation);
                        //txtAttending.setText(leaderNames.toString());
                        txtSpaces.setText(String.valueOf(availableSpaces));
                        lat = ds.getValue(EventObj.class).getLat();
                        lng = ds.getValue(EventObj.class).getLng();


                        MapsInitializer.initialize(getApplicationContext());
                        mMap = googleMap;
                        LatLng position = new LatLng(lng, lat);
                        MarkerOptions markerOptions = new MarkerOptions();

                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f));
                        markerOptions.position(position);

                        mMap.animateCamera(CameraUpdateFactory.newLatLng(position));

                        // Placing a marker on the touched position
                        mMap.addMarker(markerOptions);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventRef.child(eventId).child("eventLeaders").child(mUser.getUid()).setValue("Approved");
                btnGoing.setVisibility(View.INVISIBLE);
                btnNotGoing.setVisibility(View.INVISIBLE);
            }
        });

        btnNotGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewEvent.this)
                        .setTitle("Warning!")
                        .setMessage("You are about to indicate you are not going on this event?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                eventRef.child(eventId).child("eventLeaders").child(mUser.getUid()).removeValue();
                                btnGoing.setVisibility(View.INVISIBLE);
                                btnNotGoing.setVisibility(View.INVISIBLE);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
    }


}
