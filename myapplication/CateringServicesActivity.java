package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CateringServicesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catering_services);
    }

    public void Catering(View view) {

        Intent IntentObj = new Intent(this, CateringHomePageActivity.class);
        startActivity(IntentObj);

    }
    }
