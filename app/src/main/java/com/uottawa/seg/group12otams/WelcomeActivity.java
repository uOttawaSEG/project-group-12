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
    private Button edtWelcomeLogout, edtAdminInboxButton;

    private String userRole; // store the user role


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
        edtAdminInboxButton = findViewById(R.id.edtAdminInboxButton);
    }

    // logic when it comes to the welcome text on the welcome page

    private void displayWelcomeMessage() {
        userRole = getIntent().getStringExtra("USER_ROLE");
        // in case there is no user role
        if (userRole == null) {
            userRole = "user";
        }

        // updating the message text
        String welcomeMessage = "Welcome! You are logged in as " + userRole;
        edtWelcomeText.setText(welcomeMessage);

        // show the admin inbox button only for the admin
        if ("Administrator".equals(userRole)) {
            edtAdminInboxButton.setVisibility(View.VISIBLE);
        }
        else {
            edtAdminInboxButton.setVisibility(View.GONE);
        }
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

        // gotta add one now for the admin inbox
        edtAdminInboxButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminInboxActivity.class);
            startActivity(intent);
        });
    }

}
