package com.uottawa.seg.group12otams;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private List<DocumentSnapshot> requests;
    private OnRequestActionListener listener;

    public interface OnRequestActionListener {
        void onApprove(DocumentSnapshot request);
        void onReject(DocumentSnapshot request);
    }

    public RequestAdapter(OnRequestActionListener listener) {
        this.requests = new ArrayList<>();
        this.listener = listener;
    }

    public void setRequests(List<DocumentSnapshot> requests) {
        this.requests = requests;
        notifyDataSetChanged();
    }

    public void removeRequest(DocumentSnapshot request) {
        int position = requests.indexOf(request);
        if (position != -1) {
            requests.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addRequest(DocumentSnapshot request) {
        requests.add(request);
        notifyItemInserted(requests.size() - 1);
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        DocumentSnapshot request = requests.get(position);
        String name = request.getString("firstName") + " " + request.getString("lastName");
        String email = request.getString("email");
        String phoneNumber = request.getString("phoneNumber");
        String role = request.getString("role");
        String details;
        
        if ("Student".equals(role)) {
            details = "Role: Student\n" +
                     "Phone: " + phoneNumber + "\n" +
                     "Program: " + request.getString("programOfStudy");
        } else if ("Tutor".equals(role)) {
            details = "Role: Tutor\n" +
                     "Phone: " + phoneNumber + "\n" +
                     "Degree: " + request.getString("highestDegree") + "\n" +
                     "Courses: " + request.get("coursesOffered");
        } else {
            details = "Role: " + role + "\nPhone: " + phoneNumber;
        }

        holder.txtName.setText(name);
        holder.txtEmail.setText(email);
        holder.txtDetails.setText(details);

        boolean isPending = request.getString("status").equals("Pending");
        holder.btnApprove.setVisibility(View.VISIBLE); // Always allow approve (for rejected requests)
        holder.btnReject.setVisibility(isPending ? View.VISIBLE : View.GONE); // Reject only for pending

        holder.btnApprove.setOnClickListener(v -> listener.onApprove(request));
        holder.btnReject.setOnClickListener(v -> listener.onReject(request));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail, txtDetails;
        Button btnApprove, btnReject;

        RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtDetails = itemView.findViewById(R.id.txtDetails);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}