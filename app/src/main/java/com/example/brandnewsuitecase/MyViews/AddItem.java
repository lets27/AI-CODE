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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.util.Calendar;

public class AddItem extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String itemID;
    private Button addBtn;
    private EditText itemName, itemPrice, description;
    private ImageView itemImg;
   // private Uri imageUri;

    String imageURL;
    Uri uri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        itemName = findViewById(R.id.productName);
        itemPrice = findViewById(R.id.editPrice);
        itemImg = findViewById(R.id.addsnap);
        description = findViewById(R.id.ItemDescription);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Shopping Items");
        addBtn = findViewById(R.id.addItem);


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            System.out.println(result.getResultCode());
                            Intent data = result.getData();
                            uri = data.getData();
                            itemImg.setImageURI(uri);
                        } else {
                            Toast.makeText(AddItem.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        itemImg.setOnClickListener(view -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);

        });

        addBtn.setOnClickListener(view -> {
            saveData();
        });

    }

    private void saveData() {

        if (uri != null) {
            StorageReference storageReference = FirebaseStorage
                    .getInstance()
                    .getReference()
                    .child("Images")
                    .child(uri.getLastPathSegment());

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri uriImage = uriTask.getResult();
                    imageURL = uriImage.toString();
                    uploadData();
                    //pending
                    //  dialog.dismiss();
                }
            });
        } else {
            uploadData();
            // you will need to account for this.
            // this solution is temporary
        }

    }
    public void uploadData() {
        String itemname = itemName.getText().toString();
        String price = itemPrice.getText().toString();
//        String img = (uri != null) ? uri.toString() : "";
        String describe = description.getText().toString();
        itemID = itemname;

        // initialise object with Strings from textfields
        Product model = new Product();
        model.setItemName(itemname);
        model.setDescription(describe);
        model.setPrice(price);
        model.setImage(imageURL);




        /*Note to always make the child a date when possible so that other information don't get
        affected*/
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());


        FirebaseDatabase.getInstance().getReference("Shopping Items").child(currentDate)
                .setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddItem.this, "Saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


