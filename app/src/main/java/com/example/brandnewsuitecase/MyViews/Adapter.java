package com.example.brandnewsuitecase.MyViews;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.brandnewsuitecase.Models.Product;
import com.bumptech.glide.Glide;
import com.example.brandnewsuitecase.R;


import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    
    int lastPos = 1;
    private ArrayList<Product> itemList;
    private Context context;
   // private CourseClickInterface courseClickInterface;

    public Adapter(ArrayList<Product> itemList, Context context /*CourseClickInterface courseClickInterface*/) {
        this.itemList = itemList;
        this.context = context;
        //this.courseClickInterface = courseClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout file
        View view = LayoutInflater.from(context).inflate(R.layout.itemlistview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product model = itemList.get(position);
        Glide.with(context).load(itemList.get(position).getImage()).into(holder.thisImageView);
        holder.itemNameview.setText(model.getItemName());
        holder.price.setText(model.getPrice());
    }
        //setAnimation(holder.itemView, position);

      /*  // Handle long press event
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Call the modified method to launch SendMessageActivity
                launchSendMessageActivity(model);
                return true;
            }
        });*/

      /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                courseClickInterface.onCourseClick(clickedPosition);
            }
        });
    }

  /*  private void launchSendMessageActivity(Product model) {
        // Modify this part to launch SendMessageActivity
        // Use context to start the new activity and pass the selected course details
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra("courseDetails", model);
        context.startActivity(intent);
    }

    public void setAnimation(View itemView, int position) {
        if (position > lastPos) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        } else {
            itemView.clearAnimation();
        }
    }
*/
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemNameview, price;
        private ImageView thisImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameview = itemView.findViewById(R.id.nameview);
            price = itemView.findViewById(R.id.priceView);
            thisImageView = itemView.findViewById(R.id.viewImage);
        }
    }

 /*   public interface CourseClickInterface {
        void onCourseClick(int position);
    }*/
}