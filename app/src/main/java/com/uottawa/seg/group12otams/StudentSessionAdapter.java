package com.uottawa.seg.group12otams;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class StudentSessionAdapter extends RecyclerView.Adapter<StudentSessionAdapter.VH> {
    public interface Callbacks { void onCancel(TimeSlotRequest request); void onRate(TimeSlotRequest request, int rating); }

    private final ArrayList<TimeSlotRequest> items;
    private final Student student;
    private final Callbacks callbacks;
    private final Map<String, TimeSlot> slots;
    private final Map<String, Tutor> tutors;

    public StudentSessionAdapter(ArrayList<TimeSlotRequest> items, Student student, Callbacks callbacks,
                                 Map<String, TimeSlot> slots, Map<String, Tutor> tutors) {
        this.items = items;
        this.student = student;
        this.callbacks = callbacks;
        this.slots = slots;
        this.tutors = tutors;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_session, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        TimeSlotRequest req = items.get(position);
        TimeSlot ts = slots.get(req.getTimeSlotId());
        Tutor t = tutors.get(req.getTutorId());

        SimpleDateFormat dateFmt = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        if (ts != null) {
            h.tvDate.setText(dateFmt.format(ts.getStartTime()));
            h.tvTime.setText(timeFmt.format(ts.getStartTime()) + " - " + timeFmt.format(ts.getEndTime()));
        } else {
            h.tvDate.setText("");
            h.tvTime.setText("");
        }
        h.tvTutor.setText(t != null ? (t.getFirstName() + " " + t.getLastName()) : "");
        String status = req.getStatus();
        h.tvStatus.setText(status);

        // Cancel button visibility rules
        boolean showCancel = false;
        if ("Pending".equals(status)) showCancel = true;
        else if ("Approved".equals(status) && ts != null) {
            showCancel = TimeUtils.canCancelApproved(ts.getStartTime(), new Date());
        }
        h.btnCancel.setVisibility(showCancel ? View.VISIBLE : View.GONE);
        h.btnCancel.setOnClickListener(v -> callbacks.onCancel(req));

        // Rating: allowed when session completed (end < now) and was Approved
        boolean canRate = ts != null && "Approved".equals(status) && ts.getEndTime().before(new Date())
                && ts.getBookedStudent() != null && student.getEmail().equals(ts.getBookedStudent().getEmail());
        h.ratingBar.setVisibility(canRate ? View.VISIBLE : View.GONE);
        h.btnRate.setVisibility(canRate ? View.VISIBLE : View.GONE);
        h.btnRate.setOnClickListener(v -> {
            int rating = Math.round(h.ratingBar.getRating());
            if (rating < 1) rating = 1;
            callbacks.onRate(req, rating);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void remove(TimeSlotRequest req) {
        int idx = items.indexOf(req);
        if (idx >= 0) {
            items.remove(idx);
            notifyItemRemoved(idx);
        }
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvStatus, tvTutor;
        Button btnCancel, btnRate;
        RatingBar ratingBar;
        VH(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvSessionDate);
            tvTime = itemView.findViewById(R.id.tvSessionTime);
            tvStatus = itemView.findViewById(R.id.tvSessionStatus);
            tvTutor = itemView.findViewById(R.id.tvSessionTutor);
            btnCancel = itemView.findViewById(R.id.btnCancelSession);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            btnRate = itemView.findViewById(R.id.btnSubmitRating);
        }
    }
}
