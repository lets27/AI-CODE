package com.example.brandnewsuitecase.MyViews;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.brandnewsuitecase.Models.Image;
import com.example.brandnewsuitecase.Models.Product;
import com.example.brandnewsuitecase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddItem extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String itemID;
    private Button addBtn;
    private EditText itemName, itemPrice, description;
    private ImageView itemImg;
    private Uri imageUri;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        itemName = findViewById(R.id.productName);
        itemPrice = findViewById(R.id.editPrice);
        itemImg = findViewById(R.id.addsnap);
        description = findViewById(R.id.ItemDescription);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("courses");
        addBtn = findViewById(R.id.addItem);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new
                ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    imageUri = data.getData();
                    itemImg.setImageURI(imageUri);
                } else {
                    Toast.makeText(AddItem.this, "image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        itemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    upLoadToFireBase(imageUri);//loads items to firebase storage
                } else {
                    Toast.makeText(AddItem.this, "select image", Toast.LENGTH_SHORT).show();
                }

                String itemname = itemName.getText().toString();
                String price = itemPrice.getText().toString();
                String img = (imageUri != null) ? imageUri.toString() : "";
                String describe = description.getText().toString();
                itemID = itemname;

                // initialise object with Strings from textfields
                Product model = new Product();
                model.setItemName(itemname);
                model.setDescription(describe);
                model.setPrice(price);
                model.setImage(img);
                model.setItemID(itemID);


                // adding our object to DB
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child(itemID).setValue(model);
                        Toast.makeText(AddItem.this, "Course Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddItem.this, homeScreen.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddItem.this, "error" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void upLoadToFireBase(Uri uri) {
        final StorageReference imageRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtention(uri));

        imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Image imgClass = new Image(uri.toString());
                        String key = databaseReference.push().getKey();
                        databaseReference.child(key).setValue(imgClass);
                        Toast.makeText(AddItem.this, "uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private String getFileExtention(Uri fileUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}