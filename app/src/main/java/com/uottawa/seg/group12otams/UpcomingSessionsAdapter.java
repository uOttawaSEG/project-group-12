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

public class UpcomingSessionsAdapter extends RecyclerView.Adapter<UpcomingSessionsAdapter.ViewHolder> {

    public interface OnCancelListener {
        void onCancel(TimeSlot timeSlot);
    }

    private ArrayList<TimeSlot> timeSlots;
    private OnCancelListener cancelListener;

    public UpcomingSessionsAdapter(ArrayList<TimeSlot> timeSlots, OnCancelListener cancelListener) {
        this.timeSlots = timeSlots;
        this.cancelListener = cancelListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_upcoming_session, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeSlot ts = timeSlots.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, HH:mm", Locale.ENGLISH);
        String time = sdf.format(ts.getStartTime()) + " → " + sdf.format(ts.getEndTime());

        holder.tvSessionTime.setText(time);

        Student booked = ts.getBookedStudent();
        if (booked != null) {
            holder.bookedStudent.setText("Booked by: " + booked.getFirstName() + " " + booked.getLastName());
        } else {
            holder.bookedStudent.setText("Not booked");
        }

        holder.btnCancel.setOnClickListener(v -> cancelListener.onCancel(ts));
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSessionTime;
        TextView bookedStudent;
        Button btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSessionTime = itemView.findViewById(R.id.tvSessionTime);
            bookedStudent = itemView.findViewById(R.id.bookedStudent);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}
