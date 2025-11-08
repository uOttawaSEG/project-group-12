package com.uottawa.seg.group12otams;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class PastSessionsActivity extends AppCompatActivity {

    private Tutor tutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_sessions);
        tutor = (Tutor) getIntent().getSerializableExtra("tutor");
        Log.e("PastSessionsActivity", "Sessions: " + tutor.getPastSessions());
    }
}
