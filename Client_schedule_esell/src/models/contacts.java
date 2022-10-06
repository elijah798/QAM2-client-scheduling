package models;

/**
 *
 */

public class contacts {
    private int contactID;
    private String contactName;
    private String email;

    /**
     *
     * @param contactID
     * @param contactName
     * @param email
     */
    public contacts(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    /**
     *
     * @return
     */
    public int getContactID() {
        return contactID;
    }

    /**
     *
     * @param contactID
     */

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     *
     * @return
     */
    public String getContactName() {
        return contactName;
    }

    /**
     *
     * @param contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
