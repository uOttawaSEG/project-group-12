package com.uottawa.seg.group12otams;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PendingRequestsAdapter extends RecyclerView.Adapter<PendingRequestsAdapter.ViewHolder> {

    public interface RequestActions {
        void onApprove(TimeSlotRequest request);
        void onReject(TimeSlotRequest request);
    }

    private ArrayList<TimeSlotRequest> requests;
    private RequestActions actions;

    public PendingRequestsAdapter(ArrayList<TimeSlotRequest> requests, RequestActions actions) {
        this.requests = requests;
        this.actions = actions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudent, tvTime;
        Button btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudent = itemView.findViewById(R.id.tvRequestStudent);
            tvTime = itemView.findViewById(R.id.tvRequestTime);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request_tutor, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeSlotRequest request = requests.get(position);

        holder.tvStudent.setText("Student: " + request.getStudentId());

        holder.tvTime.setText("TimeSlot ID: " + request.getTimeSlotId());

        holder.btnApprove.setOnClickListener(v -> actions.onApprove(request));
        holder.btnReject.setOnClickListener(v -> actions.onReject(request));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}
