package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WeddingShopActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WeddingShopAdapter shopAdapter;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wedding_shop_fragment);

        // Get the selected category from the intent
        String category = getIntent().getStringExtra("category");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.wedshop_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize DatabaseHelper
        dbHandler = new DBHandler(this);

        // Fetch shops for the selected category
        List<WeddingShopModel> shops = dbHandler.getShopsByCategory(category);

        // Set adapter for shops
        shopAdapter = new WeddingShopAdapter(this, shops);
        recyclerView.setAdapter(shopAdapter);
    }
}