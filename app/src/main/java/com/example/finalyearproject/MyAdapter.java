package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.ui.home.HomeFragment;
import com.example.finalyearproject.ui.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import Objects.EventObj;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<EventObj> values;
    public static final String MESSAGE_KEY1 ="text";
    public static final String ID_KEY ="position";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    private DatabaseReference personRef;
    double lat;
    double lng;

    HomeFragment h = new HomeFragment();
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
            myRef = database.getReference("Leader");

            dbIDs = h.getLeaderNameAndId();

        }



    }

    public String getNameByID(final String id){
        String nameValue = null;
        nameValue = dbIDs.get(id);

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
        final String id = values.get(position).getId();
        lat = values.get(position).getLat();
        lng = values.get(position).getLng();

        String organizerName = getNameByID(organizer);

        holder.txtHeader.setText(name);
        holder.txtLocaitonView.setText(location);
        if(organizerName != null){
            holder.txtOrganizerView.setText(organizerName);

        }

        holder.txtHeader.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(v.getContext(),name, Toast.LENGTH_SHORT ).show();
                //addItem(position);
                // call activity to pass the item position
                Bundle b = new Bundle();
                b.putString("Long",String.valueOf(lng));
                b.putString("Lat", String.valueOf(lat));
                Intent intent = new Intent(v.getContext(), ViewEvent.class );
                intent.putExtra(ID_KEY, id);
                intent.putExtras(b);
                v.getContext().startActivity(intent);

            }

        });

    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return values.size();
    }
}
