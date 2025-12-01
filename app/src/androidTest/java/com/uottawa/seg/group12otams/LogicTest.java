package com.uottawa.seg.group12otams;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
public class LogicTest {
    @Test
    public  void cannotBookOverLappingSessions() {
        Student student = new Student("Test", "Student", "test@gmail.com", "pass", "123", "SEG2105");

        // creating overlapping time slots
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);

        Date start1 = calendar.getTime();
        calendar.add(Calendar.MINUTE, 30);
        Date end1 = calendar.getTime();

        // reset and create the overlapping slot
        calendar.add(Calendar.MINUTE, -30);
        // 15 minutes into the first slot
        calendar.add(Calendar.MINUTE, 15);
        Date start2 = calendar.getTime();
        calendar.add(Calendar.MINUTE, 30);
        Date end2 = calendar.getTime();

        // check if the overlap uses the time utils
        assertTrue("Time slots should overlap", TimeUtils.overlaps(start1, end1, start2, end2));

    }

    @Test
    public void tutoCannotDelteBookedSlot() {
        // checks to see if the tutor cannot delete the slots that are booked

        List<String> courses = Arrays.asList("SEG2105");
        Tutor tutor = new Tutor("Tutor", "Test", "tutor@email.com", "pass", "123", "Bachelors", courses, false);

        // creating the time slot with the student
        TimeSlot slot = new TimeSlot();
        slot.setBookedStudent(new Student("Booked", "Student", "booked@email.com", "pass", "123", "SEG2105"));

        // check if remove session should give an exception
        try {
            tutor.removeSession(slot);
            fail("Should have thrown excepton for deleting booked slot");
        } catch(IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Cannot remove a booked session"));
        }
    }

    @Test
    public void ratingValidation() {
        Tutor tutor = new Tutor();
        tutor.setRatings(new ArrayList<>());

        // Test if the ratings are valid
        for (int i = 1; i<=5; i++) {
            assertTrue("Rating " + i + " should be valid", i>= 1 && i<= 5);
        }

        // check for invalid ratings
        assertFalse("Rating 0 should be invalid", 0 >= 1 && 0 <=5);
        assertFalse("Rating 6 should be invalid", 6 >= 1 && 6 <=5);
    }

    @Test
    // check if the 30 min incrementation works
    public void timeSlotIncrementValidation() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);

        Date start = calendar.getTime();
        calendar.add(Calendar.MINUTE, 30);
        Date end = calendar.getTime();

        long difference = end.getTime() - start.getTime();
        boolean isValid = difference % (30 * 60 * 1000) == 0;

        assertTrue("30 minutes slot should be valid", isValid);

        // if timing is invalid
        calendar.add(Calendar.MINUTE, 15);
        Date invalidEnd = calendar.getTime();
        long invalidDifference = invalidEnd.getTime() - start.getTime();
        boolean isInvalid = invalidDifference % (30 * 60 * 1000) != 0;

        assertTrue("45 minute slot should't be valid", isInvalid);
    }
}
