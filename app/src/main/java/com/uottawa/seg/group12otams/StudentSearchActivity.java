package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StudentSearchActivity extends AppCompatActivity implements AvailableSlotsAdapter.Callbacks {
    private Student student;
    private EditText etCourse;
    private Button btnSearch;
    private RecyclerView rv;
    private AvailableSlotsAdapter adapter;

    private final Database<TimeSlot> tsDb = new Database<>(TimeSlot.class, "time_slots");
    private final Database<Tutor> tutorDb = new Database<>(Tutor.class, "tutors");
    private final Database<TimeSlotRequest> reqDb = new Database<>(TimeSlotRequest.class, "time_slot_requests");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_search);

        student = (Student) getIntent().getSerializableExtra("student");
        if (student == null) {
            Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etCourse = findViewById(R.id.etCourseCode);
        btnSearch = findViewById(R.id.btnSearch);
        rv = findViewById(R.id.rvAvailableSlots);
        rv.setLayoutManager(new LinearLayoutManager(this));

        btnSearch.setOnClickListener(v -> doSearch());
    }

    private void doSearch() {
        String code = etCourse.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            etCourse.setError("Enter course code");
            return;
        }

        // Step 1: load tutors
        tutorDb.queryTutors(tutorTask -> {
            if (!tutorTask.isSuccessful() || tutorTask.getResult() == null) {
                Toast.makeText(this, "Failed to load tutors", Toast.LENGTH_SHORT).show();
                return;
            }
            Map<String, Tutor> tutorsById = new HashMap<>();
            for (QueryDocumentSnapshot d : tutorTask.getResult()) {
                Tutor t = d.toObject(Tutor.class);
                if (t != null) tutorsById.put(t.getEmail(), t);
            }

            // Step 2: load student's existing requests to filter conflicts
            reqDb.queryTimeSlotRequests(reqTask -> {
                if (!reqTask.isSuccessful() || reqTask.getResult() == null) {
                    Toast.makeText(this, "Failed to load requests", Toast.LENGTH_SHORT).show();
                    return;
                }
                Set<String> myRequestSlotIds = new HashSet<>();
                for (QueryDocumentSnapshot d : reqTask.getResult()) {
                    TimeSlotRequest r = d.toObject(TimeSlotRequest.class);
                    if (r != null && student.getEmail().equals(r.getStudentId())) {
                        myRequestSlotIds.add(r.getTimeSlotId());
                    }
                }

                // Step 3: load all time slots and filter
                tsDb.queryTimeSlots(tsTask -> {
                    if (!tsTask.isSuccessful() || tsTask.getResult() == null) {
                        Toast.makeText(this, "Failed to load time slots", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ArrayList<TimeSlot> allSlots = new ArrayList<>();
                    for (QueryDocumentSnapshot d : tsTask.getResult()) {
                        TimeSlot ts = d.toObject(TimeSlot.class);
                        if (ts != null) allSlots.add(ts);
                    }

                    // Build a map for quick lookup by id
                    Map<String, TimeSlot> slotsById = new HashMap<>();
                    for (TimeSlot s : allSlots) slotsById.put(s.getTimeSlotId(), s);

                    // Gather student's conflicting intervals from existing requests
                    ArrayList<TimeSlot> myExisting = new ArrayList<>();
                    for (String id : myRequestSlotIds) {
                        TimeSlot s = slotsById.get(id);
                        if (s != null) myExisting.add(s);
                    }

                    // Filter by course, future, unbooked, and non-conflict
                    Date now = new Date();
                    ArrayList<TimeSlot> results = new ArrayList<>();
                    outer:
                    for (TimeSlot s : allSlots) {
                        // future only and unbooked
                        if (s.getBookedStudent() != null) continue;
                        if (!s.getStartTime().after(now)) continue;

                        // tutor must offer the requested course
                        Tutor t = tutorsById.get(s.getTutorId());
                        if (t == null || t.getCoursesOffered() == null) continue;
                        boolean offers = false;
                        for (String c : t.getCoursesOffered()) {
                            if (code.equalsIgnoreCase(c)) { offers = true; break; }
                        }
                        if (!offers) continue;

                        // non-conflicting with student's existing requests
                        for (TimeSlot existing : myExisting) {
                            if (TimeUtils.overlaps(s.getStartTime(), s.getEndTime(), existing.getStartTime(), existing.getEndTime())) {
                                continue outer;
                            }
                        }

                        results.add(s);
                    }

                    // Update UI
                    adapter = new AvailableSlotsAdapter(results, student, this);
                    rv.setAdapter(adapter);
                    findViewById(R.id.tvEmpty).setVisibility(results.isEmpty() ? View.VISIBLE : View.GONE);
                });
            });
        });
    }

    @Override
    public void onRequest(TimeSlot slot) {
        try {
            student.setTimeSlotRequest(slot);
            if (adapter != null) adapter.remove(slot);
            Toast.makeText(this, "Requested", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
