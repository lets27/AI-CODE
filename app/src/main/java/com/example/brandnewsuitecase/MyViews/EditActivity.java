package com.example.brandnewsuitecase.MyViews;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.brandnewsuitecase.Models.Product;
import com.example.brandnewsuitecase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String itemID;
    private String name, prices, descr;
    private EditText newName, newPrice, newDescription;
    private ImageView newImg;


    Button save;
    String imageURL;
    Uri uri;
    String key, oldImageUrl;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit2);

        newName = findViewById(R.id.editedName);
        newPrice = findViewById(R.id.editedPrice);
        newImg = findViewById(R.id.EditSnap);
        newDescription = findViewById(R.id.editedDescription);


        save = findViewById(R.id.addEditedItem);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            System.out.println(result.getResultCode());
                            Intent data = result.getData();
                            uri = data.getData();
                            newImg.setImageURI(uri);
                        } else {
                            Toast.makeText(EditActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        Bundle bundle= getIntent().getExtras();
        if(bundle !=null) {
            Glide.with(EditActivity.this).load(bundle.getString("image")).into(newImg);
            newName.setText(bundle.getString("itemName"));
            newPrice.setText(bundle.getString("price"));
            newDescription.setText(bundle.getString("description"));
            key = bundle.getString("key");

            oldImageUrl = bundle.getString("image");
        }


        newImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent=new Intent(EditActivity.this, homeScreen.class);
            startActivity(intent);
            }
        });

    }

    public void saveData() {
        if (uri != null) {
            storageReference = FirebaseStorage.getInstance().getReference().child("Shopping Items").child(uri.getLastPathSegment());

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri urlImage = task.getResult();
                                imageURL = urlImage.toString();
                                upDate();
                            } else {
                                // Handle failure
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle failure
                }
            });
        } else {
            // Handle case where no new image is selected
            upDate();
        }
    };

    public void upDate() {

        name = newName.getText().toString().trim();
        prices = newPrice.getText().toString().trim();
        descr = newDescription.getText().toString().trim();

        // Assuming you have a DatabaseReference for the specific item
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Shopping Items").child(key);

        // Update the values
        reference.child("itemName").setValue(name);
        reference.child("price").setValue(prices);
        reference.child("description").setValue(descr);
        reference.child("image").setValue(imageURL);

        // Finish the activity or handle success as needed
        finish();

    }

}
