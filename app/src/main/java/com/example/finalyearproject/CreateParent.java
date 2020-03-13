package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateParent extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference parentRef;
    private DatabaseReference memberRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    String id = null;
    String name = null;
    String email = null;
    String pass = null;
    String group;
    String memDob = null;
    String memDom = null;
    String notes = null;
    String fcmToken = null;
    String childId = null;
    String parentGroup = null;
    ArrayList<Member> allMember = new ArrayList<>();
    ArrayList<String> childList = new ArrayList<>();
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parent);

        childList.add("Select Paren's Child");

        mDatabase = FirebaseDatabase.getInstance();
        memberRef = mDatabase.getReference("Person").child("Member");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getInstance().getCurrentUser();

        getAllChildren();

        Spinner groupSpinner = findViewById(R.id.parentChild);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, childList);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        groupSpinner.setAdapter(adapter);
    }

    public void getAllChildren() {


        memberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    id = ds.getValue(Member.class).getId();
                    name = ds.getValue(Member.class).getName();
                    email = ds.getValue(Member.class).getEmail();
                    group = ds.getValue(Member.class).getGroup();
                    memDob = ds.getValue(Member.class).getMemDob();
                    memDom = ds.getValue(Member.class).getMemDom();
                    notes = ds.getValue(Member.class).getNotes();
                    fcmToken = ds.getValue(Member.class).getFcmToken();


                    String childName = name;

                    Member m = new Member(id, name, email, group, memDob, memDom, notes, fcmToken);
                    allMember.add(m);
                    childList.add(childName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void createNewParent(View v) {
        final ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please Wait");
        mProgress.show();
        EditText name = findViewById(R.id.txtParentName);
        EditText email = findViewById(R.id.txtParentEmail);
        EditText password = findViewById(R.id.txtParentPass);
        EditText confirm = findViewById(R.id.txtParentConfirm);
        EditText phone = findViewById(R.id.txtParentPhone);
        final Spinner child = findViewById(R.id.parentChild);

        final String txtname = name.getText().toString();
        final String txtEmail = email.getText().toString();
        final String txtPass = password.getText().toString();
        final String txtConfirm = confirm.getText().toString();
        final String txtPhone = phone.getText().toString();


        int i = 0;
        for (Member member : allMember) {

            if (member.getName().equals(child.getSelectedItem().toString())) {
                childId = member.getId();
                //Assign the parent to the same group as their child
                parentGroup = member.getGroup();
                break;
            }
            i++;
        }
        if (txtPass.equals(txtConfirm) && mAuth != null) {
            parentRef = mDatabase.getInstance().getReference();
            mAuth.createUserWithEmailAndPassword(txtEmail, txtPass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(CreateParent.this, "Worked", Toast.LENGTH_LONG).show();
                                String id = mUser.getUid();
                                Parent parent = new Parent(id, txtname, txtPhone, txtEmail, childId, parentGroup, " ");

                                parentRef.child("Person").child("Parent").child(id).setValue(parent);
                                mProgress.dismiss();
                                activity.finish();

                            } else {
                                mProgress.dismiss();
                                Toast.makeText(CreateParent.this, "Didn't Work", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        } else {
            Toast.makeText(CreateParent.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }

    }
}

