package com.uottawa.seg.group12otams;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private String programOfStudy;
    private static final Database<Student> db = new Database<Student>(Student.class, "students");

    public Student(String firstName, String lastName, String email, String password, String phoneNumber, String programOfStudy) {
        super(firstName, lastName, email, password, phoneNumber);
        this.programOfStudy = programOfStudy;
    }

    public Student() {}

    public String getProgramOfStudy() {
        return programOfStudy;
    }

    public void setProgramOfStudy(String programOfStudy) {
        this.programOfStudy = programOfStudy;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public void update(User user) {
        Student student = (Student) user;
        this.firstName = student.firstName;
        this.lastName = student.lastName;
        this.email = student.email;
        this.password = student.password;
        this.phoneNumber = student.phoneNumber;
        this.programOfStudy = student.programOfStudy;
    }

    // Deliverable 3

    // Set a timeSlot request
    public void setTimeSlotRequest(TimeSlot timeSlot) {
        // Check if the timeSlot request first exists in the db
        List<TimeSlot> allTimeSlots = db.getTimeSlots(null);
        TimeSlot existingTimeSlot = null;
        for (TimeSlot ts : allTimeSlots) {
            if (ts.getTimeSlotId().equals(timeSlot.getTimeSlotId())) {
                existingTimeSlot = ts;
            }
        }

        if (existingTimeSlot == null) {
            // Throw error. A timeslot should exist before creating a request
            throw new IllegalArgumentException("Time slot does not exist");
        }

        // Create a timeSlot request
        db.createTimeSlotRequest(this, timeSlot);
    }

    // View all timeSlot requests

    // Remove Student from specific timeSlot request
}
