package com.uottawa.seg.group12otams;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminApprovalActivity extends AppCompatActivity implements RequestAdapter.OnRequestActionListener {
    private RecyclerView rvPendingRequests, rvRejectedRequests;
    private Button btnLogout;
    private Database<Student> studentDatabase;
    private Database<Tutor> tutorDatabase;
    private RequestAdapter pendingAdapter, rejectedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approval);

        studentDatabase = new Database<Student>(Student.class, "students");
        tutorDatabase = new Database<Tutor>(Tutor.class, "tutors");

        rvPendingRequests = findViewById(R.id.rvPendingRequests);
        rvRejectedRequests = findViewById(R.id.rvRejectedRequests);
        btnLogout = findViewById(R.id.btnLogout);

        pendingAdapter = new RequestAdapter(this);
        rejectedAdapter = new RequestAdapter(this);
        rvPendingRequests.setLayoutManager(new LinearLayoutManager(this));
        rvRejectedRequests.setLayoutManager(new LinearLayoutManager(this));
        rvPendingRequests.setAdapter(pendingAdapter);
        rvRejectedRequests.setAdapter(rejectedAdapter);

        loadRequests();

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void loadRequests() {
        // Load all registration requests (both Student and Tutor) from the shared collection
        studentDatabase.getRegistrationRequests("Pending", task -> {
            if (task.isSuccessful()) {
                pendingAdapter.setRequests(task.getResult().getDocuments());
            }
        });
        studentDatabase.getRegistrationRequests("Rejected", task -> {
            if (task.isSuccessful()) {
                rejectedAdapter.setRequests(task.getResult().getDocuments());
            }
        });
    }

    @Override
    public void onApprove(DocumentSnapshot request) {
        String email = request.getString("email");
        String role = request.getString("role");
        String password = request.getString("password");

        if(role.equals("Student")) {
            Student student = new Student(
                    request.getString("firstName"),
                    request.getString("lastName"),
                    email,
                    password,
                    request.getString("phoneNumber"),
                    request.getString("programOfStudy")
            );

            studentDatabase.createUser(student);
            studentDatabase.updateRequestStatus(email, "Approved", task -> {
                if (task.isSuccessful()) {
                    pendingAdapter.removeRequest(request);
                    rejectedAdapter.removeRequest(request);
                }
            });
        }
        else {
            Tutor tutor = new Tutor(
                    request.getString("firstName"),
                    request.getString("lastName"),
                    email,
                    password,
                    request.getString("phoneNumber"),
                    request.getString("highestDegree"),
                    (ArrayList<String>) request.get("coursesOffered")
            );
            tutorDatabase.createUser(tutor);
            tutorDatabase.updateRequestStatus(email, "Approved", task -> {
                if (task.isSuccessful()) {
                    pendingAdapter.removeRequest(request);
                    rejectedAdapter.removeRequest(request);
                }
            });
        }

        Toast.makeText(this, "User approved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReject(DocumentSnapshot request) {
        String email = request.getString("email");
        studentDatabase.updateRequestStatus(email, "Rejected", task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "User rejected", Toast.LENGTH_SHORT).show();
                // Refresh UI immediately
                pendingAdapter.removeRequest(request);
                rejectedAdapter.addRequest(request);
            }
        });
    }
}