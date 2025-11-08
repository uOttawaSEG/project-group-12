package com.uottawa.seg.group12otams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TutorDashboardActivity extends AppCompatActivity {

    private Button btnCreateAvailability;
    private Button btnUpcomingSessions;
    private Button btnPastSessions;
    private Button btnPendingRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_dashboard);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        btnCreateAvailability = findViewById(R.id.btnCreateAvailability);
        btnUpcomingSessions = findViewById(R.id.btnUpcomingSessions);
        btnPastSessions = findViewById(R.id.btnPastSessions);
        btnPendingRequests = findViewById(R.id.btnPendingRequests);
    }

    private void setupClickListeners() {

        btnCreateAvailability.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateAvailabilityActivity.class);
            startActivity(intent);
        });

        btnUpcomingSessions.setOnClickListener(v -> {
            Intent intent = new Intent(this, UpcomingSessionsActivity.class);
            startActivity(intent);
        });

        btnPastSessions.setOnClickListener(v -> {
            Intent intent = new Intent(this, PastSessionsActivity.class);
            startActivity(intent);
        });

        btnPendingRequests.setOnClickListener(v -> {
            Intent intent = new Intent(this, PendingRequestsActivity.class);
            startActivity(intent);
        });
    }
}
