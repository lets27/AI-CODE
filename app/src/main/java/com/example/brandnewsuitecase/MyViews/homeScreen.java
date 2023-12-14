package com.example.brandnewsuitecase.MyViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.brandnewsuitecase.MyViews.AddItem;
import com.example.brandnewsuitecase.Models.Product;
import com.example.brandnewsuitecase.MyViews.RecyclerViewContent.ProductAdapter;
import com.example.brandnewsuitecase.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class homeScreen extends AppCompatActivity{
        ValueEventListener valueEventListener;
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private DatabaseReference databaseReference;
    private List<Product> productList;
    private ProductAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        recyclerView = findViewById(R.id.myRecycView);
        addButton = findViewById(R.id.floating);
        productList = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(homeScreen.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        productList = new ArrayList<>();
        adapter = new ProductAdapter(homeScreen.this, productList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // get item position and then call delete
                int position = viewHolder.getAdapterPosition();
                deleteItem(position);
            }
        });
        //ataching our swipe helper to the recyclerview
        helper.attachToRecyclerView(recyclerView);

        // Set up Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Shopping Items");

        // crating ValueEventListener to populate the RecyclerView
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Product product = itemSnapshot.getValue(Product.class);
                    if (product != null) {
                        product.setKey(itemSnapshot.getKey());
                        productList.add(product);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });

        // Set up FloatingActionButton click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(homeScreen.this, AddItem.class));
            }
        });
    }

    private void deleteItem(int position) {
        //getting the position of the item
        if (position >= 0 && position < productList.size()) {
            Product productToDelete = productList.get(position);

            // Remove the item from Firebase
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Shopping Items");
            reference.child(productToDelete.getKey()).removeValue();

            // Remove the item from the list and notify the adapter
            productList.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // removing ValueEventListener when the activity is destroyed
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }
}

//
