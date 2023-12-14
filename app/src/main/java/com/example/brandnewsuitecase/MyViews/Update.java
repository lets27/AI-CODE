//package com.example.brandnewsuitecase.MyViews;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.brandnewsuitecase.Models.Product;
import com.example.brandnewsuitecase.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

/*public class Update extends AppCompatActivity {

    Button editItem, deleteItem;
    FirebaseDatabase firebaseDatabase;
    EditText itemName, itemPrice, itemDescribe;
    DatabaseReference databaseReference;
    Product model;
    String id;
    private ImageView itemImg;
    private Uri imageUri;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("courses");
        itemName = findViewById(R.id.productName);
        itemPrice = findViewById(R.id.editPrice);
        itemImg = findViewById(R.id.addsnaper);
        itemDescribe = findViewById(R.id.itemDescription);
        editItem = findViewById(R.id.addItem);
        deleteItem = findViewById(R.id.deleBtn);

        model = getIntent().getParcelableExtra("selectedProduct");

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null && data.getData() != null) {
                                imageUri = data.getData();
                                itemImg.setImageURI(imageUri);
                            }
                        } else {
                            Toast.makeText(Update.this, "Image not selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        if (model != null) {
            itemName.setText(model.getItemName());
            itemPrice.setText(model.getPrice());
            Glide.with(Update.this).load(model.getImage()).into(itemImg);
            itemDescribe.setText(model.getDescription());
            id = model.getItemID();

            if (id != null && !id.isEmpty()) {
                databaseReference = firebaseDatabase.getReference("courses").child(id);
            } else {
                // Handle the case where courseID is not valid.
            }
        } else {
            Log.e("EditItems", "Model is null");
        }

        itemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                photoPicker.addCategory(Intent.CATEGORY_OPENABLE);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem();
            }
        });

        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

    private void updateItem() {

        String nameItem = itemName.getText().toString();
        String priceItem = itemPrice.getText().toString();
        String imgItem = (imageUri != null) ? imageUri.toString() : "";
        String descri = itemDescribe.getText().toString();

        if (model != null && model.getItemID() != null) {
            String id = model.getItemID();

            Map<String, Object> map = new HashMap<>();
            map.put("itemName", nameItem);
            map.put("itemPrice", priceItem);
            map.put("description", descri);
            map.put("image", imgItem);
            map.put("itemID", id);

            databaseReference.setValue(map, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error == null) {
                        Toast.makeText(Update.this, "Item updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Update.this, homeScreen.class));
                    } else {
                        Toast.makeText(Update.this, "Failed to update", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Handle the case where model or model.getItemID() is null
            Toast.makeText(Update.this, "Failed to update - Model or ID is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void delete() {
        databaseReference.removeValue();
        Toast.makeText(Update.this, "Item deleted", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Update.this, homeScreen.class));
    }
}



/*{
  "rules": {
    ".read": "now < 1703628000000",  // 2023-12-27
    ".write": "now < 1703628000000",  // 2023-12-27
  }
}*/



