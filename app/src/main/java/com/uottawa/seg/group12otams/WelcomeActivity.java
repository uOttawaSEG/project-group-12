package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WelcomeActivity extends AppCompatActivity{

    // Instance variables
    private TextView edtWelcomeText;
    private Button edtWelcomeLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        initializeViews();
        displayWelcomeMessage();
        setupClickListeners();
    }

    // initialize the elements for their views

    private void initializeViews() {
        edtWelcomeText = findViewById(R.id.edtWelcomeText);
        edtWelcomeLogout = findViewById(R.id.edtWelcomeLogout);
    }

    // logic when it comes to the welcome text on the welcome page

    private void displayWelcomeMessage() {
        String role = getIntent().getStringExtra("USER_ROLE");
        // in case there is no user role
        if (role == null) {
            role = "user";
        }

        // updating the message text
        String welcomeMessage = "Welcome! You are logged in as " + role;
        edtWelcomeText.setText(welcomeMessage);
    }

    // clicker method to go back to main page after clicking log out
    private void setupClickListeners() {
        // this time it won't be dependent on any other method before processing, so its just v ->
        edtWelcomeLogout.setOnClickListener(v -> {
            // going back to the log in screen
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // closes and stops the activity
        });
    }

}
