package com.example.finalyearproject.Adapters;

        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.PorterDuff;
        import android.graphics.drawable.Drawable;
        import android.graphics.drawable.LayerDrawable;
        import android.media.Rating;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.RatingBar;
        import android.widget.TextView;

        import androidx.recyclerview.widget.RecyclerView;

        import com.example.finalyearproject.CreationClasses.AddReview;
        import com.example.finalyearproject.R;

        import java.util.ArrayList;

        import com.example.finalyearproject.Objects.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private ArrayList<Review> values;
    public static final String MESSAGE_KEY1 ="text";
    public static final String MESSAGE_KEY2 ="position";
    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHeader;
        public RatingBar txtRating;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtHeader = (TextView) itemView.findViewById(R.id.viewReviewTitle);
            txtRating = (RatingBar) itemView.findViewById(R.id.viewRating);
            Drawable stars = txtRating.getProgressDrawable();
            stars.setTint( Color.BLUE );

        }
    }

    // Provide the dataset to the Adapter
    public ReviewAdapter(ArrayList<Review> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.review_layout, parent, false);
//        View v = inflater.inflate(R.layout.account_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String type = values.get(position).getTitle();
        final int rating = values.get(position).getRating();
        holder.txtHeader.setText(type);
        holder.txtRating.setNumStars(rating);

        holder.txtHeader.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                    Intent intent = new Intent(v.getContext(), AddReview.class);
                    intent.putExtra("type", "view");
                    intent.putExtra("reviewId", values.get(position).getReviewId());
                    v.getContext().startActivity(intent);
            }

            public void addItem(int position, Review item){
                values.add(position, item); //values is the ArrayList
                notifyItemInserted(position);
            }

            public void remove(int position){
                values.remove(position);
                notifyItemRemoved(position);
            }

            public void update(Review newItem, int position){
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


