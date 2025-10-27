package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

// Same thing as the pending request class but now for this one
public class RejectedRequest extends AppCompatActivity {

    private Button edtRejectedBackButton;

    // constructor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_registration);

        intializeViews();
        setUpClickListeners();
    }

    // intialize the views
    private void intializeViews() {
        edtRejectedBackButton = findViewById(R.id.edtRejectedBackButton);
    }

    // setting up the click listeners
    private void setUpClickListeners() {
        edtRejectedBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // close the activity
        });
    }

}
