package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentSessionsActivity extends AppCompatActivity {
    private Student student;
    private RecyclerView rv;
    private StudentSessionAdapter adapter;

    private final Database<TimeSlotRequest> reqDb = new Database<>(TimeSlotRequest.class, "time_slot_requests");
    private final Database<TimeSlot> tsDb = new Database<>(TimeSlot.class, "time_slots");
    private final Database<Tutor> tutorDb = new Database<>(Tutor.class, "tutors");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sessions);

        student = (Student) getIntent().getSerializableExtra("student");
        if (student == null) {
            Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        rv = findViewById(R.id.rvStudentSessions);
        rv.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    private void loadData() {
        // Load tutors first to have names and ratings
        tutorDb.queryTutors(tutorTask -> {
            if (!tutorTask.isSuccessful() || tutorTask.getResult() == null) {
                Toast.makeText(this, "Failed to load tutors", Toast.LENGTH_SHORT).show();
                return;
            }
            Map<String, Tutor> tutors = new HashMap<>();
            for (QueryDocumentSnapshot d : tutorTask.getResult()) {
                Tutor t = d.toObject(Tutor.class);
                if (t != null) tutors.put(t.getEmail(), t);
            }

            // Load time slots next
            tsDb.queryTimeSlots(tsTask -> {
                if (!tsTask.isSuccessful() || tsTask.getResult() == null) {
                    Toast.makeText(this, "Failed to load time slots", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, TimeSlot> slots = new HashMap<>();
                for (QueryDocumentSnapshot d : tsTask.getResult()) {
                    TimeSlot ts = d.toObject(TimeSlot.class);
                    if (ts != null) slots.put(ts.getTimeSlotId(), ts);
                }

                // Load student's requests
                reqDb.queryTimeSlotRequests(reqTask -> {
                    if (!reqTask.isSuccessful() || reqTask.getResult() == null) {
                        Toast.makeText(this, "Failed to load requests", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ArrayList<TimeSlotRequest> mine = new ArrayList<>();
                    for (QueryDocumentSnapshot d : reqTask.getResult()) {
                        TimeSlotRequest r = d.toObject(TimeSlotRequest.class);
                        if (r != null && student.getEmail().equals(r.getStudentId())) {
                            mine.add(r);
                        }
                    }

                    // Sort by associated start time desc
                    Collections.sort(mine, (a, b) -> {
                        TimeSlot tsa = slots.get(a.getTimeSlotId());
                        TimeSlot tsb = slots.get(b.getTimeSlotId());
                        Date da = tsa != null ? tsa.getStartTime() : new Date(0);
                        Date db = tsb != null ? tsb.getStartTime() : new Date(0);
                        return db.compareTo(da);
                    });

                    adapter = new StudentSessionAdapter(mine, student, new StudentSessionAdapter.Callbacks() {
                        @Override
                        public void onCancel(TimeSlotRequest request) {
                            try {
                                student.cancelRequest(request.getTimeSlotId());
                                adapter.remove(request);
                                Toast.makeText(StudentSessionsActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            } catch (IllegalArgumentException e) {
                                Toast.makeText(StudentSessionsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onRate(TimeSlotRequest request, int rating) {
                            try {
                                student.rateTutor(request.getTimeSlotId(), rating);
                                Toast.makeText(StudentSessionsActivity.this, "Thanks for rating!", Toast.LENGTH_SHORT).show();
                            } catch (IllegalArgumentException e) {
                                Toast.makeText(StudentSessionsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }, slots, tutors);
                    rv.setAdapter(adapter);
                });
            });
        });
    }
}
