package com.example.planmatenew;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ReviewsActivity extends AppCompatActivity {

    private String receivedUsername; // Variable to store the username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        // Retrieve the username from Intent
        receivedUsername = getIntent().getStringExtra("USERNAME_KEY");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;

            if (itemId == R.id.nav_reviews) {
                return true;  // Stay on the current activity
            } else if (itemId == R.id.nav_home) {
                intent = new Intent(this, SellerDashboardActivity.class);
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
    }
}
