package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


   //String pw = "123";
    //String Ename = "abc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
    }

    public void onButtonLogin(View view) {

        EditText etName = findViewById(R.id.txt_userName);
        EditText etPassword = findViewById(R.id.txt_password);

        String name = etName.getText().toString();
        String password = etPassword.getText().toString();

        // Initialize DBHandler
        DBHandler dbHandler = new DBHandler(this);

        // Check if the username and password match
        if (dbHandler.checkPassword(name, password)) {
            // Credentials match, proceed to the Home activity
            Intent intent = new Intent(MainActivity.this, EventHome.class);
            startActivity(intent);
        } else {
            // Credentials do not match, show an error message
            Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
        }/*
        if (password.equals(pw) && name.equals(Ename)) {
            // Credentials match, proceed to the Home activity
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
        } else {
            // Credentials do not match, show an error message
            Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
        }*/
    }



   public void onLinkSignup(View view) {
        Intent intent = new Intent(MainActivity.this, temp.class);
        startActivity(intent);
    }
    public void onaddshops(View view) {
        Intent intent = new Intent(MainActivity.this, AddShopActivity.class);
        startActivity(intent);
    }
    public void onclickSeller(View view) {
        Intent intent = new Intent(MainActivity.this, Seller.class);
        startActivity(intent);
    }


    public void onPhotography(View view) {
    }

    public void onMusicBand(View view) {
    }

    public void CateringServices(View view) {
    }




}
