package com.uottawa.seg.group12otams;

import java.util.Date;

public class TimeSlot {
    private Date startTime;
    private Date endTime;
    private String tutorId;
    private String timeSlotId;
    private Student bookedStudent;

    public TimeSlot(Date startTime, Date endTime, String tutorId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.tutorId = tutorId;
        this.timeSlotId = tutorId + "-" + startTime.toString();
    }

    public TimeSlot() {}

    // get the start time
    public Date getStartTime() {
        return startTime;
    }

    // get the end time
    public Date getEndTime() {
        return endTime;
    }

    // get the tutor
    public String getTutorId() {
        return tutorId;
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
