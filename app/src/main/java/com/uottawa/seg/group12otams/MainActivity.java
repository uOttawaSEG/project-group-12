package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.Task;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //Instance variables
    private Button btnLoginLog, btnRegisterAsStudent, btnRegisterAsTeacher;
    private EditText edtEmailAddressLog, edtPasswordLog;
    private Database<Administrator> administratorDatabase;
    private Database<Student> studentDatabase;
    private Database<Tutor> tutorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeDatabase();
        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        edtEmailAddressLog = findViewById(R.id.edtEmailAddressLog);
        edtPasswordLog = findViewById(R.id.edtPasswordLog);
        btnLoginLog = findViewById(R.id.btnLoginLog);
        btnRegisterAsTeacher = findViewById(R.id.btnRegisterAsTeacher);
        btnRegisterAsStudent = findViewById(R.id.btnRegisterAsStudent);
    }

    // This methods helps listen for the clicks to help move onto the next page.
    private void setupClickListeners() {
        btnLoginLog.setOnClickListener(v -> attemptLogin());
        btnRegisterAsStudent.setOnClickListener(v -> navigateToStudentRegistration());
        btnRegisterAsTeacher.setOnClickListener(v -> navigateToTeacherRegistration());
    }

    // Creates the appropriate database objects
    private void initializeDatabase() {
        administratorDatabase = new Database<Administrator>(Administrator.class, "administrators");
        studentDatabase = new Database<Student>(Student.class, "students");
        tutorDatabase = new Database<Tutor>(Tutor.class, "tutors");
    }

    private User searchDatabase(String email) {
        Administrator adminUser = administratorDatabase.getUser(email);
        Student studentUser = studentDatabase.getUser(email);
        Tutor tutorUser = tutorDatabase.getUser(email);

        if (adminUser != null) return adminUser;
        if (studentUser != null) return studentUser;
        if (tutorUser != null) return tutorUser;
        return null;
    }

    // This verifies if the log in is good
    private void attemptLogin() {
        // getting the info as a string
        String email = edtEmailAddressLog.getText().toString().trim();
        String password = edtPasswordLog.getText().toString().trim();

        // if statement to check logic
        if (!validLoginInput(email, password)) {
            return;
        }

        // Check if admin
        Administrator admin = administratorDatabase.getUser(email);
        if (admin != null && admin.getPassword().equals(password)) {
            Intent intent = new Intent(this, AdminApprovalActivity.class);
            intent.putExtra("USER_ROLE", "Administrator");
            startActivity(intent);
            return;
        }

        // Check registration request status
        studentDatabase.getRegistrationRequest(email, task -> handleRegistrationResponse(task, email, password));
    }

    private void handleRegistrationResponse(Task<DocumentSnapshot> task, String email, String password) {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
            DocumentSnapshot doc = task.getResult();
            String status = doc.getString("status");
            String role = doc.getString("role");

            if (status == null || role == null) {
                Toast.makeText(this, "Registration data is incomplete.", Toast.LENGTH_SHORT).show();
                return;
            }

            switch (status) {
                case "Approved":
                    User user = "Student".equals(role) ? studentDatabase.getUser(email) : tutorDatabase.getUser(email);
                    if (user != null && user.getPassword().equals(password)) {
                        Intent intent = new Intent(this, WelcomeActivity.class);
                        intent.putExtra("USER_ROLE", role);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "Rejected":
                    Toast.makeText(this, "Registration rejected. Contact (123) 456-7890.", Toast.LENGTH_LONG).show();
                    break;
                case "Pending":
                    Toast.makeText(this, "Registration is pending approval.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "Unknown registration status.", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            Toast.makeText(this, "No registration found.", Toast.LENGTH_SHORT).show();
        }
    }


    // input protection in case any valid input is entered
    private boolean validLoginInput(String email, String password) {
        // if no email is entered
        if (email.isEmpty()) {
            edtEmailAddressLog.setError("Email is required");
            return false;
        }

        // check to see if proper email is entered (got from video: https://www.youtube.com/watch?v=VD7_ovpwI_s)
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmailAddressLog.setError("Please enter a valid email address");
            return false;
        }

        // check if the password is empty
        if (password.isEmpty()) {
            edtPasswordLog.setError("Password is requires");
            return false;
        }

        return true;
    }

    // move to student registration page
    private void navigateToStudentRegistration() {
        Intent intent = new Intent(this, StudentRegistrationActivity.class);
        startActivity(intent);
    }

    // move to teacher registration page
    private void navigateToTeacherRegistration() {
        Intent intent = new Intent(this, TeacherRegistrationActivity.class);
        startActivity(intent);
    }

}