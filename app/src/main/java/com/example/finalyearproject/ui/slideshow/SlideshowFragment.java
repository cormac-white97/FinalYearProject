package com.example.finalyearproject.ui.slideshow;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalyearproject.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SlideshowFragment extends Fragment {

    //private SlideshowViewModel slideshowViewModel;
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- YYYY", Locale.getDefault());
    private TextView monthVal;
    EditText type;
    EditText location;
    EditText date;
    EditText numAttending;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_slideshow, container, false);

        //Initialize all textFields
        monthVal = view.findViewById(R.id.Month);
        type  = view.findViewById(R.id.txtEventType);
        location  =  view.findViewById(R.id.txtLocation);
        date = view.findViewById(R.id.txtStartDate);
        numAttending = view.findViewById(R.id.txtNumAttending);

        compactCalendar = view.findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        monthVal.setText(dateFormatMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));

        //Hardcoded event for testing
        Event ev = new Event(Color.BLUE, 1574640000000L, "Camping");
        compactCalendar.addEvent(ev);

        Event ev2 = new Event(Color.BLUE, 1572912000000L, "Hike");
        compactCalendar.addEvent(ev2);


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                String formattedDate = dateFormat.format(dateClicked);
                boolean hasEvent = false;
                List<Event> eventsForMonth = compactCalendar.getEventsForMonth(compactCalendar.getFirstDayOfCurrentMonth());

                for(Event e : eventsForMonth){

                    if(e.getTimeInMillis() == dateClicked.getTime()){
                        hasEvent = true;
                        break;
                    }
                }

                //Hardcoded in value for
                if(hasEvent){
                    for(Event e : eventsForMonth) {
                        if (e.getTimeInMillis() == dateClicked.getTime()) {
                            type.setText(e.getData().toString());
                            location.setText("Lough Dan");
                            date.setText("" + formattedDate);
                            numAttending.setText("14");
                        }
                    }
                }
                else{
                    type.setText("");
                    location.setText("");
                    date.setText("");
                    numAttending.setText("");
                    Toast.makeText(getActivity(), "This will create a new event", Toast.LENGTH_LONG).show();

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