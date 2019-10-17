package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String leaderKey = "com.FYP.loginLeader";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View v) {
        EditText uName = findViewById(R.id.email);
        EditText pWord = findViewById(R.id.password);

        String enteredUName = uName.getText().toString();
        String enteredPWord = pWord.getText().toString();

        if (enteredUName.equals("t") && enteredPWord.equals("p")) {
            String personName = "Cormac";
            Intent login = new Intent(this, Dashboard.class);
            login.putExtra(leaderKey, personName);
            startActivity(login);
        } else {
            Toast t = Toast.makeText(getApplicationContext(), "Invalid login", Toast.LENGTH_LONG);
            t.show();
        }
    }
}

