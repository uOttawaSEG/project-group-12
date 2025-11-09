package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UpcomingSessionsActivity extends AppCompatActivity {

    private Tutor tutor;
    private RecyclerView rvUpcoming;
    private UpcomingSessionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_sessions);

        tutor = (Tutor) getIntent().getSerializableExtra("tutor");
        rvUpcoming = findViewById(R.id.rvUpcomingSessions);

        ArrayList<TimeSlot> upcoming = tutor.getUpcomingSessions();
        Log.e("UpcomingSessionsActivity", "Upcoming: " + upcoming);

        adapter = new UpcomingSessionsAdapter(upcoming, timeSlot -> {
            try {
                tutor.removeSession(timeSlot);  // remove from DB
                upcoming.remove(timeSlot);
                adapter.notifyDataSetChanged();
            } catch (IllegalArgumentException e) {
                Log.e("UpcomingSessionsActivity", "Error: " + e.getMessage());
            }
        });

        rvUpcoming.setLayoutManager(new LinearLayoutManager(this));
        rvUpcoming.setAdapter(adapter);
    }
}
