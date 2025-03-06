package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Event_create extends AppCompatActivity {

    private EditText eventNameEditText, budgetEditText;
    private Button nextButton, changePhotoButton;
    private TextView selectedDate;
    private double totalBudget;
    private CalendarView calendarView;
    private ImageView photoPlaceholder;
    private DBHandler dbHandler;
    private Spinner eventTypeSpinner;
    private String selectedEventType; // Store the selected event type

    private LinearLayout taskContainer; // Replace ListView with LinearLayout
    private ArrayList<String> selectedTasks = new ArrayList<>();

    private ArrayList<Uri> selectedImageUris = new ArrayList<>();

    private final ActivityResultLauncher<Intent> photoPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    handleGalleryResult(result.getData());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create);

        dbHandler = new DBHandler(this); // Initialize database handler

        eventNameEditText = findViewById(R.id.eventNameEditText);
        budgetEditText = findViewById(R.id.total_budget); // Assuming you have an EditText for budget input
        nextButton = findViewById(R.id.next_button);
        selectedDate = findViewById(R.id.selected_date);
        calendarView = findViewById(R.id.calendar_view);
        changePhotoButton = findViewById(R.id.change_photo_button);
        photoPlaceholder = findViewById(R.id.photo_placeholder);

        eventTypeSpinner = findViewById(R.id.eventTypeSpinner); // Initialize Spinner
        taskContainer = findViewById(R.id.task_container); // Initialize LinearLayout for tasks

        // Set up the spinner with event types
        String[] eventTypes = {"Birthday", "Wedding", "Office Function", "Family Function"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, eventTypes);
        eventTypeSpinner.setAdapter(adapter);

        // Set a listener to get the selected event type
        eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEventType = parent.getItemAtPosition(position).toString(); // Get selected value correctly
                Log.d("SpinnerSelection", "Selected Event Type: " + selectedEventType);
                updateTaskList(selectedEventType); // Update task list based on selected event type
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedEventType = ""; // Default to empty if nothing is selected
            }
        });

        nextButton.setEnabled(false);
        nextButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));

        eventNameEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    nextButton.setEnabled(true);
                    nextButton.setBackgroundTintList(ContextCompat.getColorStateList(Event_create.this, android.R.color.holo_purple));
                } else {
                    nextButton.setEnabled(false);
                    nextButton.setBackgroundTintList(ContextCompat.getColorStateList(Event_create.this, android.R.color.darker_gray));
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Set default date to current date
        Calendar calendar = Calendar.getInstance();
        long currentDate = calendar.getTimeInMillis();
        calendarView.setDate(currentDate, true, true);

        // Disable past dates from selection
        calendarView.setMinDate(currentDate);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate.setText(String.format(Locale.getDefault(), "%s %d, %d", getMonthName(month), dayOfMonth, year));
        });

        changePhotoButton.setOnClickListener(v -> openPhotoPicker());

        nextButton.setOnClickListener(v -> {
            nextButton.setEnabled(false); // Disable button to prevent multiple clicks

            String eventName = eventNameEditText.getText().toString();
            String selectedDateText = selectedDate.getText().toString();
            String budgetText = budgetEditText.getText().toString();
            totalBudget = Double.parseDouble(budgetText); // Convert budget to double
            String imagePath = null;

            if (!selectedImageUris.isEmpty()) {
                // Save the first selected image to internal storage
                imagePath = saveImageToInternalStorage(selectedImageUris.get(0));
            }

            // Capture selected tasks
            selectedTasks.clear();
            for (int i = 0; i < taskContainer.getChildCount(); i++) {
                View view = taskContainer.getChildAt(i);
                if (view instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) view;
                    if (checkBox.isChecked()) {
                        selectedTasks.add(checkBox.getText().toString());
                    }
                }
            }

            // Convert selected tasks to a comma-separated string
            String tasks = String.join(", ", selectedTasks);

            // Store event details in the database
            dbHandler.addNewEvent(eventName, selectedDateText, imagePath, totalBudget, selectedEventType, tasks);

            // Pass data to EventSummaryActivity
            Intent intent = new Intent(Event_create.this, EventSummaryActivity.class);
            intent.putExtra("EVENT_NAME", eventName);
            intent.putExtra("SELECTED_DATE", selectedDateText);
            intent.putExtra("EVENT_BUDGET", totalBudget);
            intent.putExtra("IMAGE_URI", imagePath);
            intent.putExtra("EVENT_TYPE", selectedEventType);
            intent.putExtra("EVENT_TASKS", tasks); // Ensure tasks are passed
            startActivity(intent);
        });
    }

    private void openPhotoPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        photoPickerLauncher.launch(intent);
    }

    private void handleGalleryResult(Intent data) {
        selectedImageUris.clear();

        if (data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            for (int i = 0; i < Math.min(count, 4); i++) {
                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                selectedImageUris.add(imageUri);
            }
        } else if (data.getData() != null) {
            selectedImageUris.add(data.getData());
        }

        if (!selectedImageUris.isEmpty()) {
            photoPlaceholder.setImageURI(selectedImageUris.get(0));
        }
    }

    private String getMonthName(int month) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[month];
    }

    private void updateTaskList(String eventType) {
        String[] tasks;

        switch (eventType) {
            case "Wedding":
                tasks = new String[]{"Wedding Cake", "Decorations", "Catering", "Hotel", "Bands", "Photography", "Transport"};
                break;
            case "Birthday":
                tasks = new String[]{"Decorations", "Catering", "Hotel", "Bands", "Photography", "Balloons"};
                break;
            case "Office Function":
                tasks = new String[]{"Catering", "Conference Hall", "Projector Setup", "Hotel"};
                break;
            case "Family Function":
                tasks = new String[]{"Decorations", "Catering", "Hotel", "Photography"};
                break;
            default:
                tasks = new String[]{};
        }

        // Clear existing tasks
        taskContainer.removeAllViews();

        // Add tasks as CheckBoxes
        for (String task : tasks) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(task);
            checkBox.setTextSize(16);
            checkBox.setPadding(8, 8, 8, 8);
            taskContainer.addView(checkBox);
        }
    }

    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            // Get the input stream from the URI
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            if (inputStream == null) {
                return null;
            }

            // Create a file in the app's internal storage
            File directory = new File(getFilesDir(), "event_images");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = "event_image_" + System.currentTimeMillis() + ".jpg";
            File file = new File(directory, fileName);

            // Copy the image to the file
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();

            // Return the file path
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}