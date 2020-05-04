package com.example.finalyearproject.ViewDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalyearproject.Adapters.GroupViewAdapter;
import com.example.finalyearproject.Objects.EventObj;
import com.example.finalyearproject.Objects.Leader;
import com.example.finalyearproject.Objects.Member;
import com.example.finalyearproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class ViewPaymentList extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    DatabaseReference memberRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    TextView message;
    RecyclerView recyclerView;
    Intent intent;
    ImageView btnReturn;
    ArrayList<String> payments = new ArrayList<>();
    ArrayList<Member> payeeNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payment_list);
        recyclerView = findViewById(R.id.paymentListRV);
        btnReturn = findViewById(R.id.paymentListReturnArrow);
        intent = getIntent();
        payments= intent.getStringArrayListExtra("paymentList");
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getInstance().getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();

        mRef = mDatabase.getReference("Person").child("Member");
        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getValue(Member.class).getName();
                    String group = ds.getValue(Member.class).getGroup();
                    String id = ds.getValue(Member.class).getId();
                    String DOM = ds.getValue(Member.class).getMemDom();
                    String DOB = ds.getValue(Member.class).getMemDob();
                    String notes = ds.getValue(Member.class).getNotes();

                    if (payments.contains(id)){
                        Member m = new Member(id, name, group,DOB, DOM, notes);
                        payeeNames.add(m);
                    }
                }
                setAdapter(payeeNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void setAdapter(ArrayList<Member> myDataset) {
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLayoutManager);
        GroupViewAdapter mAdapter = new GroupViewAdapter(myDataset);
        recyclerView.addItemDecoration(new

                DividerItemDecoration(this, VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }
}
