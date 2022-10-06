package models;

import helper.JDBC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 */
public class customers {
    private int customerID;
    private String customerName;
    private String address;
    private String postal;
    private String phone;
    private int divisionID;
    private String division;
    private String createdDate;
    private String createdBy;
    private String updatedDate;
    private String updatedBy;

    /**
     *
     * @param customerID
     * @param customerName
     * @param address
     * @param postal
     * @param phone
     * @param divisionID
     * @param createdDate
     * @param createdBy
     * @param updatedDate
     * @param updatedBy
     */
    public customers(int customerID, String customerName, String address, String postal, String phone, int divisionID, String createdDate, String createdBy, String updatedDate, String updatedBy) throws SQLException {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.divisionID = divisionID;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.updatedDate = updatedDate;
        this.updatedBy = updatedBy;
        setDivision();
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
    public String getCustomerName() {
        return customerName;
    }

    /**
     *
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     */
    public String getPostal() {
        return postal;
    }

    /**
     *
     * @param postal
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }

    /**
     *
     * @return
     */
    public String getDivision() {
        return division;
    }

    /**
     *
     *
     */
    private void setDivision() throws SQLException {
        JDBC.openConnection();
        Connection connection = JDBC.connection;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM client_schedule.first_level_divisions WHERE Division_ID = '" + this.divisionID + "'");
        resultSet.next();
        first_level_divisions division = new first_level_divisions(resultSet.getInt("Division_ID"), resultSet.getString("Division"), resultSet.getInt("COUNTRY_ID"));

       this.division = division.getDivision();
    }



    /**
     *
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     * @return
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     *
     * @param divisionID
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     *
     * @return
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     *
     * @param createdDate
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     *
     * @return
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return
     */
    public String getUpdatedDate() {
        return updatedDate;
    }

    /**
     *
     * @param updatedDate
     */
    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     *
     * @return
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     *
     * @param updatedBy
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

}
