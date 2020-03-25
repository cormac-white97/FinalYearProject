package com.example.finalyearproject.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.AccountAdapter;
import com.example.finalyearproject.GroupViewAdapter;
import com.example.finalyearproject.Leader;
import com.example.finalyearproject.Member;
import com.example.finalyearproject.Parent;
import com.example.finalyearproject.R;
import com.example.finalyearproject.ViewEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    String email;
    String name;
    String group;
    String id;
    String DOM;
    String DOB;
    String notes;
    String token;

    ArrayList<Member> myDataset = new ArrayList<>();
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.groupView);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Person").child("Member");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //name = ds.getValue(Member.class).getName();
                    name = ds.getValue(Member.class).getName();
                    email = ds.getValue(Member.class).getEmail();
                    group = ds.getValue(Member.class).getGroup();
                    id = ds.getValue(Member.class).getId();
                    DOM = ds.getValue(Member.class).getMemDom();
                    DOB = ds.getValue(Member.class).getMemDob();
                    notes = ds.getValue(Member.class).getNotes();
                    token = ds.getValue(Member.class).getFcmToken();

                    Member m = new Member(id, name, email,group,DOB, DOM, notes, token);
                    myDataset.add(m);
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