package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import Objects.Leader;
import Objects.Member;
import Objects.Parent;

public class Dashboard extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference leaderRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private AppBarConfiguration mAppBarConfiguration;
    private static final String TAG = "MainActivity";
    private String topic = "no_topic";
    private String accountType = "null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        boolean foundUser;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        List<String> typeList = Arrays.asList("Leader", "Parent","Member");

        for(String type : typeList){

            foundUser = findUser(type, navigationView);
            if(foundUser){
                break;
            }
        }
//        if(!accountType.equals("Leader")){
            //Menu nav_Menu = navigationView.getMenu();
            //nav_Menu.findItem(R.id.nav_tools).setVisible(false);
        //}

        FirebaseMessaging.getInstance().subscribeToTopic("Weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "SUCCESS SUBSCRIPTION";
                        if (!task.isSuccessful()) {
                            msg = "Subscription failed";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(Dashboard.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_group, R.id.nav_events,
                R.id.nav_create_account, R.id.nav_event_report, R.id.nav_leaders)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



    }

    public boolean findUser(final String type, final NavigationView navigationView){
        final boolean[] found = {false};
        final String[] email = new String[1];
        final String[] name = new String[1];
        final String[] personType = new String[1];
        final String[] group = new String[1];
        leaderRef = mDatabase.getReference("Person").child(type);
        leaderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = mAuth.getInstance().getCurrentUser();

                View headerView = navigationView.getHeaderView(0);
                final String currentUserEmail = mUser.getEmail();


                TextView userName = headerView.findViewById(R.id.logedInUser);
                TextView userType = headerView.findViewById(R.id.logedInType);
                TextView userGroup = headerView.findViewById(R.id.logedInGroup);
                TextView userEmail = headerView.findViewById(R.id.logedInEmail);
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(type.equals("Leader")){
                        email[0] = ds.getValue(Leader.class).getEmail();
                        name[0] = ds.getValue(Leader.class).getName();
                        group[0] = ds.getValue(Leader.class).getGroup();
                    }
                    else if(type.equals("Parent")){
                        email[0] = ds.getValue(Parent.class).getEmail();
                        name[0] = ds.getValue(Parent.class).getName();
                        personType[0] = type;
                        group[0] = ds.getValue(Parent.class).getGroup();
                    }
                    else if(type.equals("Member")){
                        name[0] = ds.getValue(Member.class).getName();
                        personType[0] = type;
                        group[0] = ds.getValue(Member.class).getGroup();
                    }



                    if(currentUserEmail.equals(email[0])){
                        String loggedName = name[0];
                        userName.setText(loggedName);
                        userType.setText(type);
                        userGroup.setText(group[0]);
                        userEmail.setText(email[0]);
                        found[0] = true;
                        topic = group[0] + "_" + type;
                        accountType = type;

                        if (type.equals("Parent")){
                            Menu nav_Menu = navigationView.getMenu();
                            nav_Menu.findItem(R.id.nav_event_report).setVisible(false);
                            nav_Menu.findItem(R.id.nav_group).setVisible(false);
                            nav_Menu.findItem(R.id.nav_create_account).setVisible(false);
                        }
                        else{
                            Menu nav_Menu = navigationView.getMenu();
                            nav_Menu.findItem(R.id.nav_payment_history).setVisible(false);

                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return found[0];
    }


    public void logOut(MenuItem item){
        mAuth.signOut();
        Intent logout = new Intent(Dashboard.this, MainActivity.class);
        startActivity(logout);
        finish();    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}