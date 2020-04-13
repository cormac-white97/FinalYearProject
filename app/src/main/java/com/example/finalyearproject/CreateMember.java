package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Objects.Member;

public class CreateMember extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference memberRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Member member;
    private Activity activity = this;

    String type;
    String memberProfileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_member);
        mAuth = FirebaseAuth.getInstance();

        Spinner memberSpinner = findViewById(R.id.memberGroup);
        final String groupTypeList[] = new String[]{"Please Select", "Beavers", "Cubs", "Scouts", "Ventures", "Rovers"};

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groupTypeList);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        memberSpinner.setAdapter(adapter);

        Intent updateIntent = getIntent();
        type = updateIntent.getStringExtra("type");
        if (type.equals("update")) {
            memberRef = database.getInstance().getReference("Person").child("Member");
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

                        EditText newfName = findViewById(R.id.txtMemName);
                        Spinner memGroup = findViewById(R.id.memberGroup);
                        EditText newDob = findViewById(R.id.txtDOB);
                        EditText newDom = findViewById(R.id.txtmemberDate);
                        EditText newNotes = findViewById(R.id.notes);

                        if (id.equals(memberProfileId)) {
                            Date dt = null;
                            newfName.setText(name);
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                            Date dobDate = new Date(Long.parseLong(DOB));
                            newDob.setText(String.valueOf(dobDate));

                            Date domDate = new Date(Long.parseLong(DOM));
                            newDom.setText(String.valueOf(domDate));

                            newNotes.setText(notes);

                            Button btnCreate = findViewById(R.id.btnCreateNew);
                            btnCreate.setVisibility(View.INVISIBLE);

                            break;


                        }

                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            Button btnUpdate = findViewById(R.id.btnUpdate);
            btnUpdate.setVisibility(View.INVISIBLE);
        }

    }

    public void createMember(View v) {
        memberRef = database.getInstance().getReference();
        mUser = mAuth.getCurrentUser();
        final ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please Wait");
        mProgress.show();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dobDate = null;
        Date domDate = null;

        EditText fName = findViewById(R.id.txtMemName);
        Spinner memGroup = findViewById(R.id.memberGroup);
        EditText dob = findViewById(R.id.txtDOB);
        EditText dom = findViewById(R.id.txtmemberDate);
        EditText notes = findViewById(R.id.notes);

        final String txtName = fName.getText().toString();
        final String txtMemGroup = memGroup.getSelectedItem().toString();
        String txtDobDateVal = dob.getText().toString();
        String txtDomDateVal = dom.getText().toString();
        try {
            dobDate = dateFormat.parse(txtDobDateVal);
            domDate = dateFormat.parse(txtDomDateVal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final String txtDobDate = String.valueOf(dobDate.getTime());
        final String txtDomDate = String.valueOf(domDate.getTime());
        final String txtNotes = notes.getText().toString();


        String id = mUser.getUid();
        member = new Member(id, txtName, txtMemGroup, txtDobDate, txtDomDate, txtNotes);
        memberRef.child("Person").child("Member").child(id).setValue(member);
        mProgress.dismiss();
        activity.finish();

    }

    public void updateMember(View v) {
        memberRef = database.getInstance().getReference();
        mUser = mAuth.getCurrentUser();
        final ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please Wait");
        mProgress.show();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dobDate = null;
        Date domDate = null;

        EditText fName = findViewById(R.id.txtMemName);
        Spinner memGroup = findViewById(R.id.memberGroup);
        EditText dob = findViewById(R.id.txtDOB);
        EditText dom = findViewById(R.id.txtmemberDate);
        EditText notes = findViewById(R.id.notes);

        final String txtName = fName.getText().toString();
        final String txtMemGroup = memGroup.getSelectedItem().toString();
        String txtDobDateVal = dob.getText().toString();
        String txtDomDateVal = dom.getText().toString();
        try {
            dobDate = dateFormat.parse(txtDobDateVal);
            domDate = dateFormat.parse(txtDomDateVal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final String txtDobDate = String.valueOf(dobDate.getTime());
        final String txtDomDate = String.valueOf(domDate.getTime());
        final String txtNotes = notes.getText().toString();


        String id = mUser.getUid();
        member = new Member(id, txtName, txtMemGroup, txtDobDate, txtDomDate, txtNotes);
        memberRef.child("Person").child("Member").child(memberProfileId).setValue(member);
        mProgress.dismiss();
        activity.finish();

    }
}
