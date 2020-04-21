package com.example.finalyearproject.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.R;
import com.example.finalyearproject.ViewDetails.ViewEvent;

import java.util.ArrayList;

import com.example.finalyearproject.Objects.PaymentHistory;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.MyViewHolder> {
    private ArrayList<PaymentHistory> values;
    public static final String MESSAGE_KEY1 ="text";
    public static final String MESSAGE_KEY2 ="position";



    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtGroup;
        public TextView txtLocaitonView;
        public TextView txtAmount;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtLocaitonView = (TextView) itemView.findViewById(R.id.myTextView);
            txtGroup = (TextView) itemView.findViewById(R.id.txtLocaitonView);
            txtAmount = (TextView) itemView.findViewById(R.id.txtOrganizerView);
        }
    }

    // Provide the dataset to the Adapter
    public PaymentListAdapter(ArrayList<PaymentHistory> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);
//        View v = inflater.inflate(R.layout.account_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String eventId = values.get(position).getEventId();
        final String location = values.get(position).getLocation();
        final String group = values.get(position).getGroup();
        final double amount = values.get(position).getAmount();


        holder.txtLocaitonView.setText(location);
        holder.txtGroup.setText(group);
        holder.txtAmount.setText(String.valueOf(amount));

        holder.txtLocaitonView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //addItem(position);
                // call activity to pass the item position
                Intent intent = new Intent(v.getContext(), ViewEvent.class);
                intent.putExtra("ID_KEY", eventId);
                v.getContext().startActivity(intent);

            }

            public void addItem(int position, PaymentHistory item){
                values.add(position, item); //values is the ArrayList
                notifyItemInserted(position);
            }

            public void remove(int position){
                values.remove(position);
                notifyItemRemoved(position);
            }

            public void update(PaymentHistory newItem, int position){
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