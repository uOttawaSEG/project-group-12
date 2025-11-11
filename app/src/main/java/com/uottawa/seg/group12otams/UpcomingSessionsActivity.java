package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;

public class UpcomingSessionsActivity extends AppCompatActivity {

    private Tutor tutor;
    private RecyclerView rvUpcoming;
    private UpcomingSessionsAdapter adapter;
    private ArrayList<TimeSlot> upcomingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_sessions);

        tutor = (Tutor) getIntent().getSerializableExtra("tutor");

        rvUpcoming = findViewById(R.id.rvUpcomingSessions);
        rvUpcoming.setLayoutManager(new LinearLayoutManager(this));

        loadUpcomingSessions();
    }

    private void loadUpcomingSessions() {
        upcomingList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("time_slots")
                .whereEqualTo("tutorId", tutor.getEmail())
                .get()
                .addOnSuccessListener(task -> {

                    Date now = new Date();

                    for (QueryDocumentSnapshot doc : task) {
                        TimeSlot slot = doc.toObject(TimeSlot.class);
                        if (slot.getStartTime().after(now)) {
                            upcomingList.add(slot);
                        }
                    }

                    adapter = new UpcomingSessionsAdapter(upcomingList, timeSlot -> {
                        tutor.removeSession(timeSlot);
                        upcomingList.remove(timeSlot);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Session cancelled", Toast.LENGTH_SHORT).show();
                    });

                    rvUpcoming.setAdapter(adapter);

                })
                .addOnFailureListener(err -> {
                    Log.e("UpcomingSessions", "Error loading sessions", err);
                });
    }
}
