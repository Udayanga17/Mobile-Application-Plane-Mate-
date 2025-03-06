package com.example.planmatenew;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SellerDashboardActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView tvNameDash;
    private ImageView ivProfileImageDash;

    private String receivedUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;

            if (itemId == R.id.nav_home) {
                return true;  // Stay on the current activity
            } else if (itemId == R.id.nav_reviews) {
                intent = new Intent(this, ReviewsActivity.class);
                intent.putExtra("USERNAME_KEY", receivedUsername);
            } else if (itemId == R.id.nav_catalog) {
                intent = new Intent(this, CatalogActivity.class);
                intent.putExtra("USERNAME_KEY", receivedUsername);
            } else if (itemId == R.id.nav_orders) {
                intent = new Intent(this, OrderActivity.class);
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

        // Initialize Database Helper
        databaseHelper = new DatabaseHelper(this);

        // Initialize Views
        tvNameDash = findViewById(R.id.tvNameDash);
        ivProfileImageDash = findViewById(R.id.ivProfileImageDash);

        // Get the phone number from the intent
        String username = getIntent().getStringExtra("USERNAME");

        // Retrieve seller details using the phone number
        if (username != null) {
            loadSellerDetails(username);
        } else {
            Toast.makeText(this, "No username received", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadSellerDetails(String username) {
        Cursor cursor = databaseHelper.getSellerByUsername(username);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                // Get data from cursor
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SELLER_NAME));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SELLER_IMAGE));
                receivedUsername = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));

                // Set data to UI elements
                tvNameDash.setText(name);

                // Convert BLOB to Bitmap and display image
                if (imageBytes != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    ivProfileImageDash.setImageBitmap(bitmap);
                }

            } else {
                Toast.makeText(this, "No seller found with this username", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        } else {
            Toast.makeText(this, "Database error: Could not retrieve seller details", Toast.LENGTH_SHORT).show();
        }
    }

    public void LogOut(View view) {

        Intent IntentObj = new Intent(this, MainActivity.class);
        startActivity(IntentObj);

    }
}
