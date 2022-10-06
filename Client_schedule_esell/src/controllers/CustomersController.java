package controllers;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import models.customers;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomersController {
    public TableColumn customerID;
    public TableColumn customerName;
    public TableColumn address;
    public TableColumn postal;
    public TableColumn phone;

    public TableColumn divisionID;
    public TableColumn createdDate;
    public TableColumn createdBy;
    public TableColumn updatedDate;
    public TableColumn updatedBy;
    public TableView customerTable;
    public Button Add;
    public Button modify;

    private int user_ID = MenuContoller.getUserID();

    /**
     *
     * @throws SQLException
     */
    public void initialize() throws SQLException {

        populateTable();

    }

    /**
     *
     * @throws SQLException
     */
    public void populateTable () throws SQLException {
        JDBC.openConnection();
        Connection connection = JDBC.connection;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM client_schedule.customers");

        ObservableList<customers> customers = FXCollections.observableArrayList();
        while (resultSet.next()) {
            customers.add(new customers(resultSet.getInt("Customer_ID"), resultSet.getString("Customer_Name"), resultSet.getString("Address"), resultSet.getString("Postal_Code"), resultSet.getString("Phone"), resultSet.getInt("Division_ID"), resultSet.getString("Create_Date"), resultSet.getString("Created_By"), resultSet.getString("Last_Update"), resultSet.getString("Last_Updated_By")));
        }

        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        postal.setCellValueFactory(new PropertyValueFactory<>("postal"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        divisionID.setCellValueFactory(new PropertyValueFactory<>("division"));
        createdDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        createdBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        updatedDate.setCellValueFactory(new PropertyValueFactory<>("updatedDate"));
        updatedBy.setCellValueFactory(new PropertyValueFactory<>("updatedBy"));
        customerTable.setItems(customers);
    }


    /**
     *
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void handleAdd(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/AddCustomer.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        populateTable();
    }

    /**
     *
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void handleModify(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/ModifyCustomer.fxml"));
        Parent root = loader.load();
        ModifyCustomerController modifyCustomerController = loader.getController();
        modifyCustomerController.setCustomer((customers) customerTable.getSelectionModel().getSelectedItem());


        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        populateTable();
    }

    /**
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void handleDelete(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Are you sure you want to delete this customer??");
        alert.setHeaderText(null);
        alert.setContentText("Please confirm you want to delete this customer.");
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")) {
            customers customer = (customers) customerTable.getSelectionModel().getSelectedItem();
            JDBC.openConnection();
            Connection connection = JDBC.connection;
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM client_schedule.customers WHERE Customer_ID = " + customer.getCustomerID());
            populateTable();
        }

    }

}
