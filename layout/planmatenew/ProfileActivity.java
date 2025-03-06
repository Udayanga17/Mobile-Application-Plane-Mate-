package com.example.planmatenew;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ProfileActivity extends AppCompatActivity {

    private String receivedUsername; // Variable to store the username


    //retrieve
    private DatabaseHelper databaseHelper;
    private ImageView ivProfile;
    private EditText etCompany, etContactNo, etDesc, etLocation;


    //update
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap selectedBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Retrieve the username from Intent
        receivedUsername = getIntent().getStringExtra("USERNAME_KEY");

        //retrieve
        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Initialize UI elements
        ivProfile = findViewById(R.id.ivProfile);
        etCompany = findViewById(R.id.etCompany);
        etContactNo = findViewById(R.id.etContactNo);
        etDesc = findViewById(R.id.etDesc);
        etLocation = findViewById(R.id.etLocation);

        // Retrieve seller details using the phone number
        if (receivedUsername != null) {
            loadSellerData();
        } else {
            Toast.makeText(this, "No username received", Toast.LENGTH_SHORT).show();
        }

        //nav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;

            if (itemId == R.id.nav_profile) {
                return true;  // Stay on the current activity
            } else if (itemId == R.id.nav_home) {
                intent = new Intent(this, SellerDashboardActivity.class);
                intent.putExtra("USERNAME_KEY", receivedUsername);
            } else if (itemId == R.id.nav_reviews) {
                intent = new Intent(this, ReviewsActivity.class);
                intent.putExtra("USERNAME_KEY", receivedUsername);
            } else if (itemId == R.id.nav_catalog) {
                intent = new Intent(this, CatalogActivity.class);
                intent.putExtra("USERNAME_KEY", receivedUsername);
            } else if (itemId == R.id.nav_orders) {
                intent = new Intent(this, OrderActivity.class);
                intent.putExtra("USERNAME_KEY", receivedUsername);
            }

            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }

            return true;
        });


        //update
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            String companyName = etCompany.getText().toString().trim();
            String contactNumber = etContactNo.getText().toString().trim();
            String description = etDesc.getText().toString().trim();
            String location = etLocation.getText().toString().trim();

            byte[] imageBytes = null;
            if (selectedBitmap != null) {
                imageBytes = getImageBytes(selectedBitmap);
            } else {
                // If no new image is selected, use the existing one
                Cursor cursor = databaseHelper.getSellerByUsername(receivedUsername);
                if (cursor != null && cursor.moveToFirst()) {
                    imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SELLER_IMAGE));
                    cursor.close();
                }
            }

            boolean isUpdated = databaseHelper.updateUser(receivedUsername, companyName, contactNumber, location, description, imageBytes);

            if (isUpdated) {
                Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ProfileActivity.this, SellerDashboardActivity.class);
                intent.putExtra("USERNAME", receivedUsername.trim()); // Pass username
                startActivity(intent);

                // Optional: Close this activity so user can't navigate back
                finish();
            } else {
                Toast.makeText(ProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadSellerData() {

        Cursor cursor = databaseHelper.getSellerByUsername(receivedUsername);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                // Retrieve values from the cursor
                String companyName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SELLER_NAME));
                String contactNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SELLER_PHONE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SELLER_DESCRIPTION));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SELLER_ADDRESS));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SELLER_IMAGE));

                // Set data to UI elements
                etCompany.setText(companyName);
                etContactNo.setText(contactNumber);
                etDesc.setText(description);
                etLocation.setText(location);

                // Convert BLOB to Bitmap and display image
                if (imageBytes != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    ivProfile.setImageBitmap(bitmap);
                }

            } else {
                Toast.makeText(this, "Error Loading this page", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        } else {
            Toast.makeText(this, "Database error: Could not retrieve seller details", Toast.LENGTH_SHORT).show();
        }


    }


    //update
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                Uri imageUri = data.getData();
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ivProfile.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to select image", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //update
    private byte[] getImageBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }



}



