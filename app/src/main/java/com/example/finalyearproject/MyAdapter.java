package com.example.finalyearproject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.ui.event.EventFragment;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<EventObj> values;
    public static final String MESSAGE_KEY1 ="text";
    public static final String MESSAGE_KEY2 ="position";
    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHeader;
        public TextView txtLocaitonView;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtHeader = (TextView) itemView.findViewById(R.id.myTextView);
            txtLocaitonView = (TextView) itemView.findViewById(R.id.txtLocaitonView);

        }


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
        holder.txtHeader.setText(name);
        holder.txtLocaitonView.setText(location);

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
