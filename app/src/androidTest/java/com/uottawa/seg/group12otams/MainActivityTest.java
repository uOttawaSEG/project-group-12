package com.uottawa.seg.group12otams;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.widget.TextView;

import androidx.test.annotation.UiThreadTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<StudentRegistrationActivity> mActivityRule = new ActivityScenarioRule<>(StudentRegistrationActivity.class);

    private StudentRegistrationActivity mActivity;
    private TextView text;

    @Before
    public void setUp() {
        // Use the scenario to access the activity instance
        mActivityRule.getScenario().onActivity(activity -> {
            mActivity = activity;
        });
    }

    // Test in StudentRegistrationActivity
    @Test
    @UiThreadTest
    public void checkFirstName() {
        text = mActivity.findViewById(R.id.edtStudentFirstName);
        text.setText("user1");
        String name = text.getText().toString();

        assertNotEquals("user", name);
    }
}