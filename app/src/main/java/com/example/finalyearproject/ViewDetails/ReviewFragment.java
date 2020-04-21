package com.example.finalyearproject.ViewDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.Adapters.ReviewAdapter;
import com.example.finalyearproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.example.finalyearproject.Objects.Review;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class ReviewFragment extends Fragment {
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String type;
    String intentId;
    RecyclerView viewReview;

    ArrayList<Review> reviewList = new ArrayList<>();
    View v;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_reviews, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();

        type = getActivity().getIntent().getExtras().getString("type");


        mRef = mDatabase.getReference("Review");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                   String id = ds.getValue(Review.class).getReviewId();
                   String parentId = ds.getValue(Review.class).getParentId();
                   String eventId = ds.getValue(Review.class).getEventId();
                   String createdBy = ds.getValue(Review.class).getCreatedBy();
                   String txtTitle = ds.getValue(Review.class).getTitle();
                   String txtBody = ds.getValue(Review.class).getBody();

                   if(type.equals("Event")){
                       if (eventId.equals(intentId)) {
                           Review r = new Review(id, parentId, eventId, createdBy, txtTitle, txtBody);
                           reviewList.add(r);
                       }
                   }
                   else if(type.equals("Leader")){
                       if(createdBy.equals(intentId)){
                           Review r = new Review(id, parentId, eventId, createdBy, txtTitle, txtBody);
                           reviewList.add(r);
                       }
                   }

                }

                setAdapter();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    public void setAdapter() {
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(getActivity());
        viewReview.setLayoutManager(myLayoutManager);
        ReviewAdapter mAdapter = new ReviewAdapter(reviewList);
        viewReview.addItemDecoration(new

                DividerItemDecoration(getActivity(), VERTICAL));
        viewReview.setAdapter(mAdapter);
    }
}
