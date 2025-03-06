package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddShopActivity extends AppCompatActivity {

    private EditText etShopName, etShopCategory, etShopImage, etEventType;
    private Button btnAddShop;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);

        // Initialize views
        etShopName = findViewById(R.id.et_shopName);
        etShopCategory = findViewById(R.id.et_shopCategory);
        etShopImage = findViewById(R.id.et_shopImage);
        etEventType = findViewById(R.id.et_eventType);
        btnAddShop = findViewById(R.id.btn_addShop);

        // Initialize DBHandler
        dbHandler = new DBHandler(this);

        // Set click listener for the "Add Shop" button
        btnAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShopToDatabase();
            }
        });
    }

    private void addShopToDatabase() {
        // Get input values
        String shopName = etShopName.getText().toString().trim();
        String shopCategory = etShopCategory.getText().toString().trim();
        String shopImage = etShopImage.getText().toString().trim();
        String eventType = etEventType.getText().toString().trim();

        // Validate inputs
        if (shopName.isEmpty() || shopCategory.isEmpty() || shopImage.isEmpty() || eventType.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add shop to the database
        dbHandler.addNewShop(shopName, shopCategory, shopImage, eventType);

        // Show success message
        Toast.makeText(this, "Shop added successfully", Toast.LENGTH_SHORT).show();

        // Clear input fields
        etShopName.setText("");
        etShopCategory.setText("");
        etShopImage.setText("");
        etEventType.setText("");
    }
}