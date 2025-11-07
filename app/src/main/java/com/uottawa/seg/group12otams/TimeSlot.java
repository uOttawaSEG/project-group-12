package com.uottawa.seg.group12otams;

import java.util.Date;

public class TimeSlot {
    private Date startTime;
    private Date endTime;
    private Tutor tutor;
    private String timeSlotId;
    private Student bookedStudent;

    public TimeSlot(Date startTime, Date endTime, Tutor tutor) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.tutor = tutor;
        this.timeSlotId = startTime.toString() + "-" + tutor.getEmail();
    }

    // get the start time
    public Date getStartTime() {
        return startTime;
    }

    // get the end time
    public Date getEndTime() {
        return endTime;
    }

    // get the tutor
    public Tutor getTutor() {
        return tutor;
    }

    // Get timeSlotId
    public String getTimeSlotId() {
        return timeSlotId;
    }

    // Get booked student
    public Student getBookedStudent() {
        return bookedStudent;
    }

    // Set booked student
    public void setBookedStudent(Student student) {
        this.bookedStudent = student;
    }
}
