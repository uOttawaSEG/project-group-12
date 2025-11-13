package com.uottawa.seg.group12otams;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static java.util.regex.Pattern.matches;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest2 {
    @Rule
    public ActivityScenarioRule<StudentRegistrationActivity> mActivityTestRule = new
            ActivityScenarioRule<>(StudentRegistrationActivity.class);
    @Test
    public void emailIsInvalid() {
        onView(withId(R.id.edtStudentFirstName)).perform(typeText("user"), closeSoftKeyboard());
        onView(withId(R.id.edtStudentLastName)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.edtStudentEmail)).perform(typeText("email@"), closeSoftKeyboard());
        onView(withId(R.id.edtStudentRegisterButton)).perform(click());
//        onView(withText("Please enter a valid email address")).check(matches(isDisplayed()));
        onView(withId(R.id.edtStudentEmail))
                .check(matches(hasErrorText("Please enter a valid email address")));
    }
}
