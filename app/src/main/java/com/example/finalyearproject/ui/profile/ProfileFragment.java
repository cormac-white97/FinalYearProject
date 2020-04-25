package com.example.finalyearproject.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.CreationClasses.CreateParent;
import com.example.finalyearproject.ViewDetails.MemberProfile;

import com.example.finalyearproject.Objects.Leader;
import com.example.finalyearproject.Objects.Member;
import com.example.finalyearproject.Objects.Parent;
import com.example.finalyearproject.Objects.Review;

import com.example.finalyearproject.R;
import com.example.finalyearproject.Adapters.ReviewAdapter;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static androidx.recyclerview.widget.LinearLayoutManager.*;

public class ProfileFragment extends Fragment {

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- YYYY", Locale.getDefault());
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView monthVal;
    private FloatingActionButton add;

    EditText txtProfileName;
    EditText txtProfileEmail;
    EditText txtDOB;
    EditText txtVettingDate;
    EditText txtProfileGroup;
    EditText txtProfilePhoneNum;
    FloatingActionButton btnEdit;
    final String accountTypes[] = new String[]{"Leader", "Parent"};
    String intentType;
    String intentId;

    String child = null;
    RecyclerView viewReview;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final String userId = mUser.getUid();
        Intent i = getActivity().getIntent();
        intentType = i.getStringExtra("type");
        intentId = i.getStringExtra("id");
        txtProfileName = view.findViewById(R.id.txtProfileName);
        txtProfileEmail = view.findViewById(R.id.txtProfileEmail);
        txtDOB = view.findViewById(R.id.txtProfileDOB);
        txtVettingDate = view.findViewById(R.id.profileVettingDate);
        txtProfileGroup = view.findViewById(R.id.profileGroup);
        txtProfilePhoneNum = view.findViewById(R.id.profilePhoneNum);
        btnEdit = view.findViewById(R.id.btnEdit);


        viewReview = view.findViewById(R.id.viewLeaderReviews);


        if (intentId == null) {
            intentId = mUser.getUid();
        }
        for (final String accountType : accountTypes) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Person").child(accountType);
            if (accountType.equals("Leader")) {
                myRef.addValueEventListener(new ValueEventListener() {
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

                            if (id.equals(userId)) {
                                Toast.makeText(getContext(), "leader found", Toast.LENGTH_LONG).show();
                                txtProfileName.setText(name);
                                txtProfileEmail.setText(email);
                                txtDOB.setText(DOB);
                                txtVettingDate.setText(vettingDate);
                                txtProfileGroup.setText(group);
                                txtProfilePhoneNum.setText(phone);
                                break;
                            }

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else if (accountType.equals("Parent")) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            String id = ds.getValue(Parent.class).getParentId();
                            String name = ds.getValue(Parent.class).getName();
                            String email = ds.getValue(Parent.class).getEmail();

                            String group = ds.getValue(Parent.class).getGroup();
                            String phone = ds.getValue(Parent.class).getPhone();


                            if(id != null){
                                if (id.equals(userId)) {
                                    child = ds.getValue(Parent.class).getChildId();
                                    txtProfileName.setText(name);
                                    txtProfileEmail.setText(email);
                                    txtProfileGroup.setText(group);
                                    txtProfilePhoneNum.setText(phone);

                                    TextInputLayout vettingDate = view.findViewById(R.id.text_input_layout_txtVetting);
                                    TextInputLayout dob = view.findViewById(R.id.text_input_layout_txtDOB);
                                    dob.setVisibility(View.INVISIBLE);
                                    txtDOB.setVisibility(View.INVISIBLE);
                                    vettingDate.setHint("Child");


                                    myRef = database.getReference("Person").child("Member");
                                    myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                                String id = ds.getValue(Member.class).getId();
                                                String name = ds.getValue(Member.class).getName();


                                                if (id != null) {
                                                    if (id.equals(child)) {
                                                        txtVettingDate.setText(name);
                                                        break;
                                                    }
                                                }


                                            }
                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    txtVettingDate.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent childDetails = new Intent(getContext(), MemberProfile.class);
                                            childDetails.putExtra("id", child);
                                            startActivity(childDetails);
                                        }
                                    });

                                    btnEdit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent updateParent = new Intent(getContext(), CreateParent.class);
                                            updateParent.putExtra("id", userId);
                                            updateParent.putExtra("type", "update");
                                            startActivity(updateParent);
                                        }
                                    });
                                }
                            }


                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Person").child("Leader");
        myRef.addValueEventListener(new ValueEventListener() {
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

                    if (id.equals(intentId)) {
                        Toast.makeText(getContext(), "leader found", Toast.LENGTH_LONG).show();
                        txtProfileName.setText(name);
                        txtProfileEmail.setText(email);
                        txtDOB.setText(DOB);
                        txtVettingDate.setText(vettingDate);
                        txtProfileGroup.setText(group);
                        txtProfilePhoneNum.setText(phone);
                        break;
                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;

    }

    public void openReviews(View v) {

    }



}