package com.example.finalyearproject.ui.event;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.EventObj;
import com.example.finalyearproject.MyAdapter;
import com.example.finalyearproject.R;
import com.example.finalyearproject.viewLocations;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static androidx.recyclerview.widget.LinearLayoutManager.*;

public class EventFragment extends Fragment {

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- YYYY", Locale.getDefault());
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView monthVal;
    private FloatingActionButton add;
    ArrayList<EventObj> allEventsInDB = new ArrayList<>();
    EditText editTextType;
    EditText editTextLocation;
    EditText editTextDate;
    EditText numAttending;
    String id;
    String date;
    String endDate;
    String group;
    String location;
    String type;
    String createdBy;

    String clickedId;
    String clickedDate;
    String clickedEndDate;
    String clickedGroup;
    String clickedLocation;
    String clickedType;
    String clickedCreatedBy;
    private final Long dayValue = 86400000L;
    ArrayList<EventObj> eventObjs = new ArrayList<>();
    public static final String dateval = "com.example.eventmanager";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_event, container, false);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Event");


        //Initialize all textFields
        monthVal = view.findViewById(R.id.Month);
        compactCalendar = view.findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        monthVal.setText(dateFormatMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));
        add = view.findViewById(R.id.btnAdd);
        add.hide();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event ev2 = null;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    id = ds.getValue(EventObj.class).getId();
                    date = ds.getValue(EventObj.class).getDate();
                    endDate = ds.getValue(EventObj.class).getEndDate();
                    group = ds.getValue(EventObj.class).getGroup();
                    location = ds.getValue(EventObj.class).getLocation();
                    type = ds.getValue(EventObj.class).getType();
                    createdBy = ds.getValue(EventObj.class).getCreatedBy();
                    boolean dateReached = false;


                    Long longDate = Long.parseLong(date);
                    Long longEndDate = Long.parseLong(endDate);



                    do {
                        if (longDate.equals(longEndDate)) {
                            //Must be repeated one more time to add event to the last day
                            EventObj e = new EventObj(id, type, location, date, endDate, group, createdBy);
                            eventObjs.add(e);
                            e.toString();
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
                            compactCalendar.addEvent(ev2);

                            longDate = longDate + dayValue;
                            dateReached = true;
                        } else {

                            EventObj e = new EventObj(id, type, location, date, endDate, group, createdBy);
                            eventObjs.add(e);
                            e.toString();
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
                            compactCalendar.addEvent(ev2);

                            longDate = longDate + dayValue;
                        }

                    } while (dateReached != true);
                    EventObj eObj = new EventObj(id, type, location, date, endDate, group, createdBy);
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
                    ArrayList <Long> eventDays = new ArrayList<>();
                    long dateTime = Long.parseLong(e.getDate());
                    boolean dateReached = false;
                    long endDate = Long.parseLong(e.getEndDate());
                    //create an arraylist to map dates between the start and end date to an event
                    do{
                        if(dateTime == endDate){
                            eventDays.add(dateTime);
                            dateTime = dateTime + dayValue;
                            dateReached = true;

                        }
                        else{
                            eventDays.add(dateTime);
                            dateTime = dateTime + dayValue;
                        }
                    }
                    while (dateReached == false);

                    if (eventDays.contains(dateEpoch)) {
                        EventObj eventObj = new EventObj(e.getId(), e.getType(), e.getLocation(), e.getDate(), e.getEndDate(),e.getGroup(), e.getCreatedBy());
                        myDataset.add(eventObj);
                    }
                }

                MyAdapter mAdapter = new MyAdapter(myDataset);
                myRecyclerView.addItemDecoration(new

                        DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                myRecyclerView.setAdapter(mAdapter);
                add.show();
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



    public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.txtHeader.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(v.getContext(),"eventFragment Test", Toast.LENGTH_SHORT ).show();
                //addItem(position);
                // call activity to pass the item position
                //Intent intent = new Intent(v.getContext(), UpdateActivity.class );
                //intent.putExtra(MESSAGE_KEY1 ,name);
                // intent.putExtra(MESSAGE_KEY2, position);
                //v.getContext().startActivity(intent);

            }
        });
    }
}