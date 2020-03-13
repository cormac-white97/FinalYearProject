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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateMember extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference memberRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Member member;
    private Activity activity = this;

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
        EditText email = findViewById(R.id.txtMemberEmail);
        EditText pword = findViewById(R.id.txtMemberPass);
        EditText confirm = findViewById(R.id.txtMemberConfirm);
        Spinner memGroup = findViewById(R.id.memberGroup);
        EditText dob = findViewById(R.id.txtDOB);
        EditText dom = findViewById(R.id.txtmemberDate);
        EditText notes = findViewById(R.id.notes);

        final String txtName = fName.getText().toString();
        final String txtEmail = email.getText().toString();
        final String txtPword = pword.getText().toString();
        String txtConfirm = confirm.getText().toString();
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

        if (txtPword.equals(txtConfirm) && mAuth != null) {
            memberRef = database.getInstance().getReference();
            mAuth.createUserWithEmailAndPassword(txtEmail, txtPword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(CreateMember.this, "Worked", Toast.LENGTH_LONG).show();
                                String id = mUser.getUid();
                                member = new Member(id, txtName, txtEmail, txtMemGroup, txtDobDate, txtDomDate, txtNotes, null);
                                memberRef.child("Person").child("Member").child(id).setValue(member);
                                mProgress.dismiss();
                                activity.finish();

                            } else {
                                mProgress.dismiss();
                                Toast.makeText(CreateMember.this, "Didn't Work", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        } else {
            Toast.makeText(CreateMember.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }


    }
}
