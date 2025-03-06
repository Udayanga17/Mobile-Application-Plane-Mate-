package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Seller extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller);
    }

    public void onButtonfogot(View view) {
        Intent intent = new Intent(Seller.this, AddSellerActivity.class);
        startActivity(intent);
    }
    public void onLinkUser(View view) {
        Intent intent = new Intent(Seller.this, MainActivity.class);
        startActivity(intent);
    }

    public void onButtonsellerLogin(View view) {
        EditText etName = findViewById(R.id.txt_selleruserName);
        EditText etPassword = findViewById(R.id.txt_sellerpassword);

        if (etName == null || etPassword == null) {
            Toast.makeText(this, "Error: UI elements not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        DBHandler dbHandler = new DBHandler(this);
        try {
            if (dbHandler.checkSellerPassword(name, password)) {
                // Credentials match, proceed to the SellerView activity
                Intent intent = new Intent(Seller.this, SellerView.class);
                intent.putExtra("SELLER_NAME", name); // Pass seller name to the next activity
                startActivity(intent);
            } else {
                // Credentials do not match, show an error message
                Toast.makeText(Seller.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        } finally {
            dbHandler.close(); // Close the database connection
        }
    }
}