package com.example.brandnewsuitecase.MyViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.brandnewsuitecase.Models.Product;
import com.example.brandnewsuitecase.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AddItem extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String itemID;
    Button addBtn;

    EditText itemName,itemPrice,itemImg,description;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        itemName=findViewById(R.id.productName);
        itemPrice=findViewById(R.id.editPrice);
        itemImg=findViewById(R.id.imageUrl);

        description=findViewById(R.id.ItemDescription);
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("courses");
        addBtn = findViewById(R.id.addItem);

addBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        String itemname=itemName.getText().toString();
        String price=itemPrice.getText().toString();
        String img=itemImg.getText().toString();
        String describe=description.getText().toString();

        itemID= itemname;

        //initialise object with Strings from textfields
        Product model=new Product();
                    model.setItemName(itemname);
                    model.setDescription(describe);
                    model.setPrice(img);
                    model.setImage(img);
                    model.setItemID(itemID);


        System.out.println(model.getItemName());
        System.out.println(model.getPrice());
        //adding our object to DB
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //proBar.setVisibility(View.GONE);
                databaseReference.child(itemID).setValue(model);//passing object to db via courseID as key
                Toast.makeText(AddItem.this, "Course Added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddItem.this, homeScreen.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddItem.this, "error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
});


    }
}