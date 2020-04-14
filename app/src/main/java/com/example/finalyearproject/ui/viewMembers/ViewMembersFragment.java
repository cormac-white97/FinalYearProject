package com.example.finalyearproject.ui.viewMembers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.GroupViewAdapter;
import Objects.Leader;
import Objects.Member;

import com.example.finalyearproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class ViewMembersFragment extends Fragment {

    private ViewMembersViewModel viewMembersViewModel;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    TextView message;

    String email;
    String name;
    String group;
    String id;
    String DOM;
    String DOB;
    String notes;
    String token;
    String loggedInGroup;

    ArrayList<Member> myDataset = new ArrayList<>();
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewMembersViewModel =
                ViewModelProviders.of(this).get(ViewMembersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_view_members, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.groupView);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getInstance().getCurrentUser();

        message = root.findViewById(R.id.memberTextView);



        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Person").child("Leader");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    email = ds.getValue(Leader.class).getEmail();
                    group = ds.getValue(Leader.class).getGroup();

                    if(email.equals(mUser.getEmail())){
                        loggedInGroup = group;
                    }
                }

                setAdapter();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mRef = mDatabase.getReference("Person").child("Member");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    name = ds.getValue(Member.class).getName();
                    group = ds.getValue(Member.class).getGroup();
                    id = ds.getValue(Member.class).getId();
                    DOM = ds.getValue(Member.class).getMemDom();
                    DOB = ds.getValue(Member.class).getMemDob();
                    notes = ds.getValue(Member.class).getNotes();

                    if(group.equals(loggedInGroup)){
                        Member m = new Member(id, name, group,DOB, DOM, notes);
                        myDataset.add(m);
                    }

                }
                if(myDataset.isEmpty()){
                    message.setText("There are no members in your group");
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
        GroupViewAdapter mAdapter = new GroupViewAdapter(myDataset);
        recyclerView.addItemDecoration(new

                DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }
}