package com.uottawa.seg.group12otams;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tutor extends User {
    private static final Database<Tutor> db = new Database<Tutor>(Tutor.class, "tutors");
    private String highestDegree;
    private List<String> coursesOffered;
    private boolean autoApproveTimeSlotSessions = false;

    public Tutor(String firstName, String lastName, String email, String password, String phoneNumber, String highestDegree, List<String> coursesOffered) {
        super(firstName, lastName, email, password, phoneNumber);
        this.highestDegree = highestDegree;
        this.coursesOffered = coursesOffered;
    }

    public Tutor() {}

    public String getHighestDegree() {
        return highestDegree;
    }

    public List<String> getCoursesOffered() {
        return coursesOffered;
    }

    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree;
    }

    public void setCoursesOffered(List<String> coursesOffered) {
        this.coursesOffered = coursesOffered;
    }

    @Override
    public String getRole() {
        return "Tutor";
    }

    @Override
    public void update(User user) {
        Tutor tutor = (Tutor) user;
        this.firstName = tutor.firstName;
        this.lastName = tutor.lastName;
        this.email = tutor.email;
        this.password = tutor.password;
        this.phoneNumber = tutor.phoneNumber;
        this.highestDegree = tutor.highestDegree;
        this.coursesOffered = tutor.coursesOffered;
    }

    // Deliverable 3 Notes

    // Select start/end time
    // Needs to be valid (30min increments, future date, no overlapping time slots)
    @Exclude
    public void setTimeSlot(Date startDate, Date endDate) {

        // Throw error if startDate is after endDate
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        // Throw error if startDate and endDate are not 30 min apart
        long diff = endDate.getTime() - startDate.getTime();
        if (diff % (30 * 60 * 1000) != 0) {
            throw new IllegalArgumentException("Time slot must be 30 minutes apart");
        }

        // Throw an error if the startDate is in the past
        Date now = new Date();
        if (startDate.before(now)) {
            throw new IllegalArgumentException("Start date must be in the future");
        }

        // Throw an error if the endDate is in the past
        if (endDate.before(now)) {
            throw new IllegalArgumentException("End date must be in the future");
        }

        // Fetch timeslots from db
        ArrayList<TimeSlot> timeSlots = db.getTimeSlots(this);

        // Throw an error if the startDate overlaps with any existing time slots
        for (TimeSlot timeSlot : timeSlots) {
            if (startDate.after(timeSlot.getEndTime()) || endDate.before(timeSlot.getStartTime())) {
                continue;
            } else {
                throw new IllegalArgumentException("Time slot overlaps with existing time slot");
            }
        }

        // Add the new time slot to the db
        db.modifyTimeSlot(new TimeSlot(startDate, endDate, this));
    }

    // Get upcoming sessions
    @Exclude
    public ArrayList<TimeSlot> getUpcomingSessions() {
        // fetch from db
        ArrayList<TimeSlot> timeSlots = db.getTimeSlots(this);

        // filter out past sessions
        ArrayList<TimeSlot> upcomingSessions = new ArrayList<>();
        Date now = new Date();

        for (TimeSlot timeSlot : timeSlots) {
            if (timeSlot.getStartTime().after(now)) {
                upcomingSessions.add(timeSlot);
            }
        }

        return upcomingSessions;
    }

    // Get past sessions
    @Exclude
    public ArrayList<TimeSlot> getPastSessions() {
        // fetch from db
        ArrayList<TimeSlot> timeSlots = db.getTimeSlots(this);

        // filter out future sessions
        ArrayList<TimeSlot> pastSessions = new ArrayList<>();
        Date now = new Date();

        for (TimeSlot timeSlot : timeSlots) {
            if (timeSlot.getEndTime().before(now)) {
                pastSessions.add(timeSlot);
            }
        }

        return pastSessions;
    }

    // Remove session
    @Exclude
    public void removeSession(TimeSlot timeSlot) {
        // Check if a student has booked the timeSlot
        if (timeSlot.getBookedStudent() != null) {
            // Throw error (we can't remove a booked session!)
            throw new IllegalArgumentException("Cannot remove a booked session");
        }

        // Otherwise, delete from db
        db.deleteTimeSlot(timeSlot.getTimeSlotId());
    }

    // Set autoApproveTimeSlotRequest
    @Exclude
    public void setAutoApproveTimeSlotSessions(boolean autoApproveTimeSlotSessions) {
        this.autoApproveTimeSlotSessions = autoApproveTimeSlotSessions;
    }

    // Getter for autoApproveTimeSlotSessions
    @Exclude
    public boolean getAutoApproveTimeSlotSessions() {
        return autoApproveTimeSlotSessions;
    }

    // Approve or reject a timeslot request
    @Exclude
    public void setTimeSlotRequestStatus(String timeSlotId, boolean isApproved) {
        db.approveTimeSlotRequest(timeSlotId, isApproved);
    }

    // Get all timeSlot requests
    @Exclude
    public ArrayList<TimeSlotRequest> getTimeSlotRequests() {
        // Fetch from db
        return db.getTimeSlotRequests(this);
    }

    // Get pending timeSlot requests
    @Exclude
    public ArrayList<TimeSlotRequest> getPendingTimeSlotRequests() {
        // Fetch from db
        ArrayList<TimeSlotRequest> timeSlotRequests = db.getTimeSlotRequests(this);

        // Get pending time slot requests
        ArrayList<TimeSlotRequest> pendingTimeSlotRequests = new ArrayList<>();
        for (TimeSlotRequest timeSlotRequest : timeSlotRequests) {
            if (timeSlotRequest.getStatus().equals("Pending")) {
                pendingTimeSlotRequests.add(timeSlotRequest);
            }
        }

        return pendingTimeSlotRequests;
    }
}
