package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class StudentRegistrationActivity extends AppCompatActivity {
    // Instance variables
    private EditText edtStudentFirstName, edtStudentLastName, edtStudentEmail, edtStudentPassword, edtStudentNumber, edtStudentStudy;
    private Button edtStudentRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        initializeViews();
        setupClickListeners();
    }

    // Initializing the views
    private void initializeViews() {
        edtStudentFirstName = findViewById(R.id.edtStudentFirstName);
        edtStudentLastName = findViewById(R.id.edtStudentLastName);
        edtStudentEmail = findViewById(R.id.edtStudentEmail);
        edtStudentPassword = findViewById(R.id.edtStudentPassWord);
        edtStudentNumber = findViewById(R.id.edtStudentNumber);
        edtStudentRegisterButton = findViewById(R.id.edtStudentRegisterButton);
        edtStudentStudy = findViewById(R.id.edtStudentStudy);

    }

    // setting up the listeners for clicks
    private void setupClickListeners() {
        edtStudentRegisterButton.setOnClickListener(v -> attemptRegistration());
    }

    // checks if the registration info is successful
    private void attemptRegistration() {
        String firstName = edtStudentFirstName.getText().toString().trim();
        String lastName = edtStudentLastName.getText().toString().trim();
        String email = edtStudentEmail.getText().toString().trim();
        String password = edtStudentPassword.getText().toString().trim();
        String phoneNumber = edtStudentNumber.getText().toString().trim();
        String program = edtStudentStudy.getText().toString().trim();

        // after checking with a boolean method to see if valid input, move on to the welcome page
        if (validInput(firstName, lastName, email, password, phoneNumber, program)) {
            // want to alert user that registration was successful
            // learned how to do it by this video: https://www.youtube.com/watch?v=hQDr7NIBS7Y
            Toast.makeText(this, "Student Registration Successful", Toast.LENGTH_SHORT).show();
            // making the jump to the welcome class
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.putExtra("USER_ROLE", "Student");
            startActivity(intent);
        }
    }

    // boolean method to see if valid input is entered
    private boolean validInput(String firstName, String lastName, String email, String password, String phoneNumber, String program) {
        // first name cannot be empty or have any anything other than character letters
        if (firstName.isEmpty()) {
            edtStudentFirstName.setError("First Name cannot be empty");
            return false;
        }

        if (!firstName.matches("[a-zA-Z]+")) {
            edtStudentFirstName.setError("Please enter a proper first name");
            return false;
        }

        // same thing applies for last name

        if (lastName.isEmpty()) {
            edtStudentLastName.setError("Last Name cannot be empty");
            return false;
        }

        if (!lastName.matches("[a-zA-Z]+")) {
            edtStudentLastName.setError("Please enter a proper last name");
            return false;
        }

        // the email and password logic would be the same from the main activity class
        // if no email is entered
        if (email.isEmpty()) {
            edtStudentEmail.setError("Email is required");
            return false;
        }

        // check to see if proper email is entered (got from video: https://www.youtube.com/watch?v=VD7_ovpwI_s)
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtStudentEmail.setError("Please enter a valid email address");
            return false;
        }

        // check if the password is empty
        if (password.isEmpty()) {
            edtStudentPassword.setError("Password is required");
            return false;
        }

        // After doing some testing I also realized that security for phone number can be similar as email
        // phone number cannot be empty and we can check to see if proper one is entered
        if (phoneNumber.isEmpty()) {
            edtStudentNumber.setError("Phone Number is required");
            return false;
        }

        if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            edtStudentNumber.setError("Please enter a proper number");
            return false;
        }

        // for the course it could involve both numbers or letters, so I will just leave it as it cannot be empty
        if (program.isEmpty()) {
            edtStudentStudy.setError("Please enter your program of study");
            return false;
        }

        // actually wait it's a title, so it should only be words
        if (!program.matches("[a-zA-Z]+")) {
            edtStudentLastName.setError("Please enter a proper program of study");
            return false;
        }
        return true;
    }
}








