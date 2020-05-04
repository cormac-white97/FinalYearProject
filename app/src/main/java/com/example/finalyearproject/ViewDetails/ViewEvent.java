
package com.example.finalyearproject.ViewDetails;

import com.example.finalyearproject.CreationClasses.AddReview;
import com.example.finalyearproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Map;
import java.util.UUID;

import com.example.finalyearproject.Objects.EventObj;
import com.example.finalyearproject.Objects.Leader;
import com.example.finalyearproject.Objects.Parent;
import com.example.finalyearproject.Objects.PaymentHistory;
import com.example.finalyearproject.Objects.Review;


public class ViewEvent extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private EditText txtGroup;
    private EditText txtLocation;
    private EditText txtAttending;
    private EditText txtSpaces;
    private EditText txtPrice;

    private FloatingActionButton btnSave;
    private FloatingActionButton btnEventEdit;

    private Button btnGoing;
    private Button btnNotGoing;
    private Button btnPay;
    private Button btnCreateReview;
    private Button makeAvailable;
    private Button viewReviews;
    private Button viewPayments;
    private ImageView deleteButton;
    private ImageView btnBack;

    private TextView txtMsg;
    private String eventCreatedBy;

    ArrayList<String> leaderNames = new ArrayList<>();
    ArrayList<String> editLeaderNames = new ArrayList<>();
    ArrayList<Parent> parents = new ArrayList<>();

    ArrayList<String> paymentList = new ArrayList<>();
    ArrayList<Review> reviewList = new ArrayList<>();
    private String eventId;
    private double priceVal;
    HashMap<String, String> eventLeaders = new LinkedHashMap<>();
    LinkedHashMap<String, String> parentChildIds = new LinkedHashMap<>();

    boolean[] checkedItems;
    String item;
    //The position of the selected item in the dialog box
    ArrayList<Integer> mUserItems = new ArrayList<>();
    String[] list;
    ArrayList<Leader> leaders = new ArrayList<>();

    ArrayList<String> newLeaders = new ArrayList<String>();

    private DatabaseReference personRef;

    private final int MY_PERMISSION_REQUEST_SEND_SMS = 0;

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
        btnCreateReview = findViewById(R.id.btnCreateEventReviews);
        txtMsg = findViewById(R.id.approvalMessage);
        btnSave = findViewById(R.id.btnSave);
        makeAvailable = findViewById(R.id.btnMakeAvailable);
        viewPayments = findViewById(R.id.btnViewPayments);
        viewReviews = findViewById(R.id.btnViewReview);
        deleteButton = findViewById(R.id.deleteEvent);
        btnBack = findViewById(R.id.eventReturnArrow);


        btnGoing.setVisibility(View.INVISIBLE);
        btnNotGoing.setVisibility(View.INVISIBLE);
        btnPay.setVisibility(View.INVISIBLE);
        btnCreateReview.setVisibility(View.GONE);
        makeAvailable.setVisibility(View.INVISIBLE);
        viewReviews.setVisibility(View.GONE);
        txtMsg.setVisibility(View.INVISIBLE);
        viewPayments.setVisibility(View.INVISIBLE);
        btnSave.hide();
        deleteButton.setVisibility(View.INVISIBLE);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.viewMap);
        mapFragment.getMapAsync(ViewEvent.this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        reviewDatabase = FirebaseDatabase.getInstance();

        personRef = mDatabase.getReference("Person").child("Leader");
        mAuth = FirebaseAuth.getInstance();

        personRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = mAuth.getInstance().getCurrentUser();

                final String currentUserEmail = mUser.getEmail();


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String personID = ds.getValue(Leader.class).getLeaderId();
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
                    String eventStartDate = ds.getValue(EventObj.class).getStartDate();
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
                    for (Leader l : leaders) {
                        if (l.getGroup().equals(eventGroup)) {
                            if (!editLeaderNames.contains(l.getName())) {
                                if (!eventLeaders.containsKey(l.getLeaderId()))
                                    editLeaderNames.add(l.getName());
                            }
                        }

                    }

                    if (eventId.equals(clickedID)) {

                        String userId = mUser.getUid();
                        if (!eventCreatedBy.equals(mUser.getUid()) || approved.equals("approved")) {
                            btnEventEdit.hide();
                            makeAvailable.setVisibility(View.INVISIBLE);
                        } else if (eventCreatedBy.equals(mUser.getUid()) && approved.equals("pending")) {
                            deleteButton.setVisibility(View.VISIBLE);
                            makeAvailable.setVisibility(View.VISIBLE);
                        }
                        String txtPriceParsed = "€" + String.valueOf(priceVal);
                        txtGroup.setText(eventGroup);
                        txtLocation.setText(eventLocation);
                        txtSpaces.setText(String.valueOf(availableSpaces));
                        txtPrice.setText(txtPriceParsed);
                        lat = ds.getValue(EventObj.class).getLat();
                        lng = ds.getValue(EventObj.class).getLng();
                        parentReviews = ds.getValue(EventObj.class).getParentReviews();

                        event = new EventObj(eventId, eventType, eventLocation, eventStartDate, eventEndDate, eventGroup, priceVal, eventCreatedBy, eventLeaders, parentReviews, paymentList, availableSpaces, lng, lat, approved);

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
                    int j = 0;
                    final String currentUserEmail = mUser.getEmail();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (tempType.equals("Leader")) {
                            ID[0] = ds.getValue(Leader.class).getLeaderId();
                            email[0] = ds.getValue(Leader.class).getEmail();
                            name = ds.getValue(Leader.class).getName();
                            group[0] = ds.getValue(Leader.class).getGroup();
                            final int[] i = {0};

                            for (String id : eventLeaders.keySet()) {
                                if (ID[0].equals(id)) {
                                    String approval = eventLeaders.values().toArray()[i[0]].toString();

                                    if (approval.equals("Approved")) {
                                        leaderNames.add(name);

                                    }
                                }
                                i[0]++;
                            }

                            String leaderNamesDisplay = leaderNames.toString();
                            leaderNamesDisplay = leaderNamesDisplay.substring(1, leaderNamesDisplay.length() - 1);
                            txtAttending.setText(leaderNamesDisplay);



                        } else if (tempType.equals("Parent")) {

                            String phone = ds.getValue(Parent.class).getPhone();
                            String childId = ds.getValue(Parent.class).getChildId();

                            ID[0] = ds.getValue(Parent.class).getParentId();
                            email[0] = ds.getValue(Parent.class).getEmail();
                            name = ds.getValue(Parent.class).getName();
                            personType[0] = tempType;
                            group[0] = ds.getValue(Parent.class).getGroup();
                            childID[0] = ds.getValue(Parent.class).getChildId();
                            parentChildIds.put(email[0], childID[0]);

                            Parent p = new Parent(ID[0], name, phone, email[0], childId, group[0]);
                            parents.add(p);
                        }


                        if (currentUserEmail.equals(email[0])) {

                            if (tempType.equals("Leader")) {
                                for (String id : eventLeaders.keySet()) {
                                    if (id.equals(ID[0])) {

                                        String approval = eventLeaders.values().toArray()[j].toString();
                                        Date today = Calendar.getInstance().getTime();
                                        Long todayLong = Long.parseLong(String.valueOf(today.getTime()));

                                        if (Long.parseLong(event.getEndDate()) < todayLong) {
                                            if (eventLeaders.containsKey(mUser.getUid())) {
                                                viewReviews.setVisibility(View.VISIBLE);
                                                makeAvailable.setVisibility(View.GONE);
                                                btnEventEdit.hide();
                                                deleteButton.setVisibility(View.GONE);


                                            }

                                        } else if (approval.equals("pending")) {
                                            btnGoing.setVisibility(View.VISIBLE);
                                            btnNotGoing.setVisibility(View.VISIBLE);
                                        } else if (approval.equals("Approved")) {
                                            if (!eventCreatedBy.equals(mUser.getUid())) {
                                                txtMsg.setText("You are going on this event.");
                                                txtMsg.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        if(approved.equals("approved") && Long.parseLong(event.getEndDate()) > todayLong){
                                            viewPayments.setVisibility(View.VISIBLE);
                                            txtMsg.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                    j++;
                                }

                            } else if (tempType.equals("Parent")) {
                                Date today = Calendar.getInstance().getTime();
                                Long todayLong = Long.parseLong(String.valueOf(today.getTime()));

                                if (Long.parseLong(event.getEndDate()) < todayLong) {
                                    if (paymentList.contains(childID[0])) {
                                        if (parentReviews.keySet().contains(mUser.getUid())) {
                                            btnCreateReview.setVisibility(View.GONE);
                                            viewReviews.setVisibility(View.VISIBLE);
                                            break;
                                        } else {
                                            btnCreateReview.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        viewReviews.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    if (mUser.getUid().equals(ID[0]))
                                        if (paymentList.contains(childID[0])) {
                                            btnPay.setVisibility(View.GONE);
                                            txtMsg.setText("You have paid for this event");
                                            txtMsg.setVisibility(View.VISIBLE);
                                        } else if (eventGroup.equals(group[0]) && txtAvailableSpaces > 0) {
                                            btnPay.setVisibility(View.VISIBLE);
                                        }

                                }
                            }

                            foundUser[0] = true;
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        btnEventEdit.setOnClickListener(new View.OnClickListener() {
            ArrayList<String> listItemsByGroup = new ArrayList<>();

            @Override
            public void onClick(View v) {
                btnEventEdit.hide();
                btnSave.show();

                txtPrice.setTextColor(Color.BLACK);
                txtPrice.setFocusableInTouchMode(true);
                txtPrice.setEnabled(true);

                String price = txtPrice.getText().toString();
                price = price.substring(1, price.length() - 1);
                txtPrice.setText(price);
                txtAttending.setTextColor(Color.BLACK);

                txtAttending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list = GetStringArray(editLeaderNames);

                        eventLeaders.clear();
                        mUserItems.clear();
                        item = "";
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ViewEvent.this);
                        mBuilder.setTitle("Available Leaders");

                        mBuilder.setMultiChoiceItems(list, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                //add leader name to selected leaders if not already in list
                                //else remove leader if name is unchecked
                                if (isChecked) {
                                    if (!mUserItems.contains(position)) {
                                        mUserItems.add(position);
                                    }
                                } else {
                                    mUserItems.remove((Integer.valueOf(position)));
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
                                        newLeaders.add(p1.getLeaderId());
                                        leaderNames.add(name);
                                    }
                                }

                                String leaderNamesDisplay = leaderNames.toString();
                                leaderNamesDisplay = leaderNamesDisplay.substring(1, leaderNamesDisplay.length() - 1);
                                txtAttending.setText(leaderNamesDisplay);
                            }
                        });

                        mBuilder.setCancelable(true);

                        AlertDialog mDialog = mBuilder.create();
                        mDialog.show();
                    }

                });
            }

        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            ArrayList<String> listItemsByGroup = new ArrayList<>();

            @Override
            public void onClick(View v) {
                mRef = mDatabase.getReference("Event");
                btnEventEdit.show();
                btnSave.hide();
                txtAttending.setOnClickListener(null);

                txtPrice.setTextColor(Color.GRAY);
                txtPrice.setFocusableInTouchMode(false);
                txtPrice.setEnabled(false);

                txtAttending.setTextColor(Color.GRAY);
                String price = txtPrice.getText().toString();
                mRef.child(eventId).child("price").setValue(Double.parseDouble(price));


                if (newLeaders != null) {
                    for (String newLeadersId : newLeaders) {
                        mRef.child(eventId).child("eventLeaders").child(newLeadersId).setValue("pending");
                        Toast.makeText(ViewEvent.this, "The leader's names will appear when they have accepted your invitation", Toast.LENGTH_LONG).show();
                    }
                }


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

                ArrayList<String> approvedLeaders = new ArrayList<>();

                for (Map.Entry<String, String> element : eventLeaders.entrySet()) {
                    if (element.getValue().equals("Approved")) {
                        approvedLeaders.add(element.getKey());
                    }
                }

                int updatedSpaces = addSpaces(group[0], approvedLeaders);


                mRef.child(eventId).child("availableSpaces").setValue(updatedSpaces);
                txtSpaces.setText(String.valueOf(updatedSpaces));

                finish();
                startActivity(getIntent());
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

        viewPayments.setOnClickListener(new View.OnClickListener() {
            ArrayList<String> listItemsByGroup = new ArrayList<>();

            @Override
            public void onClick(View v) {
               Intent paymentIntent = new Intent(ViewEvent.this, ViewPaymentList.class);
               paymentIntent.putStringArrayListExtra("paymentList", paymentList);
               startActivity(paymentIntent);
            }

        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        openReview.putExtra("type", "create");
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
                    if(parentChildIds.values().toArray()[i].toString() != null){
                        childId = parentChildIds.values().toArray()[i].toString();
                    }
                    else{
                        childId = "no child";

                    }
                    if (key.equals(parentEmail)) {
                        for (String value : paymentList) {
                            if (value.equals("Empty")) {
                                //Overwrite default value if first payment
                                paymentList.set(0, childId);
                            } else {
                                paymentList.add(childId);
                            }
                        }
                        break;
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

    public int addSpaces(String group, ArrayList<String> leaders) {
        int newSpaces = 0;

        if (group.equals("Beavers") || group.equals("Cubs")) {
            newSpaces = (leaders.size() + 1) * 5;
        } else if (group.equals("Scouts") || group.equals("Ventures")) {
            newSpaces = (leaders.size() + 1) * 8;
        }


        return newSpaces;
    }

    public void makeEventAvailable(View v) {
        new AlertDialog.Builder(ViewEvent.this)
                .setTitle("Warning!")
                .setMessage("Once an event is made available, its details can no longer be edited." +
                        " Are you sure you want to do this?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Boolean allApproved = true;
                        for (String approval : eventLeaders.values()) {
                            if (approval.equals("pending")) {
                                allApproved = false;
                                break;
                            }

                        }
                        if (allApproved) {
                            mRef = mDatabase.getReference("Event");
                            mRef.child(eventId).child("approved").setValue("approved");

                            //check if the permission is not granted
                            if (checkPermission(Manifest.permission.SEND_SMS)) {
                                for (Parent p : parents) {
                                    if (eventGroup.contains(p.getGroup())) {
                                        SmsManager smsManager = SmsManager.getDefault();
                                        String str = p.getName();

                                        String[] splitStr = str.split("\\s+");

                                        String message = "Hi " + splitStr + ", an event has been made available.";
                                        smsManager.sendTextMessage(p.getPhone(), null, message, null, null);
                                    }
                                }
                            } else {
                                ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_SEND_SMS);

                            }

                            finish();
                            startActivity(getIntent());
                        } else {
                            Toast.makeText(ViewEvent.this, "There are still leaders pending approval", Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void openEventReviews(View v) {
        Intent viewReviews = new Intent(this, ViewReportList.class);
        viewReviews.putExtra("eventId", eventId);
        startActivity(viewReviews);
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);

        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public void deleteEvent(View v) {
        new AlertDialog.Builder(ViewEvent.this)
                .setTitle("Warning!")
                .setMessage("Are you sure you want to delete this event?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        mRef = mDatabase.getReference("Event");
                        mRef.child(eventId).removeValue();

                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
}

