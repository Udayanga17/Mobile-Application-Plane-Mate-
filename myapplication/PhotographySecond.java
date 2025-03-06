package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PhotographySecond extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographysecond);

    }
    public void onLogo(View view) {
        Intent intent = new Intent(PhotographySecond.this, Home.class);
        startActivity(intent);
    }

    public void onbooking(View view) {
        Intent intent = new Intent(PhotographySecond.this, Payment.class);
        startActivity(intent);
    }
}
