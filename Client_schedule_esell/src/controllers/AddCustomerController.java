package controllers;

import helper.JDBC;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.first_level_divisions;
import models.countries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddCustomerController {
 public TextField customerName;
 public TextField address;

 public TextField postalCode;
 public TextField phone;


 public int user_ID = MenuContoller.getUserID();
    public ChoiceBox divisionLevel;
    public ChoiceBox countryChoice;

    /**
     *
     */
    public void initialize() {
        try {
            JDBC.openConnection();
            Connection connection = JDBC.connection;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM client_schedule.countries");
            while (resultSet.next()) {
                countryChoice.getItems().add(resultSet.getString("Country"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void handleSave(ActionEvent actionEvent) throws SQLException {
        JDBC.openConnection();
        Connection connection = JDBC.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM client_schedule.first_level_divisions WHERE Division = '" + divisionLevel.getValue() + "'");
        resultSet.next();
        int divisionID = resultSet.getInt("Division_ID");
        statement.executeUpdate("INSERT INTO client_schedule.customers (Customer_Name, Address, Postal_Code, Phone, Division_ID, Created_By, Last_Updated_By) VALUES ('" + customerName.getText() + "', '" + address.getText() + "', '" + postalCode.getText() + "', '" + phone.getText() + "', '" + divisionID + "', '" + user_ID + "', '" + user_ID + "')");

        Stage stage = (Stage) customerName.getScene().getWindow();
        stage.close();
    }

    /**
     *
     * @param actionEvent
     */
    public void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) customerName.getScene().getWindow();
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

    }



