package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PastSessionsActivity extends AppCompatActivity {

    private Tutor tutor;
    private RecyclerView rvPast;
    private PastSessionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_sessions);

        tutor = (Tutor) getIntent().getSerializableExtra("tutor");
        rvPast = findViewById(R.id.rvPastSessions);

        ArrayList<TimeSlot> past = tutor.getPastSessions();
        Log.e("PastSessionsActivity", "Past sessions: " + past);

        adapter = new PastSessionsAdapter(past);

        rvPast.setLayoutManager(new LinearLayoutManager(this));
        rvPast.setAdapter(adapter);
    }
}
