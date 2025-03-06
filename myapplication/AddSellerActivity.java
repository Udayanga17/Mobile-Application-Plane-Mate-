package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddSellerActivity extends AppCompatActivity {

    private EditText etSellerName, etSellerPhone, etSellerPassword, etSellerCategory;
    private Button btnAddSeller;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_seller);

        // Initialize views
        etSellerName = findViewById(R.id.et_sellerName);
        etSellerPhone = findViewById(R.id.et_sellerPhone);
        etSellerPassword = findViewById(R.id.et_sellerPassword);
        etSellerCategory = findViewById(R.id.et_sellerCategory);
        btnAddSeller = findViewById(R.id.btn_addSeller);

        // Initialize DBHandler
        dbHandler = new DBHandler(this);

        // Set click listener for the "Add Seller" button
        btnAddSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSellerToDatabase();
            }
        });
    }

    private void addSellerToDatabase() {
        // Get input values
        String sellerName = etSellerName.getText().toString().trim();
        String sellerPhone = etSellerPhone.getText().toString().trim();
        String sellerPassword = etSellerPassword.getText().toString().trim();
        String sellerCategory = etSellerCategory.getText().toString().trim();

        // Validate inputs
        if (sellerName.isEmpty() || sellerPhone.isEmpty() || sellerPassword.isEmpty() || sellerCategory.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add seller to the database
        dbHandler.addNewSeller(sellerName, sellerPhone, sellerPassword, sellerCategory);

        // Show success message
        Toast.makeText(this, "Seller added successfully", Toast.LENGTH_SHORT).show();

        // Clear input fields
        etSellerName.setText("");
        etSellerPhone.setText("");
        etSellerPassword.setText("");
        etSellerCategory.setText("");
    }
}