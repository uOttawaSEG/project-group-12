package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class CreateAvailabilityActivity extends AppCompatActivity {

    private Button btnCreateSlot;
    private Tutor tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_availability);
        tutor = (Tutor) getIntent().getSerializableExtra("tutor");

        // Debug
        Log.e("Create-Avaliabile-Activity", "In this class! - " + tutor.getFirstName());

        // This is how the tutor sets auto approve to true/false. The way this is coded is that it is universal and NOT timeslot-specific
        // Idk how to get the radio btn, so I'm just setting it to true for now
        tutor.setAutoApproveTimeSlotSessions(true);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        btnCreateSlot = findViewById(R.id.btnCreateSlot);
    }
    private void setupClickListeners() {
        btnCreateSlot.setOnClickListener(v -> {
            // Handle button click here
            Log.e("Create-Avaliabile-Activity", "Button clicked!");

            // TODO: add dates (as Date objects)
            createAvailabilitySlot(null, null);
        });
    }
    private void createAvailabilitySlot(Date startDate, Date endDate) {
        // TODO: replace start/end date with actually set start/end dates
        // Checks were added to tutor.setTimeSlot to check if it is valid + it gives a proper message
        // I'm not tooo sure if all of them work. You can double check them if you want.

        startDate = new Date(2025, 11, 1, 0, 0, 0);
        endDate = new Date(2025, 11, 1, 0, 30, 0);
        try {
            tutor.setTimeSlot(startDate, endDate);
        } catch (IllegalArgumentException e) {
            Log.e("Create-Avaliabile-Activity", "Error: " + e.getMessage());
        }

    }
}
