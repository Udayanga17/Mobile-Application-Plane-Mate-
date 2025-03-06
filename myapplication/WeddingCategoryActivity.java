package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WeddingCategoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    WeddingCategoryAdapter  categoryAdapter;
    List<WeddingCategoryModel> weddingCategoryModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wedding_category_fragment);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.wedding_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load categories
        weddingCategoryModelList = new ArrayList<>();
        weddingCategoryModelList.add(new WeddingCategoryModel(R.drawable.wedding_cake, "Wedding Cake", "wedding cake"));
        weddingCategoryModelList.add(new WeddingCategoryModel(R.drawable.deco_cat, "Decoration", "decoration"));
        weddingCategoryModelList.add(new WeddingCategoryModel(R.drawable.catering_cat, "Catering", "catering"));
        weddingCategoryModelList.add(new WeddingCategoryModel(R.drawable.hotel_cat, "Hotel", "hotel"));
        weddingCategoryModelList.add(new WeddingCategoryModel(R.drawable.band_cat, "Bands", "band"));
        weddingCategoryModelList.add(new WeddingCategoryModel(R.drawable.photography_wed_cat, "Photography", "photography"));

        // Set adapter for categories
        categoryAdapter = new WeddingCategoryAdapter(this, weddingCategoryModelList);
        recyclerView.setAdapter(categoryAdapter);
    }

}
