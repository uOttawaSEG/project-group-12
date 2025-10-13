package com.uottawa.seg.group12otams;

import java.util.List;

public class Student extends User {
    private String programOfStudy;

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
}
