package com.uottawa.seg.group12otams;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Comparator;

public class Student extends User {
    private String programOfStudy;
    private static final Database<Student> db = new Database<Student>(Student.class, "students");
    private static final Database<Tutor> tutorDb = new Database<Tutor>(Tutor.class, "tutors");

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

    // Set a timeSlot request with conflict checks
    public void setTimeSlotRequest(TimeSlot timeSlot) {
        // Validate slot exists
        List<TimeSlot> allTimeSlots = db.getTimeSlots(null);
        TimeSlot existingTimeSlot = null;
        for (TimeSlot ts : allTimeSlots) {
            if (ts.getTimeSlotId().equals(timeSlot.getTimeSlotId())) {
                existingTimeSlot = ts;
                break;
            }
        }
        if (existingTimeSlot == null) throw new IllegalArgumentException("Time slot does not exist");

        // Prevent booking if slot already booked
        if (existingTimeSlot.getBookedStudent() != null) throw new IllegalArgumentException("Time slot already booked");

        // Prevent conflicts with existing requests/approved sessions
        ArrayList<TimeSlotRequest> myRequests = getMyTimeSlotRequests();
        for (TimeSlotRequest req : myRequests) {
            TimeSlot ts = getTimeSlotById(req.getTimeSlotId());
            if (ts == null) continue;
            if (TimeUtils.overlaps(existingTimeSlot.getStartTime(), existingTimeSlot.getEndTime(), ts.getStartTime(), ts.getEndTime())) {
                throw new IllegalArgumentException("Conflicts with an existing request or booking");
            }
        }

        // Create a timeSlot request
        db.createTimeSlotRequest(this, existingTimeSlot);
    }

    // View all timeSlot requests
    public ArrayList<TimeSlotRequest> getMyTimeSlotRequests() {
        ArrayList<TimeSlotRequest> all = db.getTimeSlotRequests(null);
        ArrayList<TimeSlotRequest> mine = new ArrayList<>();
        if (all == null) return mine;
        for (TimeSlotRequest r : all) {
            if (r.getStudentId() != null && r.getStudentId().equals(this.getEmail())) {
                mine.add(r);
            }
        }
        // Sort by slot start date desc
        mine.sort((a,b) -> {
            TimeSlot tsa = getTimeSlotById(a.getTimeSlotId());
            TimeSlot tsb = getTimeSlotById(b.getTimeSlotId());
            Date da = tsa != null ? tsa.getStartTime() : new Date(0);
            Date dbb = tsb != null ? tsb.getStartTime() : new Date(0);
            return dbb.compareTo(da);
        });
        return mine;
    }

    // Remove Student from specific timeSlot request
    public void cancelRequest(String timeSlotId) {
        // Find the request
        ArrayList<TimeSlotRequest> mine = getMyTimeSlotRequests();
        TimeSlotRequest target = null;
        for (TimeSlotRequest r : mine) {
            if (r.getTimeSlotId().equals(timeSlotId)) { target = r; break; }
        }
        if (target == null) throw new IllegalArgumentException("Request not found");

        TimeSlot ts = getTimeSlotById(timeSlotId);
        if (ts == null) throw new IllegalArgumentException("Associated time slot not found");

        if ("Pending".equals(target.getStatus())) {
            db.deleteTimeSlotRequest(timeSlotId);
            return;
        }

        if ("Approved".equals(target.getStatus())) {
            Date now = new Date();
            if (!TimeUtils.canCancelApproved(ts.getStartTime(), now)) {
                throw new IllegalArgumentException("Cannot cancel within 24 hours of the session start");
            }
            // Clear booking and mark cancelled
            ts.setBookedStudent(null);
            db.modifyTimeSlot(ts);
            db.approveTimeSlotRequest(timeSlotId, false);
            return;
        }

        // Rejected: nothing to cancel
        throw new IllegalArgumentException("Request is not active");
    }

    // Past sessions for this student, sorted by most recent first
    public ArrayList<TimeSlot> getMyPastSessions() {
        ArrayList<TimeSlot> slots = db.getTimeSlots(null);
        ArrayList<TimeSlot> mine = new ArrayList<>();
        Date now = new Date();
        for (TimeSlot ts : slots) {
            if (ts.getEndTime().before(now) && ts.getBookedStudent() != null && this.getEmail().equals(ts.getBookedStudent().getEmail())) {
                mine.add(ts);
            }
        }
        mine.sort(Comparator.comparing(TimeSlot::getStartTime).reversed());
        return mine;
    }

    // Rate tutor after completed session
    public void rateTutor(String timeSlotId, int rating) {
        TimeSlot ts = getTimeSlotById(timeSlotId);
        if (ts == null) throw new IllegalArgumentException("Time slot not found");
        Date now = new Date();
        if (!ts.getEndTime().before(now)) throw new IllegalArgumentException("Session not completed yet");
        if (ts.getBookedStudent() == null || !this.getEmail().equals(ts.getBookedStudent().getEmail()))
            throw new IllegalArgumentException("You did not attend this session");
        Tutor tutor = tutorDb.getUser(ts.getTutorId());
        if (tutor == null) throw new IllegalArgumentException("Tutor not found");
        tutor.addRating(rating);
    }

    // Search available sessions by course (requires code)
    public ArrayList<TimeSlot> searchAvailableByCourse(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) throw new IllegalArgumentException("Course code is required");
        // Get all timeslots, filter by tutor offering course, in future, not booked
        ArrayList<TimeSlot> all = db.getTimeSlots(null);
        ArrayList<TimeSlot> results = new ArrayList<>();
        Date now = new Date();
        for (TimeSlot ts : all) {
            Tutor t = tutorDb.getUser(ts.getTutorId());
            if (t == null || t.getCoursesOffered() == null) continue;
            boolean offers = false;
            for (String c : t.getCoursesOffered()) {
                if (courseCode.equalsIgnoreCase(c)) { offers = true; break; }
            }
            if (!offers) continue;
            if (ts.getBookedStudent() != null) continue;
            if (!ts.getStartTime().after(now)) continue;
            // Exclude conflicts
            boolean conflict = false;
            for (TimeSlotRequest req : getMyTimeSlotRequests()) {
                TimeSlot existing = getTimeSlotById(req.getTimeSlotId());
                if (existing != null && TimeUtils.overlaps(ts.getStartTime(), ts.getEndTime(), existing.getStartTime(), existing.getEndTime())) { conflict = true; break; }
            }
            if (!conflict) results.add(ts);
        }
        results.sort(Comparator.comparing(TimeSlot::getStartTime).reversed());
        return results;
    }

    private TimeSlot getTimeSlotById(String timeSlotId) {
        ArrayList<TimeSlot> all = db.getTimeSlots(null);
        for (TimeSlot t : all) { if (t.getTimeSlotId().equals(timeSlotId)) return t; }
        return null;
    }
}
