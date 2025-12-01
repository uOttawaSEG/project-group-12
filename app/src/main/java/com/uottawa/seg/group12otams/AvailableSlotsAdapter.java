package com.uottawa.seg.group12otams;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AvailableSlotsAdapter extends RecyclerView.Adapter<AvailableSlotsAdapter.VH> {
    interface Callbacks { void onRequest(TimeSlot slot); }

    private final ArrayList<TimeSlot> items;
    private final Student student;
    private final Callbacks callbacks;
    private final Database<Tutor> tutorDb = new Database<>(Tutor.class, "tutors");

    public AvailableSlotsAdapter(ArrayList<TimeSlot> items, Student student, Callbacks callbacks) {
        this.items = items;
        this.student = student;
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_available_slot, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        TimeSlot ts = items.get(position);
        Tutor t = tutorDb.getUser(ts.getTutorId());
        SimpleDateFormat dateFmt = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        h.tvDate.setText(dateFmt.format(ts.getStartTime()));
        h.tvTime.setText(timeFmt.format(ts.getStartTime()) + " - " + timeFmt.format(ts.getEndTime()));
        String tutorName = t != null ? (t.getFirstName() + " " + t.getLastName()) : "";
        double avg = t != null ? t.getAverageRating() : 0.0;
        h.tvTutor.setText(tutorName + "  •  ⭐ " + new DecimalFormat("0.0").format(avg));
        h.btnRequest.setOnClickListener(v -> callbacks.onRequest(ts));
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void remove(TimeSlot slot) {
        int idx = items.indexOf(slot);
        if (idx >= 0) { items.remove(idx); notifyItemRemoved(idx);}    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvTutor;
        Button btnRequest;
        public VH(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvAvailDate);
            tvTime = itemView.findViewById(R.id.tvAvailTime);
            tvTutor = itemView.findViewById(R.id.tvTutorAndRating);
            btnRequest = itemView.findViewById(R.id.btnRequestSlot);
        }
    }
}
