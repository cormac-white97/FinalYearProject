package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.ui.event.EventFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<EventObj> values;
    public static final String MESSAGE_KEY1 ="text";
    public static final String MESSAGE_KEY2 ="position";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    private HashMap<String, String> dbIDs = new HashMap<>();

    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHeader;
        public TextView txtLocaitonView;
        public TextView txtOrganizerView;



        public MyViewHolder(View itemView) {
            super(itemView);

            txtHeader = (TextView) itemView.findViewById(R.id.myTextView);
            txtLocaitonView = (TextView) itemView.findViewById(R.id.txtLocaitonView);
            txtOrganizerView = (TextView) itemView.findViewById(R.id.txtOrganizerView);

            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Person");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String dbID = ds.getValue(Person.class).getPersonID();
                        String dbFirstname = ds.getValue(Person.class).getFirstName();
                        String dbLastName = ds.getValue(Person.class).getLastName();
                        String name = dbFirstname + dbLastName;

                        dbIDs.put(dbID, name);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }



    }

    //TODO - finish this shit
    public String getNameByID(final String id){
        String nameValue = null;
        for (String dbId : dbIDs.keySet()){
            if(dbId.equals(id)){
                nameValue = String.valueOf(dbIDs.values());
            }
        }

        return nameValue;
    }


    // Provide the dataset to the Adapter
    public MyAdapter(List<EventObj> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = values.get(position).getGroup();
        final String location = values.get(position).getLocation();
        final String organizer = values.get(position).getCreatedBy();

        String organizerName = getNameByID(organizer);

        holder.txtHeader.setText(name);
        holder.txtLocaitonView.setText(location);
        if(organizerName != null){
            holder.txtOrganizerView.setText(organizerName);

        }

        holder.txtHeader.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(v.getContext(),name, Toast.LENGTH_SHORT ).show();
                //addItem(position);
                // call activity to pass the item position
                //Intent intent = new Intent(v.getContext(), UpdateActivity.class );
                //intent.putExtra(MESSAGE_KEY1 ,name);
               // intent.putExtra(MESSAGE_KEY2, position);
                //v.getContext().startActivity(intent);

            }

        });

    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return values.size();
    }
}
