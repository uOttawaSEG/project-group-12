package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class PendingRequestsActivity extends AppCompatActivity {

    private Tutor tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests);
        tutor = (Tutor) getIntent().getSerializableExtra("tutor");
        Log.e("PendingRequestsActivity", "Requests: " + tutor.getPendingTimeSlotRequests());

        // This is how the tutor sets auto approve to true/false. The way this is coded is that it is universal and NOT timeslot-specific
        tutor.setAutoApproveTimeSlotSessions(true);


        // You would fetch pending time slot requests using tutor.getPendingTimeSlotRequests()
        // You can also fetch all time slot requests using tutor.getTimeSlotRequest()
        // NOTE: it fetches it for that specific tutor. If you want to get for ALL tutors, then you have to directly call this method from the Database<Tutor> obj.
        TimeSlotRequest timeSlotRequest = null;
        // Approved, Pending, Rejected are the valid statuses
        timeSlotRequest.setStatus("Approved");

        // Check out the TimeSlotRequest class to see all of its properties
        // You can fetch a student's information (the student who requested the session) by
        // accessing the studentId property of the timeSlotRequest object, then fetching that student's information
        // from the Database<Student> obj (.getUser("email") I think)
    }
}
