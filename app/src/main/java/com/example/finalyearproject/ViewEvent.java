
package com.example.finalyearproject;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import Objects.EventObj;
import Objects.Leader;
import Objects.Parent;
import Objects.PaymentHistory;
import Objects.Review;


public class ViewEvent extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private EditText txtGroup;
    private EditText txtLocation;
    private EditText txtAttending;
    private EditText txtSpaces;
    private EditText txtPrice;
    private Button btnGoing;
    private Button btnNotGoing;
    private Button btnPay;
    private Button btnReview;
    private FloatingActionButton btnEventEdit;
    private TextView txtMsg;

    private String eventCreatedBy;

    ArrayList<String> leaderNames = new ArrayList<>();
    ArrayList<String> paymentList = new ArrayList<>();
    ArrayList<Review> reviewList = new ArrayList<>();
    private String eventId;
    private double priceVal;
    HashMap<String, String> eventLeaders = new LinkedHashMap<>();
    LinkedHashMap<String, String> parentChildIds = new LinkedHashMap<>();
    GoogleMap mMap;
    double lng;
    double lat;
    String eventLocation;
    HashMap<String, String> parentReviews = new HashMap<>();
    FirebaseDatabase mDatabase;
    FirebaseDatabase reviewDatabase;
    DatabaseReference mRef;
    DatabaseReference reviewRef;
    String userType = null;
    String eventGroup;
    String name;
    int txtAvailableSpaces;
    boolean onLeaderList = false;
    private String approved;
    String personId;
    EventObj event;
    private PaymentsClient paymentsClient;
    private static final int paypal_request_code = 7171;
    public static final String paypalClientID = "AWxCqTIpH--tM8IgOK9bBxAcM_eaG-3stEMSKBUrE0wVgycoGkbjPBFKupgfIZPXeZtlgF5AmmjEgdPZ";
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(paypalClientID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_view_event);
        txtGroup = findViewById(R.id.txtViewGroup);
        txtLocation = findViewById(R.id.txtViewLocation);
        txtAttending = findViewById(R.id.txtViewLeaders);
        txtSpaces = findViewById(R.id.txtViewSpaces);
        txtPrice = findViewById(R.id.txtViewPrice);
        btnGoing = findViewById(R.id.btnGoing);
        btnNotGoing = findViewById(R.id.btnNo);
        btnPay = findViewById(R.id.btnMakePayment);
        btnEventEdit = findViewById(R.id.btnEventEdit);
        btnReview = findViewById(R.id.btnOpenReview);
        txtMsg = findViewById(R.id.approvalMessage);

        btnGoing.setVisibility(View.INVISIBLE);
        btnNotGoing.setVisibility(View.INVISIBLE);
        btnPay.setVisibility(View.INVISIBLE);
        btnReview.setVisibility(View.INVISIBLE);
        txtMsg.setVisibility(View.INVISIBLE);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.viewMap);
        mapFragment.getMapAsync(ViewEvent.this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        reviewDatabase = FirebaseDatabase.getInstance();

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        final boolean[] found = {false};
        final String[] ID = new String[1];
        final String[] childID = new String[1];
        final String[] email = new String[1];
        final String[] personType = new String[1];
        final String[] group = new String[1];

        mRef = mDatabase.getReference("Event");
        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = mAuth.getInstance().getCurrentUser();

                Intent intent = getIntent();
                final String clickedID = intent.getStringExtra("ID_KEY");

                final String currentUserEmail = mUser.getEmail();


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    eventCreatedBy = ds.getValue(EventObj.class).getCreatedBy();
                    String eventStartDate = ds.getValue(EventObj.class).getDate();
                    String eventEndDate = ds.getValue(EventObj.class).getEndDate();
                    eventGroup = ds.getValue(EventObj.class).getGroup();
                    eventId = ds.getValue(EventObj.class).getId();
                    txtAvailableSpaces = ds.getValue(EventObj.class).getAvailableSpaces();
                    eventLocation = ds.getValue(EventObj.class).getLocation();
                    String eventType = ds.getValue(EventObj.class).getType();
                    eventLeaders = ds.getValue(EventObj.class).getEventLeaders();
                    paymentList = ds.getValue(EventObj.class).getPaymentList();
                    priceVal = ds.getValue(EventObj.class).getPrice();
                    int availableSpaces = ds.getValue(EventObj.class).getAvailableSpaces();
                    approved = ds.getValue(EventObj.class).getApproved();


                    LinkedHashMap<String, String> leadersAttending = new LinkedHashMap<>(eventLeaders);

                    for (int i = 0; i < leadersAttending.size(); i++) {
                        String id = leadersAttending.keySet().toArray()[i].toString();
                        String approval = leadersAttending.values().toArray()[i].toString();
                        if (id.equals(mUser.getUid()) && approval.equals("pending")) {
                            onLeaderList = true;
                            break;
                        }
                    }


                    if (eventId.equals(clickedID)) {

                        String userId = mUser.getUid();
                        if (!eventCreatedBy.equals(mUser.getUid())) {
                            btnEventEdit.hide();
                        }
                        String txtPriceParsed = "€" + String.valueOf(priceVal);
                        txtGroup.setText(eventGroup);
                        txtLocation.setText(eventLocation);
                        txtSpaces.setText(String.valueOf(availableSpaces));
                        txtPrice.setText(txtPriceParsed);
                        lat = ds.getValue(EventObj.class).getLat();
                        lng = ds.getValue(EventObj.class).getLng();
                        parentReviews = ds.getValue(EventObj.class).getParentReviews();

                        event = new EventObj(eventId, eventType, eventLocation, eventStartDate, eventEndDate, eventGroup, priceVal, eventCreatedBy, eventLeaders,parentReviews, paymentList, availableSpaces, lng, lat, approved);

                        MapsInitializer.initialize(getApplicationContext());
                        mMap = googleMap;
                        LatLng position = new LatLng(lng, lat);
                        MarkerOptions markerOptions = new MarkerOptions();

                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f));
                        markerOptions.position(position);

                        mMap.animateCamera(CameraUpdateFactory.newLatLng(position));

                        // Placing a marker on the event location
                        mMap.addMarker(markerOptions);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        List<String> typeList = Arrays.asList("Leader", "Parent");
        final boolean[] foundUser = {false};

        for (String type : typeList) {
            final String tempType = type;
            mRef = mDatabase.getReference("Person").child(type);
            mRef.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUser = mAuth.getInstance().getCurrentUser();

                    final String currentUserEmail = mUser.getEmail();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (tempType.equals("Leader")) {
                            ID[0] = ds.getValue(Leader.class).getPersonID();
                            email[0] = ds.getValue(Leader.class).getEmail();
                            name = ds.getValue(Leader.class).getName();
                            group[0] = ds.getValue(Leader.class).getGroup();

                            for (String id : eventLeaders.keySet()) {
                                if (ID[0].equals(id)) {
                                    leaderNames.add(name);
                                }
                            }

                            String leaderNamesDisplay = leaderNames.toString();
                            leaderNamesDisplay = leaderNamesDisplay.substring(1, leaderNamesDisplay.length() - 1);
                            txtAttending.setText(leaderNamesDisplay);

                        } else if (tempType.equals("Parent")) {
                            ID[0] = ds.getValue(Parent.class).getParentId();
                            email[0] = ds.getValue(Parent.class).getEmail();
                            name = ds.getValue(Parent.class).getName();
                            personType[0] = tempType;
                            group[0] = ds.getValue(Parent.class).getGroup();
                            childID[0] = ds.getValue(Parent.class).getChildId();
                            parentChildIds.put(email[0], childID[0]);
                        }


                        if (currentUserEmail.equals(email[0])) {
                            int i = 0;
                            if (tempType.equals("Leader")) {
                                if (onLeaderList) {
                                    for (String id : eventLeaders.keySet()) {
                                        if (id.equals(ID[0])) {
                                            String approval = eventLeaders.values().toArray()[i].toString();
                                            if (approval.equals("pending")) {
                                                btnGoing.setVisibility(View.VISIBLE);
                                                btnNotGoing.setVisibility(View.VISIBLE);
                                            } else if (approval.equals("Approved")) {
                                                txtMsg.setText("You have indicated that you are going on this event");
                                                txtMsg.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        i++;
                                    }
                                }
                            } else if (tempType.equals("Parent")) {
                                Date today = Calendar.getInstance().getTime();
                                Long todayLong = Long.parseLong(String.valueOf(today.getTime()));


                                if (Long.parseLong(event.getEndDate()) < todayLong) {
                                    if (paymentList.contains(childID[0])) {
                                        for(String parentId : parentReviews.keySet()){
                                            if(parentId.equals(mUser.getUid())){
                                                btnReview.setVisibility(View.INVISIBLE);
                                            }
                                            else{
                                                btnReview.setVisibility(View.VISIBLE);
                                            }
                                        }

                                    }

                                } else {
                                    if (mUser.getUid().equals(ID[0]))
                                        if (paymentList.contains(childID[0])) {
                                            btnPay.setVisibility(View.GONE);
                                            txtMsg.setText("You have paid for this event");
                                            txtMsg.setVisibility(View.VISIBLE);
                                        } else if (eventGroup.equals(group[0])/* && readyToPay*/) {

                                            btnPay.setVisibility(View.VISIBLE);
                                        }

                                }
                            }

                            foundUser[0] = true;
                            break;
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if (foundUser[0] == true) {
                break;
            }
        }

        btnEventEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(ViewEvent.this, CreateNewEvent.class);
                editIntent.putExtra("editEvent", (Parcelable) event);
                editIntent.putExtra("type", "Edit");
                startActivity(editIntent);
            }
        });

        btnGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef = mDatabase.getReference("Event");
                mRef.child(eventId).child("eventLeaders").child(mUser.getUid()).setValue("Approved");
                btnGoing.setVisibility(View.INVISIBLE);
                btnNotGoing.setVisibility(View.INVISIBLE);
                txtMsg.setText("You have indicated that you are going on this event");
                txtMsg.setVisibility(View.VISIBLE);
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
                                mRef = mDatabase.getReference("Event");
                                mRef.child(eventId).child("eventLeaders").child(mUser.getUid()).removeValue();
                                btnGoing.setVisibility(View.INVISIBLE);
                                btnNotGoing.setVisibility(View.INVISIBLE);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
    }

    public void makePayment(View v) {
        String amount = txtPrice.getText().toString();
        amount = amount.replace("€", "");

        PayPalPayment payment = new PayPalPayment(BigDecimal.valueOf(priceVal), "EUR", "Test Payment", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, paypal_request_code);
    }

    public void openReview(View v) {
        String eventID = eventId;
        String parentId = mUser.getUid();

        Intent openReview = new Intent(this, AddReview.class);
        openReview.putExtra("eventId", eventID);
        openReview.putExtra("parentId", parentId);
        openReview.putExtra("createdBy", eventCreatedBy);
        startActivity(openReview);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == paypal_request_code) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Your Payment was successful", Toast.LENGTH_LONG).show();
                String parentEmail = mUser.getEmail();
                String childId = null;
                for (int i = 0; i < parentChildIds.keySet().size(); i++) {
                    String key = parentChildIds.keySet().toArray()[i].toString();
                    childId = parentChildIds.values().toArray()[i].toString();
                    if (key.equals(parentEmail)) {
                        for (String value : paymentList) {
                            if (value.equals("Empty")) {
                                //Overwrite default value if first payment
                                paymentList.set(0, childId);
                            } else {
                                paymentList.add(childId);
                            }
                        }

                    }
                }

                txtAvailableSpaces = txtAvailableSpaces - 1;
                mRef = mDatabase.getReference("Event");
                mRef.child(eventId).child("availableSpaces").setValue(txtAvailableSpaces);
                mRef.child(eventId).child("paymentList").setValue(paymentList);

                String paymentID = UUID.randomUUID().toString();
                PaymentHistory ph = new PaymentHistory(paymentID, priceVal, eventId, mUser.getUid(), childId, eventGroup, eventLocation);
                mRef = mDatabase.getReference("Payment History");
                mRef.child(paymentID).setValue(ph);

            } else {
                Toast.makeText(this, "Your Payment was not successful", Toast.LENGTH_LONG).show();

            }
        }
    }
}

