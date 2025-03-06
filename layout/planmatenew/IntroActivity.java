package com.example.planmatenew;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;


import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;



import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;




public class IntroActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;

    //db
    private DatabaseHelper databaseHelper;
    private EditText etName,  etPhone, etAddress, etDescription;
    private Spinner spinCategory;
    private ImageView ivProfileImage;
    private Bitmap selectedBitmap = null;

    private String receivedUsername; // Variable to store the username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);


        // Retrieve the username from Intent
        receivedUsername = getIntent().getStringExtra("USERNAME_KEY");

        //db

        databaseHelper = new DatabaseHelper(this);

        //DatabaseHelper dbHelper = new DatabaseHelper(this);
        //SQLiteDatabase db = dbHelper.getWritableDatabase();

        etName = findViewById(R.id.etName);
        spinCategory = findViewById(R.id.spin_1);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etLocation);
        etDescription = findViewById(R.id.etDescription);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        Button btnFinish = findViewById(R.id.btnFinish);

        profileImageView = findViewById(R.id.ivProfileImage);
        TextView selectImageButton = findViewById(R.id.txt_4);



        // Open gallery on button click
        selectImageButton.setOnClickListener(v -> openGallery());
        btnFinish.setOnClickListener(v -> saveUserData());

        ////////////////////////////////
        Spinner spinner = findViewById(R.id.spin_1);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.items, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the Spinner
        spinner.setAdapter(adapter);

        // Set an item selected listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Show the selected item
                String selectedItem = parentView.getItemAtPosition(position).toString();
                Toast.makeText(IntroActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected
            }
   });

    }

    // Method to open the gallery and pick an image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri); // Assign it here
                profileImageView.setImageBitmap(selectedBitmap); // Display the image
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Convert Bitmap to Byte Array
    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // Save Data to SQLite
    private void saveUserData() {
        String username = receivedUsername.trim();
        String name = etName.getText().toString().trim();
        String category = spinCategory.getSelectedItem().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (username.isEmpty() || name.isEmpty() || category.isEmpty() || phone.isEmpty() || address.isEmpty() || description.isEmpty() ||selectedBitmap == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] imageBytes = convertBitmapToByteArray(selectedBitmap);

        boolean isInserted = databaseHelper.insertUser(username, name, category, phone, address, description, imageBytes);
        if (isInserted) {
            Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

            // Navigate to NextActivity
            Intent intent = new Intent(IntroActivity.this, SellerDashboardActivity.class);
            intent.putExtra("USERNAME", receivedUsername.trim()); // Pass username
            startActivity(intent);

            // Optional: Close this activity so user can't navigate back
            finish();

        } else {
            Toast.makeText(this, "Failed to Insert Data", Toast.LENGTH_SHORT).show();
        }
    }


    // Method to handle location submission
    public void submitLocation(View view) {
        EditText locationEditText = findViewById(R.id.etLocation);
        String location = locationEditText.getText().toString().trim();

        if (!location.isEmpty()) {
            // Use the location string (e.g., to search on a map or API)
            Toast.makeText(this, "Location entered: " + location, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
        }
    }


}
