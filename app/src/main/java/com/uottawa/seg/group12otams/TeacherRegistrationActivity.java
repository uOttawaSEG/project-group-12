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

import java.util.ArrayList;

public class TeacherRegistrationActivity extends AppCompatActivity {


    // Instance Variables
    private EditText edtTeacherFirstName, edtTeacherLastName, edtTeacherEmail, edtTeacherPassword, edtTeacherNumber, edtTeacherHighestDegree, edtTeacherCoursesOffered;
    private Button edtTeacherRegistrationButton;
    private Database<Tutor> tutorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_registration);

        initializeViews();
        setupClickListeners();
        tutorDatabase = new Database<Tutor>(Tutor.class, "tutors");

    }


    // initializing the views when clicking
    private void initializeViews() {
        edtTeacherFirstName = findViewById(R.id.edtTeacherFirstName);
        edtTeacherLastName = findViewById(R.id.edtTeacherLastName);
        edtTeacherEmail = findViewById(R.id.edtTeacherEmail);
        edtTeacherPassword = findViewById(R.id.edtTeacherPassword);
        edtTeacherNumber = findViewById(R.id.edtTeacherNumber);
        edtTeacherHighestDegree = findViewById(R.id.edtTeachersHighestDegree);
        edtTeacherCoursesOffered = findViewById(R.id.edtTeachersCoursesOffered);
        edtTeacherRegistrationButton = findViewById(R.id.edtTeacherRegisterButton);
    }


    // setting up the listeners for clicks
    private void setupClickListeners() {
        edtTeacherRegistrationButton.setOnClickListener(v -> attemptRegistration());
    }

    // logic to see if the teacher registration is successful
    private void attemptRegistration() {
        String firstName = edtTeacherFirstName.getText().toString().trim();
        String lastName = edtTeacherLastName.getText().toString().trim();
        String email = edtTeacherEmail.getText().toString().trim();
        String password = edtTeacherPassword.getText().toString().trim();
        String phoneNumber = edtTeacherNumber.getText().toString().trim();
        String degree = edtTeacherHighestDegree.getText().toString().trim();
        String courses = edtTeacherCoursesOffered.getText().toString().trim();

        // using an if statement to check
        if (validInput(firstName, lastName, email, password, phoneNumber, degree, courses)) {
            // Convert comma-seperated string of courses to ArrayList
            ArrayList<String> coursesList = new ArrayList<String>();
            String[] coursesArray = courses.split(",");
            for (String course : coursesArray) {
                coursesList.add(course.trim());
            }

            // add registration to database
            Tutor newUser = new Tutor(firstName, lastName, email, password, phoneNumber, degree, coursesList, false);
            tutorDatabase.createRegistrationRequest(newUser, "Tutor");

            // make a pop up message to show registration successful
            Toast.makeText(this, "Tutor Registration Pending", Toast.LENGTH_SHORT).show();
            // jump to the welcome page
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("USER_ROLE", "Tutor");
            startActivity(intent);
        }
    }

    // boolean method to see if input is valid
    private boolean validInput(String firstName, String lastName, String email, String password, String phoneNumber, String degree, String courses) {
    // most of the logic would be the same from the student registration class except for the last two
        // first name cannot be empty or have any anything other than character letters
        if (firstName.isEmpty()) {
            edtTeacherFirstName.setError("First Name cannot be empty");
            return false;
        }

        if (!firstName.matches("[a-zA-Z]+")) {
            edtTeacherFirstName.setError("Please enter a proper first name");
            return false;
        }

        // same thing applies for last name

        if (lastName.isEmpty()) {
            edtTeacherLastName.setError("Last Name cannot be empty");
            return false;
        }

        if (!lastName.matches("[a-zA-Z]+")) {
            edtTeacherLastName.setError("Please enter a proper last name");
            return false;
        }

        // the email and password logic would be the same from the main activity class
        // if no email is entered
        if (email.isEmpty()) {
            edtTeacherEmail.setError("Email is required");
            return false;
        }

        // check to see if proper email is entered (got from video: https://www.youtube.com/watch?v=VD7_ovpwI_s)
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtTeacherEmail.setError("Please enter a valid email address");
            return false;
        }

        // check if the password is empty
        if (password.isEmpty()) {
            edtTeacherPassword.setError("Password is required");
            return false;
        }

        // After doing some testing I also realized that security for phone number can be similar as email
        // phone number cannot be empty and we can check to see if proper one is entered
        if (phoneNumber.isEmpty()) {
            edtTeacherNumber.setError("Phone Number is required");
            return false;
        }

        if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            edtTeacherNumber.setError("Please enter a proper number");
            return false;
        }

        // degree would have the same logic as the first name and last name
        if (degree.isEmpty()) {
            edtTeacherHighestDegree.setError("Highest degree must be filled");
            return false;
        }

        if (!degree.matches("[a-zA-Z]+")) {
            edtTeacherHighestDegree.setError("Please enter a proper degree");
            return false;
        }

        // same thing with courses offered, but we would need to allow a comma in case they have multiple courses
        if (courses.isEmpty()) {
            edtTeacherCoursesOffered.setError("Courses offered must be filled");
            return false;
        }

        // numbers should also be allowed example SEG 2105
        if (!courses.matches("[a-zA-Z0-9, ]+")) {
            edtTeacherCoursesOffered.setError("Please enter proper courses");
            return false;
        }

        return true;
    }
}