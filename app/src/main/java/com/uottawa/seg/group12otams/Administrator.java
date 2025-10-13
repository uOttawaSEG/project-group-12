package com.uottawa.seg.group12otams;

public class Administrator extends User {
    public Administrator(String firstName, String lastName, String email, String password, String phoneNumber) {
        super(firstName, lastName, email, password, phoneNumber);
    }

    public Administrator() {}

    @Override
    public String getRole() {
        return "Administrator";
    }

    @Override
    public void update(User user) {
        Administrator administrator = (Administrator) user;
        this.firstName = administrator.firstName;
        this.lastName = administrator.lastName;
        this.email = administrator.email;
        this.password = administrator.password;
        this.phoneNumber = administrator.phoneNumber;
    }
}
