package com.example.finalyearproject.ui.leaders;

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

import com.example.finalyearproject.Adapters.LeaderAdapter;
import com.example.finalyearproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.finalyearproject.Objects.Leader;
import com.example.finalyearproject.Objects.Parent;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class LeaderFragment extends Fragment {

    private LeaderViewModel leaderViewModel;
    ArrayList<Leader> myDataset = new ArrayList<>();
    RecyclerView recyclerView;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String loggedInGroup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        leaderViewModel =
                ViewModelProviders.of(this).get(LeaderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_leaders, container, false);

        recyclerView = root.findViewById(R.id.leaderView);


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getInstance().getCurrentUser();

        List<String> typeList = Arrays.asList("Leader", "Parent");



        mDatabase = FirebaseDatabase.getInstance();

        for(final String type : typeList){
            mRef = mDatabase.getReference("Person").child(type);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(type.equals("Leader")){
                            String id = ds.getValue(Leader.class).getLeaderId();
                            String group = ds.getValue(Leader.class).getGroup();

                            if(id.equals(mUser.getUid())){
                                loggedInGroup = group;
                            }
                        }
                        else if(type.equals("Parent")){
                            String id = ds.getValue(Parent.class).getParentId();
                            String group = ds.getValue(Leader.class).getGroup();

                            if(id != null){
                                if(id.equals(mUser.getUid())){
                                    loggedInGroup = group;
                                }
                            }

                        }

                    }

                    setAdapter();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }





        mRef = mDatabase.getReference("Person").child("Leader");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.getValue(Leader.class).getLeaderId();
                    String name = ds.getValue(Leader.class).getName();
                    String email = ds.getValue(Leader.class).getEmail();
                    String DOB = ds.getValue(Leader.class).getDOB();
                    String vettingDate = ds.getValue(Leader.class).getVettingDate();
                    String group = ds.getValue(Leader.class).getGroup();
                    String phone = ds.getValue(Leader.class).getPhone();

                    if(group.equals(loggedInGroup)){
                        Leader l = new Leader(id, name, DOB,group, phone, email, vettingDate);

                        myDataset.add(l);
                    }

                }
                if(myDataset.isEmpty()){

                }
                setAdapter();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }

    public void setAdapter() {
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(myLayoutManager);
        LeaderAdapter mAdapter = new LeaderAdapter(myDataset);
        recyclerView.addItemDecoration(new

                DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }
}