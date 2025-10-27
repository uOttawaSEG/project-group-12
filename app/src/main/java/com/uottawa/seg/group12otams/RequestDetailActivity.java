package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This displays the registration information, different displays for student or tutor.
 * Buttons for approving or rejecting. To Think realistically this screen should also be scrollable
 * like the mail app on IOS if this an inbox so we need to know how to do that.
 * Watched this tutorial on it: https://www.youtube.com/watch?v=wJiSWMtwL6I
 */

public class RequestDetailActivity extends AppCompatActivity {
    // The text UI Elements to display the information
    private TextView txtFirstName, txtLastName, txtEmail, txtPhone, txtUserType;

    // special unique ones for tutor and student
    private TextView txtProgramOfStudy, txtHighestDegree, txtCoursesOffered;

    // the four buttons
    private Button btnApprove, btnReject, btnCancel, btnApproveRejected;

    // an instance of the registration request will be needed to know the current request being identified
    private RegistrationRequest currentRequest;
    // also will need a boolean to know if its pending or rejected
    private boolean isPending;

    // and also the position it is in the list
    private int position;

    // create the create method and set up the UI, load the data from the request and intialize the event listeners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        // initalize the UI
        intializeViews();

        // load the request data
        loadRequestData();

        // set up the click listeners
        setUpClickListeners();
    }

    // this method connects the java variables to the xml layout element IDS
    private void intializeViews() {
        // for the texts
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtUserType = findViewById(R.id.txtUserType);
        txtProgramOfStudy = findViewById(R.id.txtProgramOfStudy);
        txtHighestDegree = findViewById(R.id.txtHighestDegree);
        txtCoursesOffered = findViewById(R.id.txtCoursesOffered);

        // for the buttons
        btnApprove = findViewById(R.id.btnApprove);
        btnReject = findViewById(R.id.btnReject);
        btnCancel = findViewById(R.id.btnCancel);
        btnApproveRejected = findViewById(R.id.btnApprovedRejected);
    }

    /**
     * This will load the users request data and create it's UI, determine if its student or tutor
     * and show their own unique details and also set up the appropraite buttons
     * if its in the pending or rejcted list
     */

    private void loadRequestData() {

        // we need to get the current request thats being passed on
        currentRequest = (RegistrationRequest) getIntent().getSerializableExtra("REQUEST");

        // determine if it is pending or not
        isPending = getIntent().getBooleanExtra("IS_PENDING", true);

        // get the position from the list to
        position = getIntent().getIntExtra("POISTION", -1);

        // move on if the data is proper (for protection)

        if (currentRequest != null) {
            // lets display the common information between student and tutor

            txtFirstName.setText("First Name: " + currentRequest.getFirstName());
            txtLastName.setText("Last Name: " + currentRequest.getLastName());
            txtEmail.setText("Email: " + currentRequest.getEmail());
            txtPhone.setText("Phone: " + currentRequest.getPhone());
            txtUserType.setText("User Type: " + currentRequest.getUserType());

            // if its a student show the program of study and hide the tutors texts
            if ("Student".equals(currentRequest.getUserType())) {
                txtProgramOfStudy.setText("Program of Study: " + currentRequest.getProgramOfStudy());
                txtProgramOfStudy.setVisibility(View.VISIBLE);
                txtHighestDegree.setVisibility(View.GONE);
                txtCoursesOffered.setVisibility(View.GONE);
            }
            // its the tutor show the opposite
            else {
                txtHighestDegree.setText("Hghest Degree: " + currentRequest.getHighestDegree());
                txtCoursesOffered.setText("Courses Offered: " + currentRequest.getCoursesOffered());
                txtProgramOfStudy.setVisibility(View.GONE);
                txtHighestDegree.setVisibility(View.VISIBLE);
                txtCoursesOffered.setVisibility(View.VISIBLE);
            }

            // now its time to handle the specific situations for the buttons
            // if it's pending approve and reject only appear
            if (isPending) {
                btnApprove.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.GONE);
                btnApproveRejected.setVisibility(View.GONE);
            }
            // do the opposite for rejected list
            else {
                btnApprove.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                btnApproveRejected.setVisibility(View.VISIBLE);
            }
        }
    }

    // Time to set up the click listeners
    private void setUpClickListeners() {
        // using approval and rejection methods we assign clicks of the buttons with them
        // for pending list
        btnApprove.setOnClickListener(v -> handleApproval());
        btnReject.setOnClickListener(v -> handleRejection());

        // for approval on rejected list
        btnApproveRejected.setOnClickListener(v -> handleApproval());

        // for cancel button just close the activity with no action
        btnCancel.setOnClickListener(v -> finish());

    }

    // approval method will need intent to send result to admin inbox class and handles the approval action for both lists
    private void handleApproval() {
        Intent resultIntent = new Intent();
        // alerting that the approve action was taken
        resultIntent.putExtra("ACTION", "APPROVE");
        // the request was approved
        resultIntent.putExtra("REQUEST", currentRequest);
        // the location to remove
        resultIntent.putExtra("POSITION", position);

        // now we just set the result and finish the activity
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    // now we basically just do the same for rejection method
    private void handleRejection() {
        Intent resultIntent = new Intent();
        // alerting that the approve action was taken
        resultIntent.putExtra("ACTION", "REJECT");
        // the request was approved
        resultIntent.putExtra("REQUEST", currentRequest);
        // the location to remove
        resultIntent.putExtra("POSITION", position);

        // now we just set the result and finish the activity
        setResult(RESULT_OK, resultIntent);
        finish();
    }


}
