package com.uottawa.seg.group12otams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

// this is the admin inbox activity class where we will be able to navigate and accept and deny registrations
// Used this link to understand how to get result from activity: https://developer.android.com/training/basics/intents/result
public class AdminInboxActivity extends AppCompatActivity{
    // two buttons for two tabs, pending requests and rejected requests
    private Button btnPendingRequests, btnRejectedRequests;

    // Linear layout to show and hide sections
    private LinearLayout pendingLayout, rejectedLayout;

    // Recycle views to show the lists of requests
    private RecyclerView recyclerViewPending, recyclerViewRejected;

    // two array lists one for pending list and one for the rejected list
    private List<RegistrationRequest> pendingRequests = new ArrayList<>();
    private List<RegistrationRequest> rejectedRequests = new ArrayList<>();

    // we need adapters for the recycler views and UI
    private RequestAdapter pendingAdapter, rejectedAdapter;

    // this method is when the acivity runs first by creating UI setting up data and intialize the listeners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set it as the xml page
        setContentView(R.layout.activity_admin_inbox);

        // initalize the view
        initalizeViews();

        // set up the button click listeners
        setUpClickListeners();

        // we wanna start off with the pending request lists as the default
        showPendingRequests();
    }
    // this method intializes the view
    private void initalizeViews() {
        btnPendingRequests = findViewById(R.id.btnPendingRequests);
        btnRejectedRequests = findViewById(R.id.btnRejectedRequests);
        pendingLayout = findViewById(R.id.pendingLayout);
        rejectedLayout = findViewById(R.id.rejectedLayout);
        recyclerViewPending = findViewById(R.id.recyclerViewerPending);
        recyclerViewRejected = findViewById(R.id.recyclerViewerRejected);

        // set up the recyclcers with linear layout managers to display items in veritcal list
        recyclerViewPending.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRejected.setLayoutManager(new LinearLayoutManager(this));
    }

    // method to set up the click listeners

    private void setUpClickListeners() {
        // when the pending list is clicked show the pending list
        btnPendingRequests.setOnClickListener(v -> showPendingRequests());

        // when the rejected list is clicked show the rejected list
        btnRejectedRequests.setOnClickListener(v -> showRejectedRequests());
    }

    // this is the show pending request method which updates the colour of the buttons to show activity
    // it also hides the rejected section and just shows the pending section
    private void showPendingRequests() {
        // when clicking on one of the two tabs the clicked one, turns red with white text and the other is like a light greyish with red text
        btnPendingRequests.setBackgroundColor(getResources().getColor(R.color.dark_red));
        btnPendingRequests.setTextColor(getResources().getColor(R.color.white));

        // turn the rejected tab to light grey with red text
        btnRejectedRequests.setBackgroundColor(getResources().getColor(R.color.light_grey));
        btnRejectedRequests.setTextColor(getResources().getColor(R.color.dark_red));

        // show pending layout and hide rejected
        pendingLayout.setVisibility(View.VISIBLE);
        rejectedLayout.setVisibility(View.GONE);

    }

    // now for the show rejected requests method we do just swap the variables of logic
    private void showRejectedRequests() {
        // when clicking on one of the two tabs the clicked one, turns red with white text and the other is like a light greyish with red text
        btnRejectedRequests.setBackgroundColor(getResources().getColor(R.color.dark_red));
        btnRejectedRequests.setTextColor(getResources().getColor(R.color.white));

        // turn the pending tab to light grey with red text
        btnPendingRequests.setBackgroundColor(getResources().getColor(R.color.dark_gray));
        btnPendingRequests.setTextColor(getResources().getColor(R.color.white));

        // show rejected layout and hide pending
        rejectedLayout.setVisibility(View.VISIBLE);
        pendingLayout.setVisibility(View.GONE);

    }
    // we need a method to handle the results from Request Detail Actiivty
    // This is called to accept or reject a request
    // we should have request code, result code as integers and Intent to carry
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the result is what is epxted from RequestDetailActivity
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // we need to extract the action that happened if it was approve or reject
            String action = data.getStringExtra("ACTION");
            // we also need to get the request object that was used
            RegistrationRequest request = (RegistrationRequest) data.getSerializableExtra("REQUEST");

            // also need to find the position on the list
            int position = data.getIntExtra("POSITION", -1);

            // once valid move on to process the reject or accept button
            if (request != null) {
                handleRequestAction(action, request, position);
            }
        }
    }

    // This we can use a switch case for approve or reject
    // processes the actions from request detial activity class and updates UI to show changes
    private void handleRequestAction(String action, RegistrationRequest request, int position) {
        switch (action) {
            case "APPROVE":
                // handle the approval and remove from pending list
                if (pendingRequests.contains(request)) {
                    // update the UI with the change in the pending list
                    pendingAdapter.removeItem(request);
                }
                // remove from the rejected list and update the UO
                else if (rejectedRequests.contains(request)) {
                    rejectedAdapter.removeItem(request);
                }
                break;

            case "REJECT":
                // if its from pending list move it to the rejected list.
                if (pendingRequests.contains(request)) {
                    pendingAdapter.removeItem(request);
                    rejectedRequests.add(request);

                    // tell the adatper that a new item was added at the end
                    rejectedAdapter.notifyItemInserted(rejectedRequests.size() - 1);
                }
                break;
        }
    }

}
