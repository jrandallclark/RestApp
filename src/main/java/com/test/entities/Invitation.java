package com.test.entities;

import java.util.Date;

public class Invitation {
    private int attendeeCount;
    private String name;
    private String[] attendees;
    private Date startDate;

    public Invitation() { }

    public Invitation(String country, Date startDate, int attendeeCount, String[] attendees) {
        this();
        setName(country);
        setAttendees(attendees);
        setAttendeeCount(attendeeCount);
        setStartDate(startDate);
    }

    public int getAttendeeCount() {
        return attendeeCount;
    }

    private void setAttendeeCount(int attendeeCount) {
        this.attendeeCount = attendeeCount;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String[] getAttendees() {
        return attendees;
    }

    private void setAttendees(String[] attendees) {
        this.attendees = attendees;
    }

    public Date getStartDate() {
        return startDate;
    }

    private void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
