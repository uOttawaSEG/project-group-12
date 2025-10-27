package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This is the java class for pending page, and allows to go back to login so at least it's not stuck
 */
public class PendingRequest extends AppCompatActivity{
    private Button edtPendingBackButton;

    // constructor to intialize everything
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_pending);

        initalizeViews();
        setUpClickListeners();
    }

    // Initalize the views by connecting buttons
    private void initalizeViews() {
        edtPendingBackButton = findViewById(R.id.edtPendingBackButton);
    }

    // set up the click listener to go back to main activity class
    private void setUpClickListeners() {
        edtPendingBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // finish the activity
        });
    }
}
