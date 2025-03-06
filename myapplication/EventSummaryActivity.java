package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventSummaryActivity extends AppCompatActivity {

    private TextView eventNameTextView, selectedDateTextView, countdownTextView, budgetTextView;
    private ImageView eventImageView;
    private Button backButton;
    private RecyclerView shopRecyclerView;
    private ShopAdapter shopAdapter;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_summary);

        eventNameTextView = findViewById(R.id.event_name_textview);
        selectedDateTextView = findViewById(R.id.selected_date_textview);
        countdownTextView = findViewById(R.id.countdown_textview);
        budgetTextView = findViewById(R.id.event_budget_textview);
        eventImageView = findViewById(R.id.event_image);
        backButton = findViewById(R.id.back_button);
        shopRecyclerView = findViewById(R.id.shop_recyclerview);

        // Initialize DBHandler
        dbHandler = new DBHandler(this);

        // Retrieve data from Intent
        Intent intent = getIntent();
        String eventName = intent.getStringExtra("EVENT_NAME");
        String selectedDate = intent.getStringExtra("SELECTED_DATE");
        double budget = intent.getDoubleExtra("EVENT_BUDGET", -1.0);
        String imageUri = intent.getStringExtra("IMAGE_URI");
        String eventTasks = intent.getStringExtra("EVENT_TASKS");

        // Set data to UI
        eventNameTextView.setText(eventName);
        selectedDateTextView.setText(selectedDate);

        if (budget >= 0) {
            budgetTextView.setText(String.format(Locale.getDefault(), "LKR %.2f", budget));
        } else {
            budgetTextView.setText("Not specified");
        }

        if (imageUri != null && !imageUri.isEmpty()) {
            try {
                eventImageView.setImageURI(Uri.parse(imageUri));
            } catch (Exception e) {
                eventImageView.setImageResource(R.drawable.wedding_1); // Fallback image
            }
        } else {
            eventImageView.setImageResource(R.drawable.wedding_1); // Fallback image
        }

        // Start countdown
        startCountdown(selectedDate);

        // Display shops based on selected tasks


        String eventTasks2 = intent.getStringExtra("EVENT_TASKS");
        if (eventTasks2 != null && !eventTasks.isEmpty()) {
            String[] tasks = eventTasks.split(", ");
            displayShopsForTasks(tasks);
        }

        backButton.setOnClickListener(v -> finish());
    }

    private void displayShopsForTasks(String[] tasks) {
        List<WeddingShopModel> shops = new ArrayList<>();
        for (String task : tasks) {
            shops.addAll(dbHandler.getShopsByCategory(task));
        }

        // Set up RecyclerView
        shopAdapter = new ShopAdapter(shops);
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        shopRecyclerView.setAdapter(shopAdapter);
    }

    private void startCountdown(String eventDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        try {
            Date eventDateTime = sdf.parse(eventDate);
            if (eventDateTime != null) {
                long eventTimeInMillis = eventDateTime.getTime();
                long currentTime = System.currentTimeMillis();
                long timeRemaining = eventTimeInMillis - currentTime;

                if (timeRemaining > 0) {
                    new CountDownTimer(timeRemaining, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long days = millisUntilFinished / (1000 * 60 * 60 * 24);
                            long hours = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                            long minutes = (millisUntilFinished / (1000 * 60)) % 60;
                            long seconds = (millisUntilFinished / 1000) % 60;

                            String countdownText = String.format(Locale.getDefault(),
                                    "Event starts in: %d days, %02d:%02d:%02d",
                                    days, hours, minutes, seconds);
                            countdownTextView.setText(countdownText);
                        }

                        @Override
                        public void onFinish() {
                            countdownTextView.setText("Event Started!");
                        }
                    }.start();
                } else {
                    countdownTextView.setText("Event has already started!");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            countdownTextView.setText("Invalid date format!");
        }
    }
}