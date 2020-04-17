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

import Objects.Member;
import Objects.Parent;

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
    String parentId;
    String type;

    String txtname;
    String txtEmail;
    String txtPass;
    String txtConfirm;
    String txtPhone;

    ArrayList<Member> allMember = new ArrayList<>();
    ArrayList<String> childList = new ArrayList<>();
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parent);

        mDatabase = FirebaseDatabase.getInstance();
        memberRef = mDatabase.getReference("Person").child("Member");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getInstance().getCurrentUser();

        Intent i = getIntent();
        parentId = i.getStringExtra("id");
        type = i.getStringExtra("type");

        if (type.equals("update")) {
            Toast.makeText(this, parentId, Toast.LENGTH_SHORT).show();
            parentRef = mDatabase.getInstance().getReference("Person").child("Parent");
            parentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String id = ds.getValue(Parent.class).getParentId();
                        String txtName = ds.getValue(Parent.class).getName();
                        String txtEmail = ds.getValue(Parent.class).getEmail();
                        String txtPhone = ds.getValue(Parent.class).getPhone();
                        String txtChildId = ds.getValue(Parent.class).getChildId();
                        String txtParentGroup = ds.getValue(Parent.class).getGroup();


                        EditText name = findViewById(R.id.txtParentName);
                        EditText email = findViewById(R.id.txtParentEmail);
                        EditText password = findViewById(R.id.txtParentPass);
                        EditText confirm = findViewById(R.id.txtParentConfirm);
                        EditText phone = findViewById(R.id.txtParentPhone);

                        //The password can be updated from the log in page
                        password.setVisibility(View.GONE);
                        confirm.setVisibility(View.GONE);


                        if (id.equals(parentId)) {

                            name.setText(txtName);
                            email.setText(txtEmail);
                            phone.setText(txtPhone);
                            childId = txtChildId;
                            parentGroup = txtParentGroup;

                            Button btnCreate = findViewById(R.id.create_parent_account);
                            btnCreate.setVisibility(View.INVISIBLE);

                            break;


                        }

                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Button btnUpdate = findViewById(R.id.btnParentUpdate);
            btnUpdate.setVisibility(View.INVISIBLE);
        }
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


    public void updateParent(View v) {
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


        if (txtPass.equals(txtConfirm) && mAuth != null) {
            parentRef = mDatabase.getInstance().getReference();

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
}

