package com.example.planmatenew;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private Bitmap selectedBitmap = null;



    EditText etNo, etName, etDescription, etPrice;
    ImageView ivItem;
    Button btnAdd;
    DatabaseHelper databaseHelper;
    private static final int IMAGE_PICK_CODE = 100;
    private Uri imageUri;


    private String receivedUsername; // Variable to store the username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Retrieve the username from Intent
        receivedUsername = getIntent().getStringExtra("USERNAME_KEY");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;

            if (itemId == R.id.nav_orders) {
                intent = new Intent(this, OrderActivity.class);
                intent.putExtra("USERNAME_KEY", receivedUsername);
            } else if (itemId == R.id.nav_home) {
                intent = new Intent(this, SellerDashboardActivity.class);
                intent.putExtra("USERNAME_KEY", receivedUsername);
            } else if (itemId == R.id.nav_reviews) {
                intent = new Intent(this, ReviewsActivity.class);
                intent.putExtra("USERNAME_KEY", receivedUsername);
            } else if (itemId == R.id.nav_catalog) {
                intent = new Intent(this, CatalogActivity.class);
                intent.putExtra("USERNAME_KEY", receivedUsername);
            } else if (itemId == R.id.nav_profile) {
                intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("USERNAME_KEY", receivedUsername);
            }

            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }

            return true;
        });


        databaseHelper = new DatabaseHelper(this);

        etNo = findViewById(R.id.etNo);
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        ivItem = findViewById(R.id.ivItem);
        btnAdd = findViewById(R.id.button2);

        profileImageView = findViewById(R.id.ivItem);
        TextView selectImageButton = findViewById(R.id.txt_4);



        // Open gallery on button click
        selectImageButton.setOnClickListener(v -> openGallery());
        btnAdd.setOnClickListener(v -> addItem());

    }


//    public byte[] imageViewToByte(ImageView imageView) {
//        // Convert ImageView content to Bitmap
//        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//
//        // Create an output stream to hold the byte data
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//        // Compress the Bitmap into the output stream as PNG (you can use JPEG too)
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//
//        // Convert the output stream to byte array
//        return stream.toByteArray();
//    }


    // Convert Bitmap to Byte Array
    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    //addItem
    private void addItem() {
        String itemNo = etNo.getText().toString().trim();
        String itemName = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String username = receivedUsername.trim();

        // Validate user input
        if (itemNo.isEmpty() || itemName.isEmpty() || description.isEmpty() || price.isEmpty() || ivItem.getDrawable() == null) {
            Toast.makeText(this, "Please fill all fields and select an image!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert ImageView to byte array
        byte[] imageBytes = convertBitmapToByteArray(selectedBitmap);

        // Insert into SQLite
        boolean isInserted = databaseHelper.insertItem(itemNo, itemName, description, price, username, imageBytes);
        if (isInserted) {

            Toast.makeText(this, "Item added Successfully", Toast.LENGTH_SHORT).show();

            // Navigate to NextActivity
            Intent intent = new Intent(AddItemActivity.this, CatalogActivity.class);
            intent.putExtra("USERNAME", receivedUsername.trim()); // Pass username
            startActivity(intent);

            // Optional: Close this activity so user can't navigate back
            finish();

        } else {
            Toast.makeText(this, "Error adding item ", Toast.LENGTH_SHORT).show();
        }
    }


    // Method to open the gallery and pick an image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri); // Assign it here
                profileImageView.setImageBitmap(selectedBitmap); // Display the image
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
