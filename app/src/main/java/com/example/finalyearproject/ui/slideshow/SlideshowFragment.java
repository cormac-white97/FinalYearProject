package com.example.finalyearproject.ui.slideshow;


import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalyearproject.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- YYYY", Locale.getDefault());
    private TextView monthVal;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_slideshow, container, false);
        monthVal = view.findViewById(R.id.Month);


        compactCalendar = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        monthVal.setText(dateFormatMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));

        Event ev = new Event(Color.BLUE, 1574640000000L, "Camping");
        compactCalendar.addEvent(ev);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                Context context = getActivity().getApplicationContext();
                TextView date = (TextView) view.findViewById(R.id.eventDate);
                TextView eventDate = (TextView) view.findViewById(R.id.monthEvents);
                DateFormat simple = new SimpleDateFormat("dd MMM yyyy");
                String formattedsimple = simple.format(dateClicked);
                boolean hasEvent = false;
                List<Event> eventsForMonth = compactCalendar.getEventsForMonth(1572566400000L);


                for(Event e : eventsForMonth){

                    if(e.getTimeInMillis() == dateClicked.getTime()){
                        hasEvent = true;
                        break;
                    }

                }

                if(hasEvent){
                    date.setText("Event on date");
                }
                else{
                    date.setText("No event on date");

                }
                 eventDate.setText("" + dateClicked.getTime());



            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                monthVal.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });
        return view;

    }
}