package com.example.finalyearproject.CreationClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.finalyearproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

import com.example.finalyearproject.Objects.Review;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class AddReview extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    private EditText title;
    private EditText body;
    private RadioGroup radioRatingGroup;
    private RadioButton selectedButton;

    private String eventId;
    private String parentId;
    private String createdBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        mRef = database.getInstance().getReference();


        Intent review = getIntent();
        eventId = review.getStringExtra("eventId");
        parentId = review.getStringExtra("parentId");
        createdBy = review.getStringExtra("createdBy");

        title = findViewById(R.id.reviewTitle);
        body = findViewById(R.id.reviewBody);
        radioRatingGroup = findViewById(R.id.ratingGroup);
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

        Review r = new Review(id, parentId, eventId, createdBy, txtTitle, txtBody, ratingValue);


        mRef.child("Review").child(id).setValue(r);
        mRef.child("Event").child(eventId).child("parentReviews").setValue(parentReviews);

        Toast.makeText(this, "Thank you for your review", Toast.LENGTH_LONG).show();

        this.finish();

    }
}
