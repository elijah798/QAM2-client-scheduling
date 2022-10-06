package controllers;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.appointments;
import models.contacts;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import models.types;
import models.userCount;


/**
 * FXML Controller class
 *
 * @author
 */

public class ReportsContoller {



    public ObservableList<appointments> allAppointments = FXCollections.observableArrayList();
    public ObservableList<contacts> allContacts = FXCollections.observableArrayList();
    public ComboBox ContactBox;
    public TableView reportTable;
    /**
     * Initializes the controller class.
     * @throws SQLException
     */
    public void initialize() throws SQLException {
        getAllAppointments();
        getContacts();
        populateContactBox();
    }

    /**
     *
     * @throws SQLException
     */
    public void populateContactBox() throws SQLException {
        for (contacts contact : allContacts) {
            ContactBox.getItems().add(contact.getContactName());
        }
    }

    /**
     *
     * @throws SQLException
     */
    public void getAllAppointments() throws SQLException {
        String query = "SELECT * FROM client_schedule.appointments";
        JDBC.openConnection();
        Connection connection = JDBC.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            allAppointments.add(new appointments(
                    resultSet.getInt("Appointment_ID"),
                    resultSet.getString("Title"),
                    resultSet.getString("Description"),
                    resultSet.getString("Location"),
                    resultSet.getInt("Contact_ID"),
                    resultSet.getString("Type"),
                    resultSet.getTimestamp("Start"),
                    resultSet.getTimestamp("End"),
                    resultSet.getInt("Customer_ID"),
                    resultSet.getInt("User_ID")
            ));
        }
    }


    /**
     *
     * @throws SQLException
     */
    public void getContacts() throws SQLException {
        String query = "SELECT * FROM client_schedule.contacts";
        JDBC.openConnection();
        Connection connection = JDBC.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            allContacts.add(new contacts(
                    resultSet.getInt("Contact_ID"),
                    resultSet.getString("Contact_Name"),
                    resultSet.getString("Email")
            ));
        }
    }

    /**
     *
     * @param actionEvent
     */
    public void handleContactSchedule(ActionEvent actionEvent) {
        reportTable.getColumns().clear();
        TableColumn<appointments, String> appointmentID = new TableColumn<>("Appointment_ID");
TableColumn<appointments, String> title = new TableColumn<>("Title");
TableColumn<appointments, String> description = new TableColumn<>("Description");
TableColumn<appointments, String> location = new TableColumn<>("Location");
TableColumn<appointments, String> type = new TableColumn<>("Type");
TableColumn<appointments, String> start = new TableColumn<>("Start");
TableColumn<appointments, String> end = new TableColumn<>("End");
TableColumn<appointments, String> customerID = new TableColumn<>("Customer_ID");
TableColumn<appointments, String> userID = new TableColumn<>("User_ID");

        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        description.setCellValueFactory(new PropertyValueFactory<>("Description"));
        location.setCellValueFactory(new PropertyValueFactory<>("Location"));
        type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        start.setCellValueFactory(new PropertyValueFactory<>("Start"));
        end.setCellValueFactory(new PropertyValueFactory<>("End"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        title.setCellValueFactory(new PropertyValueFactory<>("Title"));

        reportTable.getColumns().addAll(title, description, location, type, start, end, customerID, userID);
        reportTable.getItems().clear();
        for (appointments appointment : allAppointments) {
            if (appointment.getContact() == ContactBox.getSelectionModel().getSelectedIndex() + 1) {
                reportTable.getItems().add(appointment);
            }
        }

    }

    /**
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void handleAppointmentsByTypeMonth(ActionEvent actionEvent) throws SQLException {
        reportTable.getColumns().clear();
        TableColumn<types, String> type = new TableColumn<>("Type");
        TableColumn<types, String> Month = new TableColumn<>("Month");
        TableColumn<types, String> count = new TableColumn<>("Count");

        type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        Month.setCellValueFactory(new PropertyValueFactory<>("Month"));
        count.setCellValueFactory(new PropertyValueFactory<>("Count"));

        reportTable.getColumns().addAll(type, Month, count);

        reportTable.getItems().clear();

        ObservableList<types> allTypes = FXCollections.observableArrayList();

        String query = "SELECT Type, MONTHNAME(Start) AS Month, COUNT(*) AS Count FROM client_schedule.appointments GROUP BY Type, MONTHNAME(Start)";
        JDBC.openConnection();
        Connection connection = JDBC.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            allTypes.add(new types(
                    resultSet.getString("Type"),
                    resultSet.getString("Month"),
                    resultSet.getInt("Count")
            ));
        }
        reportTable.getItems().addAll(allTypes);
    }

    /**
     *
     * @param actionEvent
     * @throws SQLException
     */

    public void handleQuantityofAppointments(ActionEvent actionEvent) throws SQLException {
        reportTable.getColumns().clear();
        TableColumn<userCount, String> user = new TableColumn<>("User");
        TableColumn<userCount, String> count = new TableColumn<>("Count");


        //get user and appointment count
        String query = "SELECT User_Name, COUNT(*) AS Count FROM client_schedule.appointments INNER JOIN client_schedule.users ON appointments.User_ID = users.User_ID GROUP BY User_Name";
        JDBC.openConnection();
        Connection connection = JDBC.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            reportTable.getItems().add(new userCount(
                    resultSet.getString("User_Name"),
                    resultSet.getInt("Count")
            ));
        }

        user.setCellValueFactory(new PropertyValueFactory<>("User"));
        count.setCellValueFactory(new PropertyValueFactory<>("Count"));

        reportTable.getColumns().addAll(user, count);






    }

}
