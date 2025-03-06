package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Hotel extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

    }

    public void oncard(View view) {
        Intent intent = new Intent(Hotel.this, PhotographySecond.class);
        startActivity(intent);
    }



}
