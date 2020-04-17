package com.example.finalyearproject.ui.eventReport;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Objects.EventObj;

import com.example.finalyearproject.MyAdapter;
import com.example.finalyearproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class EventReportFragment extends Fragment implements OnChartValueSelectedListener {
    View root;
    private EventReportModel eventReportModel;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<String> groupList = new ArrayList<>();
    private ArrayList<Integer> eventCount = new ArrayList<>();
    private ArrayList<EventObj> eventDetails = new ArrayList<>();
    private PieChart chart;

    int beaverCounter = 0;
    int cubCounter = 0;
    int scoutCounter = 0;
    int ventureCounter = 0;
    int roverCounter = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        eventReportModel =
                ViewModelProviders.of(this).get(EventReportModel.class);
        root = inflater.inflate(R.layout.fragment_event_report, container, false);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Event");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event ev2 = null;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.getValue(EventObj.class).getId();
                    String date = ds.getValue(EventObj.class).getDate();
                    String endDate = ds.getValue(EventObj.class).getEndDate();
                    String group = ds.getValue(EventObj.class).getGroup();
                    String location = ds.getValue(EventObj.class).getLocation();
                    String type = ds.getValue(EventObj.class).getType();
                    double price = ds.getValue(EventObj.class).getPrice();
                    String createdBy = ds.getValue(EventObj.class).getCreatedBy();
                    HashMap<String, String> eventLeaders = ds.getValue(EventObj.class).getEventLeaders();
                    ArrayList<String> paymentList = ds.getValue(EventObj.class).getPaymentList();
                    int availableSpaces = ds.getValue(EventObj.class).getAvailableSpaces();
                    double lat = ds.getValue(EventObj.class).getLat();
                    double lng = ds.getValue(EventObj.class).getLng();
                    String approved = ds.getValue(EventObj.class).getApproved();
                    boolean dateReached = false;

                    EventObj e = new EventObj(id, type, location, date, endDate, group, price, createdBy, eventLeaders, paymentList, availableSpaces, lng, lat, approved);
                    eventDetails.add(e);

                    if (!groupList.contains(group)) {
                        groupList.add(group);
                    }

                    if (group.equals("Beavers")) {
                        beaverCounter++;
                    } else if (group.equals("Cubs")) {
                        cubCounter++;
                    } else if (group.equals("Scouts")) {
                        scoutCounter++;
                    } else if (group.equals("Ventures")) {
                        ventureCounter++;
                    } else if (group.equals("Rovers")) {
                        roverCounter++;
                    }

                }

                Collections.sort(groupList);

                if (beaverCounter > 0)
                    eventCount.add(beaverCounter);

                if (cubCounter > 0)
                    eventCount.add(cubCounter);

                if (roverCounter > 0)
                    eventCount.add(roverCounter);

                if (scoutCounter > 0)
                    eventCount.add(scoutCounter);

                if (ventureCounter > 0)
                    eventCount.add(ventureCounter);

                List<PieEntry> pieEntries = new ArrayList<>();

                for (int i = 0; i < groupList.size(); i++) {
                    pieEntries.add(new PieEntry(eventCount.get(i), groupList.get(i)));
                }

                PieDataSet dataSet = new PieDataSet(pieEntries, "");
                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return String.valueOf((int) value);
                    }
                });

                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData data = new PieData(dataSet);

                chart = root.findViewById(R.id.pie_chart);
                chart.setData(data);
                chart.animateY(1000);
                chart.invalidate();
                chart.setClickable(true);
                chart.setOnChartValueSelectedListener(EventReportFragment.this);
                chart.setCenterText("Events");
                chart.setCenterTextSize(25F);
                chart.getDescription().setEnabled(false);
                chart.setCenterTextColor(Color.parseColor("#084897"));

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return root;
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        PieEntry pe = (PieEntry) e;
        RecyclerView myRecyclerView = (RecyclerView) root.findViewById(R.id.report_recycler_view);
        ArrayList<EventObj> eventForDay = new ArrayList<>();
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(myLayoutManager);
        ArrayList<EventObj> myDataset = new ArrayList<EventObj>();

        // Clear collection..
        myDataset.clear();

        for (EventObj ed : eventDetails) {
            if (ed.getGroup().equals(String.valueOf(pe.getLabel()))) {
                EventObj eventObj = new EventObj(ed.getId(), ed.getType(), ed.getLocation(), ed.getDate(), ed.getEndDate(), ed.getGroup(), ed.getPrice(), ed.getCreatedBy(), ed.getEventLeaders(), ed.getPaymentList(), ed.getAvailableSpaces(), ed.getLat(), ed.getLng(), ed.getApproved());
                myDataset.add(eventObj);
            }
        }

        MyAdapter mAdapter = new MyAdapter(myDataset);
        myRecyclerView.addItemDecoration(new

                DividerItemDecoration(getActivity(), VERTICAL));
        myRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onNothingSelected() {

    }
}