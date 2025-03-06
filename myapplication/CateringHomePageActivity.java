package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CateringHomePageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catering_homepage);


    }

    public void HotBuffet(View view) {

        Intent IntentObj = new Intent(this, HotBuffetActivity.class);
        startActivity(IntentObj);

    }
}
