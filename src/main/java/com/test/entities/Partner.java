package com.test.entities;

import java.util.Date;

public class Partner {
    private String firstName, lastName, email, country;
    private Date[] availableDates;

    public Partner() { }

    public Partner(String firstName, String lastName, String email, String country, Date[] availableDates) {
        this();
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setCountry(country);
        setAvailableDates(availableDates);
    }

    public String getFirstName() {
        return firstName;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    private void setCountry(String country) {
        this.country = country;
    }

    public Date[] getAvailableDates() {
        return availableDates;
    }

    private void setAvailableDates(Date[] availableDates) {
        this.availableDates = availableDates;
    }
}
