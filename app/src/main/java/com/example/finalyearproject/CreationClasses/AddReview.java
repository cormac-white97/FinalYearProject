package com.example.finalyearproject.CreationClasses;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.Objects.Leader;
import com.example.finalyearproject.Objects.Parent;
import com.example.finalyearproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

import com.example.finalyearproject.Objects.Review;
import com.google.firebase.database.ValueEventListener;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class AddReview extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    private TextView msg;
    private EditText title;
    private EditText body;
    private RadioGroup radioRatingGroup;
    private RadioButton selectedButton;
    private Button btnCreateReview;
    private RatingBar eventRating;

    private String eventId;
    private String parentId;
    private String createdBy;
    private String type;
    private String reviewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        database = FirebaseDatabase.getInstance();
        mRef = database.getInstance().getReference();


        Intent review = getIntent();
        eventId = review.getStringExtra("eventId");
        parentId = review.getStringExtra("parentId");
        createdBy = review.getStringExtra("createdBy");
        type = review.getStringExtra("type");
        reviewId = review.getStringExtra("reviewId");

        msg = findViewById(R.id.addReviewMsg);
        title = findViewById(R.id.reviewTitle);
        body = findViewById(R.id.reviewBody);
        radioRatingGroup = findViewById(R.id.ratingGroup);
        btnCreateReview = findViewById(R.id.btnAddReview);
        eventRating = findViewById(R.id.overallReview);


        if(type.equals("view")){

            //removing button to create a review and setting text fields to uneditable
            btnCreateReview.setVisibility(View.GONE);
            radioRatingGroup.setVisibility(View.GONE);
            title.setEnabled(false);
            body.setEnabled(false);


            Drawable stars = eventRating.getProgressDrawable();
            stars.setTint( Color.BLUE );

            mRef = database.getReference("Review");
            mRef.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String id = ds.getValue(Review.class).getReviewId();
                        String dbParentId = ds.getValue(Review.class).getParentId();
                        String eventId = ds.getValue(Review.class).getEventId();
                        String createdBy = ds.getValue(Review.class).getCreatedBy();
                        String txtTitle = ds.getValue(Review.class).getTitle();
                        String txtBody = ds.getValue(Review.class).getBody();
                        int rating = ds.getValue(Review.class).getRating();

                        if (id.equals(reviewId)) {
                            title.setText(txtTitle);
                            title.setTextColor(Color.BLACK);
                            body.setText(txtBody);
                            body.setTextColor(Color.BLACK);

                            eventRating.setIsIndicator(true);
                            eventRating.setNumStars(rating);

                            parentId = dbParentId;
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mRef = database.getReference("Person").child("Parent");
            mRef.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String id = ds.getValue(Parent.class).getParentId();
                        String parentName = ds.getValue(Parent.class).getName();

                        if (id.equals(parentId)) {
                           msg.setText(parentName + "'s Review.");
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            eventRating.setVisibility(View.GONE);
        }
    }

    public void createReview(View v){
        String id = UUID.randomUUID().toString();
        String txtTitle = title.getText().toString();
        String txtBody = body.getText().toString();
        HashMap<String, String> parentReviews = new HashMap<>();
        int selectedId = radioRatingGroup.getCheckedRadioButtonId();
        selectedButton = (RadioButton) findViewById(selectedId);
        int ratingValue = Integer.parseInt(selectedButton.getText().toString());
        parentReviews.put(parentId, id);

        if(ratingValue != 0){
            Review r = new Review(id, parentId, eventId, createdBy, txtTitle, txtBody, ratingValue);


            mRef.child("Review").child(id).setValue(r);
            mRef.child("Event").child(eventId).child("parentReviews").setValue(parentReviews);

            Toast.makeText(this, "Thank you for your review", Toast.LENGTH_LONG).show();

            this.finish();
        }
        else{
            Toast.makeText(this, "Please add a rating", Toast.LENGTH_LONG).show();

        }


    }
}
