package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.Adapters.MyAdapter;
import com.example.finalyearproject.Adapters.ReviewAdapter;
import com.example.finalyearproject.CreationClasses.CreateNewEvent;
import com.example.finalyearproject.Objects.EventObj;
import com.example.finalyearproject.Objects.Review;
import com.example.finalyearproject.ViewDetails.viewLocations;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class ViewReportList extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;

    BarChart chart;

    RecyclerView reviewList;
    TextView msg;
    ImageView btnReturn;
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    ArrayList<Review> eventReview = new ArrayList<>();
    ArrayList<Review> myDataset = new ArrayList<Review>();
    Intent review;
    String selectedEventId;

    int oneCount = 0;
    int twoCount = 0;
    int threeCount = 0;
    int fourCount = 0;
    int fiveCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report_list);
        chart = (BarChart) findViewById(R.id.review_chart);
        reviewList = findViewById(R.id.eventReviewList);
        msg = findViewById(R.id.reviewMsg);
        btnReturn = findViewById(R.id.returnArrow);

        review = getIntent();
        selectedEventId = review.getStringExtra("eventId");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Review");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.getValue(Review.class).getReviewId();
                    String parentId = ds.getValue(Review.class).getParentId();
                    String eventId = ds.getValue(Review.class).getEventId();
                    String createdBy = ds.getValue(Review.class).getCreatedBy();
                    String title = ds.getValue(Review.class).getTitle();
                    String body = ds.getValue(Review.class).getBody();
                    int rating = ds.getValue(Review.class).getRating();

                    if(selectedEventId.equals(eventId)){
                        Review r = new Review(id, parentId, eventId, createdBy, title, body, rating);
                        eventReview.add(r);
                    }

                }
                for (Review r : eventReview) {
                    if (r.getRating() == 1) {
                        oneCount++;
                    } else if (r.getRating() == 2) {
                        twoCount++;
                    } else if (r.getRating() == 3) {
                        threeCount++;
                    } else if (r.getRating() == 4) {
                        fourCount++;
                    } else if (r.getRating() == 5) {
                        fiveCount++;
                    }
                }

                chart.animateXY(2000, 2000);
                chart.invalidate();

                barEntries.add(new BarEntry(0, oneCount));
                barEntries.add(new BarEntry(1, twoCount));
                barEntries.add(new BarEntry(2, threeCount));
                barEntries.add(new BarEntry(3, fourCount));
                barEntries.add(new BarEntry(4, fiveCount));

                BarDataSet dataSet = new BarDataSet(barEntries, "data set 1");
                final ArrayList<String> xAxisLabel = new ArrayList<>();
                xAxisLabel.add("1");
                xAxisLabel.add("2");
                xAxisLabel.add("3");
                xAxisLabel.add("4");
                xAxisLabel.add("5");

                XAxis xAxis = chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

                ValueFormatter formatter = new ValueFormatter() {


                    @Override
                    public String getFormattedValue(float value) {
                        return xAxisLabel.get((int) value);
                    }
                };

                xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
                xAxis.setValueFormatter(formatter);

                BarData data = new BarData(dataSet);
                data.setBarWidth(0.9f);
                chart.getXAxis().setDrawGridLines(false);
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                chart.setData(data);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String selectedRating = chart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), chart.getXAxis());

                RecyclerView myRecyclerView = (RecyclerView) findViewById(R.id.report_recycler_view);

                msg.setText("Reviews with a " + selectedRating + " star rating:");

                // Clear collection..
                myDataset.clear();

                for (Review r : eventReview) {
                    if (String.valueOf(r.getRating()).equals(selectedRating)) {
                        Review review = new Review(r.getReviewId(), r.getParentId(), r.getEventId(), r.getCreatedBy(), r.getTitle(), r.getBody(), r.getRating());
                        myDataset.add(review);
                    }
                }

                setAdapter(myDataset);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void setAdapter(ArrayList<Review> myDataset) {
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        reviewList.setLayoutManager(myLayoutManager);
        ReviewAdapter mAdapter = new ReviewAdapter(myDataset);
        reviewList.addItemDecoration(new

                DividerItemDecoration(this, VERTICAL));
        reviewList.setAdapter(mAdapter);
    }




}