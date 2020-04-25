package com.example.finalyearproject.ui.home;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.Objects.EventObj;

import com.example.finalyearproject.Adapters.MyAdapter;

import com.example.finalyearproject.Objects.Leader;
import com.example.finalyearproject.Objects.Parent;

import com.example.finalyearproject.R;
import com.example.finalyearproject.ViewDetails.viewLocations;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static androidx.recyclerview.widget.LinearLayoutManager.*;

public class HomeFragment extends Fragment {

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- YYYY", Locale.getDefault());
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference personRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView monthVal;
    private FloatingActionButton add;
    ArrayList<Leader> leaders = new ArrayList<>();
    static HashMap<String, String> idAndName = new HashMap<>();
    ArrayList<EventObj> allEventsInDB = new ArrayList<>();
    EditText editTextType;
    EditText editTextLocation;
    EditText editTextDate;
    EditText numAttending;

    String clickedId;
    String clickedDate;
    String clickedEndDate;
    String clickedGroup;
    String clickedLocation;
    String clickedType;
    String clickedCreatedBy;
    String loggedInType = "no type";
    String loggedInGroup = "no group";
    double lat;
    double lng;
    private final Long dayValue = 86400000L;
    ArrayList<EventObj> eventObjs = new ArrayList<>();
    public static final String dateval = "com.example.eventmanager";

    private HomeViewModel homeViewModel;

    FirebaseUser mUser = mAuth.getInstance().getCurrentUser();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Event");

        List<String> typeList = Arrays.asList("Leader", "Parent", "Member");
        final boolean[] foundUser = {false};

        //Initialize all textFields
        monthVal = view.findViewById(R.id.Month);
        compactCalendar = view.findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        monthVal.setText(dateFormatMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));
        add = view.findViewById(R.id.btnAdd);
        add.hide();

        for (final String type : typeList) {
            personRef = database.getReference("Person").child(type);
            personRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        if (type.equals("Leader")) {
                            String personID = ds.getValue(Leader.class).getLeaderId();
                            String name = ds.getValue(Leader.class).getName();
                            String DOB = ds.getValue(Leader.class).getDOB();
                            String group = ds.getValue(Leader.class).getGroup();
                            String phone = ds.getValue(Leader.class).getPhone();
                            String email = ds.getValue(Leader.class).getEmail();
                            String vettingDate = ds.getValue(Leader.class).getVettingDate();

                            Leader p = new Leader(personID, name, DOB, group, phone, email, vettingDate);
                            leaders.add(p);
                            idAndName.put(personID, name);

                            if (mUser.getEmail().equals(email)) {
                                loggedInType = "Leader";

                            }
                        } else if (type.equals("Parent")) {
                            String parentId = ds.getValue(Parent.class).getParentId();
                            String group = ds.getValue(Parent.class).getGroup();

                            if (mUser.getUid().equals(parentId)) {
                                loggedInType = "Parent";
                                loggedInGroup = group;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if (foundUser[0]) {
                break;
            }
        }


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event ev2 = null;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.getValue(EventObj.class).getId();
                    String date = ds.getValue(EventObj.class).getStartDate();
                    String endDate = ds.getValue(EventObj.class).getEndDate();
                    String group = ds.getValue(EventObj.class).getGroup();
                    String location = ds.getValue(EventObj.class).getLocation();
                    String type = ds.getValue(EventObj.class).getType();
                    double price = ds.getValue(EventObj.class).getPrice();
                    String createdBy = ds.getValue(EventObj.class).getCreatedBy();
                    HashMap<String, String> eventLeaders = ds.getValue(EventObj.class).getEventLeaders();
                    ArrayList<String> paymentList = ds.getValue(EventObj.class).getPaymentList();
                    int availableSpaces = ds.getValue(EventObj.class).getAvailableSpaces();
                    lat = ds.getValue(EventObj.class).getLat();
                    lng = ds.getValue(EventObj.class).getLng();
                    String approved = ds.getValue(EventObj.class).getApproved();
                    HashMap<String, String> parentReviews = new HashMap<>();

                    boolean dateReached = false;


                    Long longDate = Long.parseLong(date);
                    Long longEndDate = Long.parseLong(endDate);


                    do {
                        if (longDate.equals(longEndDate)) {
                            //Must be repeated one more time to add event to the last day
                            EventObj e = new EventObj(id, type, location, date, endDate, group, price, createdBy, eventLeaders, parentReviews, paymentList, availableSpaces, lng, lat, approved);
                            eventObjs.add(e);

                            addEventMarker(group, longDate, type, ev2, compactCalendar, approved);

                            longDate = longDate + dayValue;
                            dateReached = true;
                        } else {

                            EventObj e = new EventObj(id, type, location, date, endDate, group, price, createdBy, eventLeaders, parentReviews, paymentList, availableSpaces, lng, lat, approved);
                            eventObjs.add(e);

                            addEventMarker(group, longDate, type, ev2, compactCalendar, approved);

                            longDate = longDate + dayValue;
                        }

                    } while (dateReached != true);

                    EventObj eObj = new EventObj(id, type, location, date, endDate, group, price, createdBy, eventLeaders, parentReviews, paymentList, availableSpaces, lng, lat, approved);
                    allEventsInDB.add(eObj);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(final Date dateClicked) {
                RecyclerView myRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

                ArrayList<EventObj> eventForDay = new ArrayList<>();

                long dateEpoch = dateClicked.getTime();


                LinearLayoutManager myLayoutManager = new LinearLayoutManager(getActivity());
                myRecyclerView.setLayoutManager(myLayoutManager);

                ArrayList<EventObj> myDataset = new ArrayList<EventObj>();

                // Clear collection..
                myDataset.clear();

                for (EventObj e : allEventsInDB) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    ArrayList<Long> eventDays = new ArrayList<>();
                    long dateTime = Long.parseLong(e.getStartDate());
                    boolean dateReached = false;
                    long endDate = Long.parseLong(e.getEndDate());
                    //create an arraylist to map dates between the start and end date to an event
                    do {
                        if (dateTime == endDate) {
                            eventDays.add(dateTime);
                            dateTime = dateTime + dayValue;
                            dateReached = true;

                        } else {
                            eventDays.add(dateTime);
                            dateTime = dateTime + dayValue;
                        }
                    }
                    while (dateReached == false);

                    if (eventDays.contains(dateEpoch)) {
                        EventObj eventObj = new EventObj(e.getId(), e.getType(), e.getLocation(), e.getStartDate(), e.getEndDate(), e.getGroup(), e.getPrice(), e.getCreatedBy(), e.getEventLeaders(), e.getParentReviews(), e.getPaymentList(), e.getAvailableSpaces(), e.getLat(), e.getLng(), e.getApproved());
                        myDataset.add(eventObj);
                    }
                }

                MyAdapter mAdapter = new MyAdapter(myDataset);
                myRecyclerView.addItemDecoration(new

                        DividerItemDecoration(getActivity(), VERTICAL));
                myRecyclerView.setAdapter(mAdapter);

                if (loggedInType.equals("Leader")) {
                    add.show();
                }

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        String dateString = String.valueOf(dateClicked.getTime());
                        Intent intent = new Intent(getActivity(), viewLocations.class);
                        intent.putExtra(dateval, dateString);
                        startActivity(intent);
                    }
                });
            }

            //Update month value at the top of the calendar when scrolling
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                monthVal.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

        return view;
    }

    public HashMap<String, String> getLeaderNameAndId() {
        return idAndName;
    }


    public void addEventMarker(String group, Long longDate, String type, Event ev2,CompactCalendarView calendar, String approval) {

        if (loggedInType.equals("Parent")) {
            if (group.equals("Beavers") && group.equals(loggedInGroup) && approval.equals("approved")) {
                ev2 = new Event(Color.BLUE, longDate, type);
            } else if (group.equals("Cubs") && group.equals(loggedInGroup) && approval.equals("approved")) {
                ev2 = new Event(Color.RED, longDate, type);

            } else if (group.equals("Scouts") && group.equals(loggedInGroup) && approval.equals("approved")) {
                ev2 = new Event(Color.GREEN, longDate, type);

            } else if (group.equals("Ventures") && group.equals(loggedInGroup) && approval.equals("approved")) {
                ev2 = new Event(Color.MAGENTA, longDate, type);

            } else if (group.equals("Rovers") && group.equals(loggedInGroup) && approval.equals("approved")) {
                ev2 = new Event(Color.BLACK, longDate, type);

            }
            if(ev2 != null){
                calendar.addEvent(ev2);
            }
        }
        else if(loggedInType.equals("Leader")){
            if (group.equals("Beavers")) {
                ev2 = new Event(Color.BLUE, longDate, type);
            } else if (group.equals("Cubs")) {
                ev2 = new Event(Color.RED, longDate, type);

            } else if (group.equals("Scouts")) {
                ev2 = new Event(Color.GREEN, longDate, type);

            } else if (group.equals("Ventures")) {
                ev2 = new Event(Color.MAGENTA, longDate, type);

            } else if (group.equals("Rovers")) {
                ev2 = new Event(Color.BLACK, longDate, type);

            }
            calendar.addEvent(ev2);
        }

    }
}