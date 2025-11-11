package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PendingRequestsActivity extends AppCompatActivity {

    private Tutor tutor;
    private RecyclerView rvPending;
    private PendingRequestsAdapter adapter;
    private ArrayList<TimeSlotRequest> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests);

        tutor = (Tutor) getIntent().getSerializableExtra("tutor");

        rvPending = findViewById(R.id.rvPendingRequests);
        rvPending.setLayoutManager(new LinearLayoutManager(this));

        loadPendingRequests();
    }

    private void loadPendingRequests() {
        requests = tutor.getPendingTimeSlotRequests();

        if (requests == null) {
            requests = new ArrayList<>();
        }

        adapter = new PendingRequestsAdapter(
                requests,
                new PendingRequestsAdapter.RequestActions() {
                    @Override
                    public void onApprove(TimeSlotRequest request) {
                        tutor.setTimeSlotRequestStatus(request.getTimeSlotId(), true);
                        request.setStatus("Approved");
                        requests.remove(request);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(PendingRequestsActivity.this, "Approved", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReject(TimeSlotRequest request) {
                        tutor.setTimeSlotRequestStatus(request.getTimeSlotId(), false);
                        request.setStatus("Rejected");
                        requests.remove(request);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(PendingRequestsActivity.this, "Rejected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        rvPending.setAdapter(adapter);
    }
}
