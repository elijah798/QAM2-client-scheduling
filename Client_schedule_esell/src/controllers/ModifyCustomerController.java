package controllers;
import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.countries;
import models.customers;
import models.first_level_divisions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 */
public class ModifyCustomerController {
    public customers customer;
    public TextField customerID;
    public TextField customerName;
    public TextField address;
    public ChoiceBox countryChoice;
    public ChoiceBox divisionLevel;
    public TextField postalCode;
    public TextField phone;

    /**
     *
     * @param customer
     * @throws SQLException
     */
    public void setCustomer(customers customer) throws SQLException {
        this.customer = customer;
        System.out.println(customer.getCustomerName());
        populateFields();
    }

    /**
     *
     * @throws SQLException
     */
    public void populateFields() throws SQLException {
        customerID.setText(String.valueOf(customer.getCustomerID()));
        customerName.setText(customer.getCustomerName());
        address.setText(customer.getAddress());
        postalCode.setText(customer.getPostal());
        phone.setText(customer.getPhone());

        countryChoice.getItems().clear();
        divisionLevel.getItems().clear();
        addCountriesandDivisions();
        countryChoice.getSelectionModel().select(getCountry().getCountry());
        divisionLevel.getSelectionModel().select(getDivision().getDivision());



    }

    /**
     *
     * @throws SQLException
     */
    public void addCountriesandDivisions() throws SQLException {
        JDBC.openConnection();
        Connection connection = JDBC.connection;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM client_schedule.countries");
        while (resultSet.next()) {
            countryChoice.getItems().add(resultSet.getString("Country"));
        }

        ResultSet resultSet1 = statement.executeQuery("SELECT * FROM client_schedule.first_level_divisions WHERE COUNTRY_ID = " + getCountry().getCountry_ID());
        while (resultSet1.next()) {
            divisionLevel.getItems().add(resultSet1.getString("Division"));
        }

    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public countries getCountry() throws SQLException {
        JDBC.openConnection();
        Connection connection = JDBC.connection;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM client_schedule.first_level_divisions WHERE Division_ID = '" + customer.getDivisionID() + "'");
        resultSet.next();
        int countryID = resultSet.getInt("COUNTRY_ID");

        ResultSet resultSet2 = statement.executeQuery("SELECT * FROM client_schedule.countries WHERE Country_ID = '" + countryID + "'");
        resultSet2.next();
        countries country = new countries(resultSet2.getInt("Country_ID"), resultSet2.getString("Country"));

        return country;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public first_level_divisions getDivision() throws SQLException {
        JDBC.openConnection();
        Connection connection = JDBC.connection;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM client_schedule.first_level_divisions WHERE Division_ID = '" + customer.getDivisionID() + "'");
        resultSet.next();
        first_level_divisions division = new first_level_divisions(resultSet.getInt("Division_ID"), resultSet.getString("Division"), resultSet.getInt("COUNTRY_ID"));

        return division;
    }


    /**
     *
     * @param actionEvent
     */
    public void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void handleDivision(ActionEvent actionEvent) throws SQLException {
        divisionLevel.getItems().clear();
        JDBC.openConnection();
        Connection connection = JDBC.connection;
        Statement statement = connection.createStatement();
        int SelectedCountry = countryChoice.getSelectionModel().getSelectedIndex() + 1;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM client_schedule.first_level_divisions WHERE COUNTRY_ID = " + SelectedCountry);
        while (resultSet.next()) {

            divisionLevel.getItems().add(resultSet.getString("Division"));
        }
        JDBC.closeConnection();

    }

    /**
     *
     * @param actionEvent
     */
    public void handleSave(ActionEvent actionEvent) {
        JDBC.openConnection();
        Connection connection = JDBC.connection;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE client_schedule.customers SET Customer_Name = '" + customerName.getText() + "', Address = '" + address.getText() + "', Postal_Code = '" + postalCode.getText() + "', Phone = '" + phone.getText() + "', Division_ID = '" + getDivisionID() + "' WHERE Customer_ID = " + customer.getCustomerID());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        JDBC.closeConnection();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    private String getDivisionID() throws SQLException {
        JDBC.openConnection();
        Connection connection = JDBC.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM client_schedule.first_level_divisions WHERE Division = '" + divisionLevel.getSelectionModel().getSelectedItem() + "'");
        resultSet.next();
        String divisionID = resultSet.getString("Division_ID");
        return divisionID;
    }
    }
