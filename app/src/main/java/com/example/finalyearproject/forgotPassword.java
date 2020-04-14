package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {

    EditText txtEmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        txtEmailAddress = findViewById(R.id.txtResetEmail);

    }

    public void sendResetEmail(View v){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = txtEmailAddress.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(forgotPassword.this, "A password reset email has been sent", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(forgotPassword.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
