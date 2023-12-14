package com.example.brandnewsuitecase.MyViews;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.brandnewsuitecase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ItemDetails extends AppCompatActivity {

    private TextView names, prices, descrip;
    private ImageView snapper;
    private Button delete, edit;
    private String key = "";
    private String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        names = findViewById(R.id.gotName);
        prices = findViewById(R.id.gotPrice);
        descrip = findViewById(R.id.gotDescrip);
        snapper = findViewById(R.id.detailImage);
        delete = findViewById(R.id.deleto);
        edit = findViewById(R.id.toEdit);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Glide.with(this).load(bundle.getString("image")).into(snapper);
            names.setText(bundle.getString("itemName"));
            prices.setText(bundle.getString("price"));
            descrip.setText(bundle.getString("description"));
            key = bundle.getString("key");
            imageUrl = bundle.getString("image");

        }

        delete.setOnClickListener(v -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Shopping Items");
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
            storageReference.delete().addOnSuccessListener(unused -> {
                reference.child(key).removeValue();
                Toast.makeText(ItemDetails.this, "Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), homeScreen.class));
                finish();
            });

        });

edit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(ItemDetails.this,EditActivity.class)
                .putExtra("itemName",names.getText().toString())
                .putExtra("price",prices.getText().toString())
                .putExtra("description",descrip.getText().toString())
                .putExtra("image",imageUrl)
                .putExtra("key",key);
                startActivity(intent);
    }
});

    }
}
