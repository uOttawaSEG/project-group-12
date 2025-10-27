package com.uottawa.seg.group12otams;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// video I used to understand Recycle view and get general idea of how UI should look like: https://www.youtube.com/watch?v=TAEbP_ccjsk

// This is the request adapter class which will use recycle view to display the registration requests in lists just like an email

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    // creating the list of requested registrations
    private List<RegistrationRequest> requests;

    // flag to figure out if its for pending or rejceted requests
    private boolean isPendingList;

    // reference to the main admission inbox class for navigation
    private AdminInboxActivity activity;

    // constructor
    public RequestAdapter(List<RegistrationRequest> requests, AdminInboxActivity activity, boolean isPendingList) {
        this.requests = requests;
        this.isPendingList = isPendingList;
        this.activity = activity;
    }

    // this is the remove method which will remove a registration from the list, notfiies the adaptor to update with nice animation
    public void removeItem(int position) {
        // remove the request from the list
        requests.remove(position);
        // notify adapter to trigger animation
        notifyItemRemoved(position);

        // notify that the list has changed so this keeps proper index
        notifyItemRangeChanged(position, requests.size());
    }

    // this is also the remove method by using the request object
    public void removeItem(RegistrationRequest request) {
        // find the position of the request
        int position = requests.indexOf(request);

        // remove it using the position remove method
        // check to see if the position exists
        if (position != -1) {
            removeItem(position);
        }
    }

    // this is the request viewholder method prepare to create a new view
    // parent means the view group which view will be updated.
    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout and create the new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registration_request, parent, false);
        return new RequestViewHolder(view);
    }

    // This displays the data at their specified positions
    // we will have the new view holder updated and position of item within data set
    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        // get the position
        RegistrationRequest request = requests.get(position);

        // bind the data to the view holder
        holder.bind(request);

        // set the click listeners for the entire item view
        holder.itemView.setOnClickListener(v -> {
            // time to create the intent
            Intent intent = new Intent(activity, RequestDetailActivity.class);

            // pass the data and information
            intent.putExtra("REQUEST", request);
            intent.putExtra("IS_PENDING", isPendingList);
            intent.putExtra("POSITION", position);

            // start the activity to handle the rejections and acceptances
            activity.startActivityForResult(intent, 1);
        });

    }

    // get the total number of items in the data set held by the adapter
    @Override
    public int getItemCount() {
        return requests.size();
    }

    // we need to create an inner class that can hold the views for each own item

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        // ui elements displaying the information
        private TextView txtUserName, txtUserType, txtStudentInfo, txtTutorDegree, txtTutorCourses;

        // constructor for view holders
        // item view is the inflated item view
        public RequestViewHolder (@NonNull View itemView) {
            super(itemView);

            // initalize the ui elements for thier own views
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtUserType = itemView.findViewById(R.id.txtUserType);
            txtStudentInfo = itemView.findViewById(R.id.txtStudentInfo);
            txtTutorDegree = itemView.findViewById(R.id.txtTutorDegree);
            txtTutorCourses = itemView.findViewById(R.id.txtTutorCourses);

        }

        // bind a request to a view holders view
        public void bind(RegistrationRequest request) {
            // set user names gonna do whole name together
            txtUserName.setText(request.getFirstName() + " " + request.getLastName());

            // set the user type as tutor or student
            txtUserType.setText(request.getUserType());

            // display the studnet information
            if ("Student".equals(request.getUserType())) {
                txtStudentInfo.setText("Program: " + request.getProgramOfStudy());
                txtStudentInfo.setVisibility(View.VISIBLE);
                txtTutorDegree.setVisibility(View.GONE);
                txtTutorCourses.setVisibility(View.GONE);
            }
            // display tutor information
            else {
                txtTutorDegree.setText("Highest Degree: " + request.getHighestDegree());
                txtTutorCourses.setText("Course Offered: " + request.getCoursesOffered());
                txtStudentInfo.setVisibility(View.GONE);
                txtTutorDegree.setVisibility(View.VISIBLE);
                txtTutorCourses.setVisibility(View.VISIBLE);
            }
        }
    }
}

