package com.uottawa.seg.group12otams;

public abstract class User {
    protected String firstName;
    protected String lastName;
    // NOTE: email is a unique key only associated to a single User
    protected String email;
    protected String password;
    protected String phoneNumber;

    public User(String firstName, String lastName, String email, String password, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public User() {}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public void logoff() {
        System.out.println("You have successfully logged off.");
    }

    public abstract String getRole();

    public void displayWelcomeMessage() {
        System.out.println("Welcome! You are logged in as " + getRole() + ".");
    }

    public abstract void update(User user);
}
