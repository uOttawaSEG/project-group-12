package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class UpcomingSessionsActivity extends AppCompatActivity {
    private Tutor tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_sessions);
        tutor = (Tutor) getIntent().getSerializableExtra("tutor");
        Log.e("UpcomingSessionsActivity", "Sessions: " + tutor.getUpcomingSessions());
    }
}
