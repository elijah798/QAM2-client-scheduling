package models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Creates an appointment object to store appointment data from the database for use in program
 */
public class appointments {
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private int contact;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private int customerID;
    private int userID;

    /**
     *
     * @param appointmentID
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param userID
     */
    public appointments(int appointmentID, String title, String description, String location, int contact, String type, Timestamp start, Timestamp end, int customerID, int userID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
    }


    /**
     *
     * @return
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     *
     * @param appointmentID
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return
     */
    public int getContact() {
        return contact;
    }

    /**
     *
     * @param contact
     */
    public void setContact(int contact) {
        this.contact = contact;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     *
     * @param start
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     *
     * @return
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     *
     * @param end
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     *
     * @return
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     *
     * @param customerID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     *
     * @return
     */
    public int getUserID() {
        return userID;
    }

    /**
     *
     * @param userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
}
