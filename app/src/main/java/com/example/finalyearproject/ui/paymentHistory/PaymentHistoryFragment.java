package com.example.finalyearproject.ui.paymentHistory;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.finalyearproject.LeaderAdapter;
import com.example.finalyearproject.PaymentListAdapter;
import com.example.finalyearproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Objects.PaymentHistory;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;


public class PaymentHistoryFragment extends Fragment {

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    RecyclerView paymentHistory;
    ArrayList<PaymentHistory> paymentList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_payment_history, container, false);
        paymentHistory = view.findViewById(R.id.paymentHistory);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();

        mRef = mDatabase.getReference("Payment History");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.getValue(PaymentHistory.class).getPaymentId();
                    double amount = ds.getValue(PaymentHistory.class).getAmount();
                    String eventId = ds.getValue(PaymentHistory.class).getEventId();
                    String parentId = ds.getValue(PaymentHistory.class).getParentId();
                    String memberId = ds.getValue(PaymentHistory.class).getMemberId();
                    String group = ds.getValue(PaymentHistory.class).getGroup();
                    String location = ds.getValue(PaymentHistory.class).getLocation();

                    if (parentId.equals(mUser.getUid())) {
                        PaymentHistory ph = new PaymentHistory(id, amount, eventId, parentId, memberId, group, location);
                        paymentList.add(ph);
                    }
                }

                setAdapter();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;

    }

    public void setAdapter() {
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(getActivity());
        paymentHistory.setLayoutManager(myLayoutManager);
        PaymentListAdapter mAdapter = new PaymentListAdapter(paymentList);
        paymentHistory.addItemDecoration(new

                DividerItemDecoration(getActivity(), VERTICAL));
        paymentHistory.setAdapter(mAdapter);
    }
}
