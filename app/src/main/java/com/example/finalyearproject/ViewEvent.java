
package com.example.finalyearproject;

import com.paypal.android.sdk.payments.PayPalConfiguration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import Objects.PaymentHistory;


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
    private TextView txtMsg;

    ArrayList<String> leaderNames = new ArrayList<>();
    ArrayList<String> paymentList = new ArrayList<>();
    private String eventId;
    private double priceVal;
    HashMap<String, String> eventLeaders = new LinkedHashMap<>();
    LinkedHashMap<String, String> parentChildIds = new LinkedHashMap<>();
    GoogleMap mMap;
    double lng;
    double lat;
    String eventLocation;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    String userType = null;
    String eventGroup;
    String name;
    int txtAvailableSpaces;
    boolean onLeaderList = false;
    private boolean approved = false;
    String personId;
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
        txtMsg = findViewById(R.id.approvalMessage);

        btnGoing.setVisibility(View.INVISIBLE);
        btnNotGoing.setVisibility(View.INVISIBLE);
        btnPay.setVisibility(View.INVISIBLE);
        txtMsg.setVisibility(View.INVISIBLE);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.viewMap);
        mapFragment.getMapAsync(ViewEvent.this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        final boolean[] found = {false};
        final String[] leaderID = new String[1];
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
                final String clickedID = intent.getStringExtra(MyAdapter.ID_KEY);

                final String currentUserEmail = mUser.getEmail();


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String eventCreatedBy = ds.getValue(EventObj.class).getCreatedBy();
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
                        String txtPriceParsed = "€" + String.valueOf(priceVal);
                        txtGroup.setText(eventGroup);
                        txtLocation.setText(eventLocation);
                        txtSpaces.setText(String.valueOf(availableSpaces));
                        txtPrice.setText(txtPriceParsed);
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

        List<String> typeList = Arrays.asList("Leader", "Parent", "Member");
        final boolean[] foundUser = {false};

        for (String type : typeList) {
            final String tempType = type;
            mRef = mDatabase.getReference("Person").child(type);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUser = mAuth.getInstance().getCurrentUser();

                    final String currentUserEmail = mUser.getEmail();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (tempType.equals("Leader")) {
                            leaderID[0] = ds.getValue(Leader.class).getPersonID();
                            email[0] = ds.getValue(Leader.class).getEmail();
                            name = ds.getValue(Leader.class).getName();
                            personType[0] = ds.getValue(Leader.class).getPersonType();
                            group[0] = ds.getValue(Leader.class).getGroup();

                            for (String id : eventLeaders.keySet()) {
                                if (leaderID[0].equals(id)) {
                                    leaderNames.add(name);
                                }
                            }

                            String leaderNamesDisplay = leaderNames.toString();
                            leaderNamesDisplay = leaderNamesDisplay.substring(1, leaderNamesDisplay.length() - 1);
                            txtAttending.setText(leaderNamesDisplay);

                        } else if (tempType.equals("Parent")) {
                            leaderID[0] = ds.getValue(Parent.class).getParentId();
                            email[0] = ds.getValue(Parent.class).getEmail();
                            name = ds.getValue(Parent.class).getName();
                            personType[0] = tempType;
                            group[0] = ds.getValue(Parent.class).getGroup();
                            childID[0] = ds.getValue(Parent.class).getChildId();
                            parentChildIds.put(email[0], childID[0]);
                        } else if (tempType.equals("Member")) {
                            email[0] = ds.getValue(Member.class).getEmail();
                            name = ds.getValue(Member.class).getName();
                            personType[0] = tempType;
                            group[0] = ds.getValue(Member.class).getGroup();
                        }


                        if (currentUserEmail.equals(email[0])) {
                            int i = 0;
                            if (tempType.equals("Leader")) {
                                if (onLeaderList) {
                                    for (String id : eventLeaders.keySet()) {
                                        if (id.equals(leaderID[0])) {
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
                                if (eventGroup.equals(group[0])/* && readyToPay*/) {

                                    btnPay.setVisibility(View.VISIBLE);
                                }

                            } else if (tempType.equals("Member")) {
                                Toast.makeText(ViewEvent.this, "Member", Toast.LENGTH_LONG).show();
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

