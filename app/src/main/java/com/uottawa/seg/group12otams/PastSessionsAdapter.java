package com.uottawa.seg.group12otams;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PastSessionsAdapter extends RecyclerView.Adapter<PastSessionsAdapter.ViewHolder> {

    private ArrayList<TimeSlot> pastSessions;

    public PastSessionsAdapter(ArrayList<TimeSlot> pastSessions) {
        this.pastSessions = pastSessions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_past_session, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeSlot ts = pastSessions.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        holder.tvDate.setText(dateFormat.format(ts.getStartTime()));
        holder.tvTime.setText(
                timeFormat.format(ts.getStartTime()) + " - " +
                        timeFormat.format(ts.getEndTime())
        );
    }

    @Override
    public int getItemCount() {
        return pastSessions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvAvailabilityDate);
            tvTime = itemView.findViewById(R.id.tvAvailabilityTime);
        }
    }
}
