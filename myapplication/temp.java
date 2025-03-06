package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class temp extends Activity {

    private DBHandler dbHandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHandler = new DBHandler(temp.this);


    }

    public void onLinkLogin(View view) {
        Intent intent = new Intent(temp.this, MainActivity.class);
        startActivity(intent);
    }
    public void onButtonSignup(View view) {
        // Retrieve user input
        EditText sName = findViewById(R.id.txt_signupUsername);
        String name = sName.getText().toString().trim();

        EditText sPhone = findViewById(R.id.txt_signupPhonenumber);
        String phone = sPhone.getText().toString().trim();

        EditText sPassword = findViewById(R.id.txt_signupPassword);
        String password = sPassword.getText().toString().trim();

        // Validate user input
        if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(temp.this, "Please enter all the data.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add new user to the database
        dbHandler.addNewUser(name, phone, password);

        // Display success message
        Toast.makeText(temp.this, "User has been added.", Toast.LENGTH_SHORT).show();

        // Clear the input fields
        sName.setText("");
        sPhone.setText("");
        sPassword.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Close database connection to prevent leaks
        dbHandler.close();
    }
}
