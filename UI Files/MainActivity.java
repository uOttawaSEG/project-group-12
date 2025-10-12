package com.example.appui;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    //Instance variables
    private Button btnLoginLog, btnRegisterAsStudent, btnRegisterAsTeacher;
    private EditText edtEmailAdressLog, edtPasswordLog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();
    }
    // This method intializes the views when looking into one of the elements
    // looked at tutorial https://www.youtube.com/watch?v=hHD50che-v8&list=PLmYhEKb90q4upTnziTMzxMUGlra-KTYXu&index=3
    private void initializeViews() {
        edtEmailAdressLog = findViewById(R.id.edtEmailAdressLog);
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

    // This verifies if the log in is good
    private void attemptLogin() {
        // getting the info as a string
        String email = edtEmailAdressLog.getText().toString().trim();
        String password = edtPasswordLog.getText().toString().trim();

        // if statement to check logic ( database team needs to review to fill this in)

        if (validLoginInput(email, password)) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            String role = "";
            intent.putExtra("USER_ROLE", role);
            startActivity(intent);

        }
    }

    // input protection in case any ivalid input is entered
    private boolean validLoginInput(String email, String password) {
        // if no email is entered
        if (email.isEmpty()) {
            edtEmailAdressLog.setError("Email is required");
            return false;
        }

        // check to see if proper email is entered (got from video: https://www.youtube.com/watch?v=VD7_ovpwI_s)
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmailAdressLog.setError("Please enter a valid email adress");
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