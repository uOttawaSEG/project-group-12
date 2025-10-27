package com.uottawa.seg.group12otams;

// imported this so I can pass with intents
import java.io.Serializable;

// This is the class that represents a user requesting thier registration in the system.
// All information is passed on except passwords and Admin uses this to accept or reject registrations.
public class RegistrationRequest implements Serializable {
    // Instance variables which are the variables common between tutor and student excluding password.
    private String id; // This is to uniquely identify each request
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String userType; // to determine if student or tutor
    private String status; // figure out if it's approved, rejected or pending

    // variable unique for student registration
    private String programOfStudy;

    // variables unqiue for the tutor registration
    private String highestDegree;
    private String coursesOffered;


    // Now we will need to have unique construcotrs for both student and tutor.
    // Also will then need getter methods but not setter methods because once registered, the data can't change.

    public RegistrationRequest(String id, String firstName, String lastName, String email, String phone, String userType, String status, String programofStudy) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.status = status;
        this.programOfStudy = programofStudy;
        // for the tutor instance variables just set them to null;
        this.highestDegree = null;
        this.coursesOffered = null;
    }

    // constructor for the tutor class
    public RegistrationRequest(String id, String firstName, String lastName, String email, String phone, String userType, String status, String highestDegree, String coursesOffered) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.status = status;
        this.highestDegree = highestDegree;
        this.coursesOffered = coursesOffered;
        // this time set program of study to null since this is tutor constructor
        this.programOfStudy = null;
    }

    // getter methods for all instance variables

    // get the ID
    public String getId() {
        return id;
    }

    // get the first name
    public String getFirstName() {
        return firstName;
    }

    // get the last name
    public String getLastName() {
        return lastName;
    }

    // get the email
    public String getEmail() {
        return email;
    }

    // get the phone number
    public String getPhone() {
        return phone;
    }

    // get the user type
    public String getUserType() {
        return userType;
    }

    // get the status
    public String getStatus() {
        return status;
    }

    // get the program of study
    public String getProgramOfStudy() {
        return programOfStudy;
    }

    // get the tutors highest degree
    public String getHighestDegree() {
        return highestDegree;
    }

    // get the tutors courses offered
    public String getCoursesOffered() {
        return coursesOffered;
    }
}
