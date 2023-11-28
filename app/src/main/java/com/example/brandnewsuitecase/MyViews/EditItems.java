package com.example.brandnewsuitecase.MyViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.brandnewsuitecase.R;
import com.example.brandnewsuitecase.Models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditItems extends AppCompatActivity {
    Button editItem, deleteItem;

    FirebaseDatabase firebaseDatabase;

    EditText itemName, itemPrice, itemDescribe, itemImage;
    DatabaseReference databaseReference;
    Product model;
    String id;

    String itemID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);


        itemName = findViewById(R.id.productName);
        itemPrice = findViewById(R.id.editPrice);
        itemImage = findViewById(R.id.imageUrl);

        itemDescribe = findViewById(R.id.itemDescription);

        firebaseDatabase = FirebaseDatabase.getInstance();

        editItem = findViewById(R.id.addItem);
        deleteItem = findViewById(R.id.deleBtn);

        //getting data from the addcourse activity
        model = getIntent().getParcelableExtra("course");
        //check if the object is empty
// if not empty we initialise parameters from the blue print
        if (model != null) {
            itemName.setText(model.getItemName());
            itemPrice.setText(model.getPrice());
            itemImage.setText(model.getImage());
            itemDescribe.setText(model.getDescription());
            id = model.getItemID();

            if (id != null && !id.isEmpty()) {
                databaseReference = firebaseDatabase.getReference("courses").child(id);
            } else {
                // Handle the case where courseID is not valid.
            }
        }


        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String nameItem = itemName.getText().toString();
                String priceItem = itemPrice.getText().toString();
                String imgItem = itemImage.getText().toString();
                String descri = itemDescribe.getText().toString();

                String id = nameItem;

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("itemName", nameItem);
                map.put("itemPrice", priceItem);
                map.put("describe", descri);
                map.put("img", imgItem);
                map.put("itemID", id);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (databaseReference != null) {
                            databaseReference.updateChildren(map, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error == null) {
                                        Toast.makeText(EditItems.this, "Course updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(EditItems.this, customListView.class));
                                    } else {
                                        Toast.makeText(EditItems.this, "Failed to update", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(EditItems.this, "Database reference is null", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditItems.this, "Failed to update", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

        private void delete(){
            databaseReference.removeValue();
            Toast.makeText(EditItems.this, "course deleted", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EditItems.this, customListView.class));
        }
    }







