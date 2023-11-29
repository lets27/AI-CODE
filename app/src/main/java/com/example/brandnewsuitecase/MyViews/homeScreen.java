package com.example.brandnewsuitecase.MyViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.brandnewsuitecase.Models.Product;
import com.example.brandnewsuitecase.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class homeScreen extends AppCompatActivity implements Adapter.ItemClickInterface {

    //private FirebaseAuth auth;
    private Button btn;
    private TextView txtUser;
    //private FirebaseUser user;

    private RecyclerView ReView;
    private FloatingActionButton addBtn;
    private FirebaseDatabase firebaseDatabase;
    private RelativeLayout botSheet;
    private ArrayList<Product> modelList;
    private DatabaseReference databaseReference;

    private FloatingActionButton add;


    private  Adapter itemAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        botSheet = findViewById(R.id.bmSheet);
        ReView = findViewById(R.id.myRecycView);
        add = findViewById(R.id.floating);

        modelList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("courses");
        itemAdapter = new Adapter(modelList, homeScreen.this,this);

        // set recycler view
        if (ReView != null) {
            ReView.setLayoutManager(new LinearLayoutManager(this));
            ReView.setAdapter(itemAdapter);


        }

        // Set up the click listener for the FloatingActionButton
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the AddItem activity
                startActivity(new Intent(homeScreen.this, AddItem.class));
            }
        });

        // Fetch data from Firebase
        getAllCourses();


    }

    public void getAllCourses() {
        modelList.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product newProduct = snapshot.getValue(Product.class);
                boolean containsProduct = false;
                if (newProduct != null) {
                    // Check if the modelList is empty
                    if (modelList.isEmpty()) {
                        modelList.add(newProduct);
                        itemAdapter.notifyDataSetChanged();
                        return; // Exit the method if the list was empty
                    }

                    // Check if the modelList already contains the product

                    for (Product existingProduct : modelList) {
                        if (existingProduct != null && existingProduct.getItemID() != null &&
                                existingProduct.getItemID().equals(newProduct.getItemID())) {
                            containsProduct = true;
                            break;
                        }

                    }
                }
                    // Add the product to the modelList if it's not already present
                    if (!containsProduct) {
                        modelList.add(newProduct);
                        itemAdapter.notifyDataSetChanged();

                    }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                itemAdapter.notifyDataSetChanged();
            }
        });
    }




    @Override
    public void onItemClick(int position) {// we need to make sure the Product object gets parsed
        Product selectedProduct = modelList.get(position);
        Intent intent = new Intent(homeScreen.this, EditItems.class);
        intent.putExtra("selectedProduct", selectedProduct);
        startActivity(intent);
        //displaySheet(modelList.get(position));
    }

    private  void displaySheet(Product model) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottomsheet, botSheet);

        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        TextView descri;
        TextView itemname = layout.findViewById(R.id.title);
        ImageView image = layout.findViewById(R.id.myView);
        TextView price = layout.findViewById(R.id.price);
        descri = layout.findViewById(R.id.bmdescribe);

        Button edit = layout.findViewById(R.id.bmEdit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent(homeScreen.this,EditItems.class);
                intent.putExtra("item",model);
                startActivity(intent);

            }
        });
    }




}



