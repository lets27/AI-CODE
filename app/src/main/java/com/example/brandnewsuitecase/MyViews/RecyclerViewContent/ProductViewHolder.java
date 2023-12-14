package com.example.brandnewsuitecase.MyViews.RecyclerViewContent;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brandnewsuitecase.Models.Image;
import com.example.brandnewsuitecase.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    ImageView viewImage;
    TextView nameView, priceView;
    CardView myCrdItem;
    ImageView delete,edit;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        viewImage =  itemView.findViewById(R.id.viewImage);
        nameView =  itemView.findViewById(R.id.nameView);
        priceView = itemView.findViewById(R.id.priceView);
        myCrdItem=itemView.findViewById(R.id.myCardview);
        delete=itemView.findViewById(R.id.deleteBtn);
        edit=itemView.findViewById(R.id.editBtn);

    }
}
