package com.uottawa.seg.group12otams;

public class Administrator extends User {
    public Administrator(String firstName, String lastName, String email, String password, String phoneNumber) {
        super(firstName, lastName, email, password, phoneNumber);
    }

    @Override
    public String getRole() {
        return "Administrator";
    }
}
