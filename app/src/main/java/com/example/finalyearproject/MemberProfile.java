package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.finalyearproject.ui.event.EventFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Objects.Member;
import Objects.Parent;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class MemberProfile extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference memberRef;

    private EditText txtName;
    private EditText txtGroup;
    private EditText txtAge;
    private EditText txtDom;
    private EditText txtParentName;
    private EditText txtParentPhone;
    private EditText txtNotes;

    private String memberProfileId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);

        Intent intent = getIntent();
        memberProfileId = intent.getStringExtra("id");
        txtName = findViewById(R.id.viewMemName);
        txtGroup = findViewById(R.id.viewMemGroup);
        txtAge = findViewById(R.id.viewMemDOB);
        txtDom = findViewById(R.id.viewMemDate);
        txtParentName = findViewById(R.id.viewParentName);
        txtParentPhone = findViewById(R.id.viewParentPhone);
        txtNotes = findViewById(R.id.viewNotes);


        database = FirebaseDatabase.getInstance();
        memberRef = database.getReference("Person").child("Member");
        memberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getValue(Member.class).getName();
                    String group = ds.getValue(Member.class).getGroup();
                    String id = ds.getValue(Member.class).getId();
                    String DOM = ds.getValue(Member.class).getMemDom();
                    String DOB = ds.getValue(Member.class).getMemDob();
                    String notes = ds.getValue(Member.class).getNotes();

                    if (id.equals(memberProfileId)) {
                        Date dt = null;
                        txtName.setText(name);
                        txtGroup.setText(group);
                        Date today = Calendar.getInstance().getTime();

                        Date dobDate = new Date(Long.parseLong(DOB));
                        int age = getDiffYears(dobDate,today);
                        txtAge.setText(String.valueOf(age));

                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                        Date date1 = new Date(Long.parseLong(DOM));

                        txtDom.setText(dateFormat.format(date1));

                        txtNotes.setText(notes);

                        break;



                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        memberRef = database.getReference("Person").child("Parent");
        memberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getValue(Parent.class).getName();
                    String phone = ds.getValue(Parent.class).getPhone();
                    String childId = ds.getValue(Parent.class).getChildId();


                    if (childId.equals(memberProfileId)) {
                       txtParentName.setText(name);
                       txtParentPhone.setText(phone);
                        break;

                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTime(date);
        return cal;
    }

    public void updateMember(View v){
        Intent intent = new Intent(MemberProfile.this, CreateMember.class);
        intent.putExtra("type", "update");
        intent.putExtra("memberId", memberProfileId);
        startActivity(intent);

    }
}
