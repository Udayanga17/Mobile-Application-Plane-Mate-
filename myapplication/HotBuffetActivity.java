package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HotBuffetActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //EdgeToEdge.enable(this);
            setContentView(R.layout.activity_hot_buffet);
        }

    public void PlainRice(View view) {

        Intent IntentObj = new Intent(this, PalinRiceOrderViewActivity.class);
        startActivity(IntentObj);

    }
    }
