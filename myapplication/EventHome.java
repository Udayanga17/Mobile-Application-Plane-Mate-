package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

public class EventHome extends Activity {
    private DBHandler dbHandler;
    private LinearLayout myEventsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_home);

        dbHandler = new DBHandler(this);
        myEventsContainer = findViewById(R.id.my_events_container);

        loadEvents();
    }

    public void onClick(View view) {
        Intent intent = new Intent(EventHome.this, Event_create.class);
        startActivity(intent);
    }
    public void onWedding(View view){

        Intent intent = new Intent(EventHome.this, WeddingCategoryActivity.class);
        startActivity(intent);
    }

    private void loadEvents() {
        Cursor cursor = dbHandler.getAllEvents();

        if (cursor == null) {
            Toast.makeText(this, "Error fetching events", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No events found", Toast.LENGTH_SHORT).show();
                return;
            }

            myEventsContainer.removeAllViews();

            while (cursor.moveToNext()) {
                @SuppressLint("Range") int eventId = cursor.getInt(cursor.getColumnIndex("event_id"));
                @SuppressLint("Range") String eventName = cursor.getString(cursor.getColumnIndex("event_name"));
                @SuppressLint("Range") String eventDate = cursor.getString(cursor.getColumnIndex("event_date"));
                @SuppressLint("Range") String imageUri = cursor.getString(cursor.getColumnIndex("event_image"));
                @SuppressLint("Range") double budget = cursor.getDouble(cursor.getColumnIndex("event_budget")); // Retrieve budget

                CardView cardView = new CardView(this);
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                cardParams.setMargins(16, 16, 16, 16);
                cardView.setLayoutParams(cardParams);
                cardView.setRadius(12);
                cardView.setCardElevation(8);
                cardView.setUseCompatPadding(true);

                // Add click listener to the card
                cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(EventHome.this, EventSummaryActivity.class);
                    intent.putExtra("EVENT_NAME", eventName);
                    intent.putExtra("SELECTED_DATE", eventDate);
                    intent.putExtra("IMAGE_URI", imageUri);
                    intent.putExtra("EVENT_BUDGET", budget); // Pass budget
                    startActivity(intent);
                });

                LinearLayout eventLayout = new LinearLayout(this);
                eventLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                eventLayout.setOrientation(LinearLayout.HORIZONTAL);
                eventLayout.setPadding(16, 16, 16, 16);

                ImageView eventIcon = new ImageView(this);
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(150, 150);
                eventIcon.setLayoutParams(imageParams);
                eventIcon.setPadding(0, 0, 16, 0);

                if (imageUri != null && !imageUri.isEmpty()) {
                    try {
                        eventIcon.setImageURI(Uri.parse(imageUri));
                    } catch (Exception e) {
                        eventIcon.setImageResource(R.drawable.wedding_1);
                    }
                } else {
                    eventIcon.setImageResource(R.drawable.wedding_1);
                }

                TextView eventTextView = new TextView(this);
                eventTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));
                eventTextView.setText(eventName + "\n" + eventDate + "\nBudget: LKR " + budget);
                eventTextView.setTextSize(16);
                eventTextView.setTextColor(getResources().getColor(android.R.color.black));

                Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
                deleteButton.setOnClickListener(v -> {
                    dbHandler.deleteEvent(eventId);
                    loadEvents();
                });

                eventLayout.addView(eventIcon);
                eventLayout.addView(eventTextView);
                eventLayout.addView(deleteButton);
                cardView.addView(eventLayout);
                myEventsContainer.addView(cardView);
            }
        } finally {
            cursor.close();
        }
    }



}
