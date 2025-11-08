package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateAvailabilityActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private Spinner spinnerStart, spinnerEnd;
    private RadioGroup radioGroup;
    private Button btnCreateSlot;

    private Tutor tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_availability);

        tutor = (Tutor) getIntent().getSerializableExtra("tutor");
        if (tutor == null) {
            Toast.makeText(this, "Error: tutor not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupTimeSpinners();
        setupClickListeners();
    }

    private void initializeViews() {
        datePicker = findViewById(R.id.datePicker);
        spinnerStart = findViewById(R.id.spinnerStartTime);
        spinnerEnd = findViewById(R.id.spinnerEndTime);
        radioGroup = findViewById(R.id.radioGroupApproval);
        btnCreateSlot = findViewById(R.id.btnCreateSlot);
    }

    private void setupTimeSpinners() {

        ArrayList<String> times = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            times.add(String.format("%02d:00", hour));
            times.add(String.format("%02d:30", hour));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                times
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerStart.setAdapter(adapter);
        spinnerEnd.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnCreateSlot.setOnClickListener(v -> createAvailabilitySlot());
    }

    private void createAvailabilitySlot() {

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();

        String startStr = spinnerStart.getSelectedItem().toString();
        String endStr = spinnerEnd.getSelectedItem().toString();

        Date startDate = combineDateAndTime(year, month, day, startStr);
        Date endDate = combineDateAndTime(year, month, day, endStr);

        int selectedId = radioGroup.getCheckedRadioButtonId();
        boolean autoApprove = false;

        if (selectedId != -1) {
            RadioButton selected = findViewById(selectedId);
            autoApprove = selected.getId() == R.id.radioAuto;
        }

        tutor.setAutoApproveTimeSlotSessions(autoApprove);
        try {
            tutor.setTimeSlot(startDate, endDate);
            Toast.makeText(this, "Availability slot created!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("CreateAvailability", "Error: " + e.getMessage());
        }
    }

    private Date combineDateAndTime(int year, int month, int day, String timeString) {
        String[] split = timeString.split(":");
        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, 0);
        return calendar.getTime();
    }
}
