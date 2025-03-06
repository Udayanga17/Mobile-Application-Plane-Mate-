package com.example.planmatenew;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

//recyclerview
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {

    private String receivedUsername; // Variable to store the username

    //recyclerview
    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Retrieve the username from Intent
        receivedUsername = getIntent().getStringExtra("USERNAME_KEY");

        //recyclerview
        recyclerView = findViewById(R.id.recyclerViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        itemList = getAllItems();

        itemAdapter = new ItemAdapter(this, itemList);
        recyclerView.setAdapter(itemAdapter);

        ////

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;

            if (itemId == R.id.nav_catalog) {
                return true;  // Stay on the current activity
            } else if (itemId == R.id.nav_home) {
                intent = new Intent(this, SellerDashboardActivity.class);
                intent.putExtra("USERNAME_KEY", receivedUsername);
            } else if (itemId == R.id.nav_reviews) {
                intent = new Intent(this, ReviewsActivity.class);
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

    ///recyclerview
    private List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllItems(receivedUsername); // Pass received username

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("item_name");
            int priceIndex = cursor.getColumnIndex("price");
            int imageIndex = cursor.getColumnIndex("image");

            if (idIndex != -1 && nameIndex != -1 && priceIndex != -1 && imageIndex != -1) {
                do {
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    double price = cursor.getDouble(priceIndex);
                    byte[] image = cursor.getBlob(imageIndex);

                    items.add(new Item(id, name, price, image));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        return items;
    }




    ////

    public void AddItem(View view) {

        Intent IntentObj = new Intent(this, AddItemActivity.class);
        IntentObj.putExtra("USERNAME_KEY", receivedUsername);
        startActivity(IntentObj);

    }
}

