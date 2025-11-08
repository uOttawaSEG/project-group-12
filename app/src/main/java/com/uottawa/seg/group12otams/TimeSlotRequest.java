package com.uottawa.seg.group12otams;

public class TimeSlotRequest {
    private String studentId;
    private String tutorId;
//    private boolean autoApproveTimeSlotSessions;
    private String timeSlotId;
    private String status;

    public TimeSlotRequest(String studentId, String tutorId, String timeSlotId, String status) {
        this.studentId = studentId;
        this.tutorId = tutorId;
        this.timeSlotId = timeSlotId;
        this.status = status;
//        this.autoApproveTimeSlotSessions = autoApproveTimeSlotSessions;
    }

    public TimeSlotRequest() {}

    // Getter for status
    public String getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }

    // Getter for studentId
    public String getStudentId() {
        return studentId;
    }

    // Getter for tutorId
    public String getTutorId() {
        return tutorId;
    }

    // Getter for timeSlotId
    public String getTimeSlotId() {
        return timeSlotId;
    }

    // Getter for autoApproveTimeSlotSessions
//    public boolean getAutoApproveTimeSlotSessions() {
//        return autoApproveTimeSlotSessions;
//    }
}
