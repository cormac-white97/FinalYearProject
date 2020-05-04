package com.example.finalyearproject.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.R;
import com.example.finalyearproject.ViewDetails.ViewLeaderDetails;

import java.util.ArrayList;

import com.example.finalyearproject.Objects.Leader;

public class LeaderAdapter extends RecyclerView.Adapter<LeaderAdapter.MyViewHolder> {
    private ArrayList<Leader> values;
    public static final String MESSAGE_KEY1 ="text";
    public static final String MESSAGE_KEY2 ="position";
    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHeader;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtHeader = (TextView) itemView.findViewById(R.id.accountType);
        }
    }

    // Provide the dataset to the Adapter
    public LeaderAdapter(ArrayList<Leader> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.account_layout, parent, false);
//        View v = inflater.inflate(R.layout.account_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Leader type = values.get(position);
        holder.txtHeader.setText(type.getName());

        holder.txtHeader.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //addItem(position);
                // call activity to pass the item position
                Intent intent = new Intent(v.getContext(), ViewLeaderDetails.class);
                intent.putExtra("id", type.getLeaderId());
                intent.putExtra("type", "viewLeader");
                v.getContext().startActivity(intent);

            }

            public void addItem(int position, Leader item){
                values.add(position, item); //values is the ArrayList
                notifyItemInserted(position);
            }

            public void remove(int position){
                values.remove(position);
                notifyItemRemoved(position);
            }

            public void update(Leader newItem, int position){
                values.set(position, newItem);
                notifyItemChanged(position);
            }

        });

    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return values.size();
    }
}

