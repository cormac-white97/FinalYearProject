package com.example.finalyearproject.ui.event;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalyearproject.EventObj;
import com.example.finalyearproject.R;
import com.example.finalyearproject.viewLocations;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventFragment extends Fragment {

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- YYYY", Locale.getDefault());
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView monthVal;
    EditText editTextType;
    EditText editTextLocation;
    EditText editTextDate;
    EditText numAttending;
    String date;
    String group;
    String location;
    String type;
    ArrayList<EventObj> eventObjs = new ArrayList<>();
    public static final String dateval = "com.example.eventmanager";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_event, container, false);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Event");


        //Initialize all textFields
        monthVal = view.findViewById(R.id.Month);
        editTextType = view.findViewById(R.id.txtEventType);
        editTextLocation = view.findViewById(R.id.txtLocation);
        editTextDate = view.findViewById(R.id.viewStartDate);
        numAttending = view.findViewById(R.id.txtNumAttending);
        compactCalendar = view.findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        monthVal.setText(dateFormatMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event ev2 = null;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    date = ds.getValue(EventObj.class).getDate();
                    group = ds.getValue(EventObj.class).getGroup();
                    location = ds.getValue(EventObj.class).getLocation();
                    type = ds.getValue(EventObj.class).getType();

                    EventObj e = new EventObj(type, location, date, group);
                    eventObjs.add(e);
                    e.toString();
                    if(group.equals("Beavers")){
                        ev2 = new Event(Color.BLUE, Long.parseLong(date), type);
                    }
                    else if(group.equals("Cubs")){
                        ev2 = new Event(Color.RED, Long.parseLong(date), type);

                    }
                    else if(group.equals("Scouts")){
                        ev2 = new Event(Color.GREEN, Long.parseLong(date), type);

                    }
                    else if(group.equals("Ventures")){
                        ev2 = new Event(Color.MAGENTA, Long.parseLong(date), type);

                    }
                    else if(group.equals("Rovers")){
                        ev2 = new Event(Color.BLACK, Long.parseLong(date), type);

                    }
                    compactCalendar.addEvent(ev2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {


                DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                String formattedDate = dateFormat.format(dateClicked);
                boolean hasEvent = false;
                List<Event> eventsForMonth = compactCalendar.getEventsForMonth(compactCalendar.getFirstDayOfCurrentMonth());

                for (Event e : eventsForMonth) {

                    if (e.getTimeInMillis() == dateClicked.getTime()) {
                        hasEvent = true;
                        break;
                    }
                }

                //Hardcoded in value for event in calendar
                if (hasEvent) {
                    for (Event e : eventsForMonth) {
                        if (e.getTimeInMillis() == dateClicked.getTime()) {
                            editTextType.setText(e.getData().toString());
                            editTextDate.setText("" + formattedDate);
                            for (EventObj evo : eventObjs){

                                if(evo.getDate().equals(dateClicked.getTime() + "")){
                                    editTextLocation.setText(evo.getLocation());
                                    numAttending.setText(evo.getGroup());
                                }
                            }


                        }
                    }
                } else {
                    String dateString = String.valueOf(dateClicked.getTime());
                    Intent intent = new Intent(getActivity(), viewLocations.class);
                    intent.putExtra(dateval, dateString);
                    startActivity(intent);

                }

            }

            //Update month value at the top of the calendar when scrolling
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                monthVal.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

        return view;
    }

}