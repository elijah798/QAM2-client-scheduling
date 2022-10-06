package controllers;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.appointments;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

/**
 *
 */
public class SchedulingController {


    public TableView AppointmentTable;
    public TableColumn appointmentID;
    public TableColumn title;
    public TableColumn description;
    public TableColumn locations;
    public TableColumn contact;
    public TableColumn type;
    public TableColumn start;
    public TableColumn end;
    public TableColumn customerID;
    public TableColumn UserID;
    public RadioButton OBMonth;
    public RadioButton OBWeek;

    public ObservableList<appointments> AllAppointments = FXCollections.observableArrayList();
    public ToggleGroup Order_By;

    /**
     *
     * @throws SQLException
     */
    public void initialize() throws SQLException {
        AllAppointments = getAllAppointments();
        populateTable();
        System.out.println(AllAppointments);


    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public ObservableList<appointments> getAllAppointments() throws SQLException {
        AllAppointments.clear();
        JDBC.openConnection();
        Connection connection = JDBC.connection;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM client_schedule.appointments");

        while (resultSet.next()) {
            int appointmentID = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            int contact = resultSet.getInt("Contact_ID");
            String type = resultSet.getString("Type");
            Timestamp start = resultSet.getTimestamp("Start");
            Timestamp end = resultSet.getTimestamp("End");
            int customerID = resultSet.getInt("Customer_ID");
            int userID = resultSet.getInt("User_ID");

            AllAppointments.add(new appointments(appointmentID, title, description, location, contact, type, start, end, customerID, userID));
        }


        return AllAppointments;
    }


    /**
     * This method uses a lambda stream expression to efficiently and accurately populate the schedule table with appointments. This is used to update the appointments table when it is updated as well
     * @throws SQLException
     */
    public void populateTable() throws SQLException {
        getAllAppointments();
        AllAppointments.stream().forEach(appointments -> {
            appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            locations.setCellValueFactory(new PropertyValueFactory<>("location"));
            contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            start.setCellValueFactory(new PropertyValueFactory<>("start"));
            end.setCellValueFactory(new PropertyValueFactory<>("end"));
            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            UserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
            AppointmentTable.setItems(AllAppointments);
        });
    }

    /**
     *
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void AddAppoint(ActionEvent actionEvent) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/addAppointment.fxml"));
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
     * @throws SQLException
     */
    public void DeleteAppoint(ActionEvent actionEvent) throws SQLException {
        System.out.println(AppointmentTable.getSelectionModel().getSelectedItem());
        if (AppointmentTable.getSelectionModel().getSelectedItem() != null) {
            appointments appointment = (appointments) AppointmentTable.getSelectionModel().getSelectedItem();
            JDBC.openConnection();
            Connection connection = JDBC.connection;
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM client_schedule.appointments WHERE Appointment_ID = " + appointment.getAppointmentID());
            populateTable();
        }

    }

    /**
     *
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void UpdateAppoint(ActionEvent actionEvent) throws IOException, SQLException {
        if (AppointmentTable.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/updateAppoint.fxml"));
            Parent root = loader.load();
            UpdateAppointmentController updateAppointmentController = loader.getController();
            updateAppointmentController.setAppointment((appointments) AppointmentTable.getSelectionModel().getSelectedItem());
            updateAppointmentController.setAllAppointments(AllAppointments);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            populateTable();
        }
    }

    /**
     * Lambda expression is used to iterate through the all the appointments to find the appointments from the current month. The for each expression is needed to access the date and use the is after and before functions of each appointment.
     * @param actionEvent
     */
    //Lambda expression to filter appointments by month
    public void OBMonth(ActionEvent actionEvent) {
        ObservableList<appointments> MonthAppointments = FXCollections.observableArrayList();
        LocalDateTime CurrentMonth = LocalDateTime.now().minusMonths(1);
        LocalDateTime NextMonth = LocalDateTime.now().plusMonths(1);


        AllAppointments.forEach(appointment -> {
            if (appointment.getStart().toLocalDateTime().isAfter(CurrentMonth) && appointment.getStart().toLocalDateTime().isBefore(NextMonth)) {
                MonthAppointments.add(appointment);
            }
        });

        AppointmentTable.setItems(MonthAppointments);
    }

    /**
     *
     * @param actionEvent
     */
    public void OBWeek(ActionEvent actionEvent) {
        ObservableList<appointments> WeekAppointments = FXCollections.observableArrayList();
        System.out.println(AllAppointments);
        LocalDateTime CurrentWeek = LocalDateTime.now().minusWeeks(1);
        LocalDateTime NextWeek = LocalDateTime.now().plusWeeks(1);


        AllAppointments.forEach(appointment -> {
            if (appointment.getStart().toLocalDateTime().isAfter(CurrentWeek) && appointment.getStart().toLocalDateTime().isBefore(NextWeek)) {
                WeekAppointments.add(appointment);
            }
        });


        AppointmentTable.setItems(WeekAppointments);
    }

    /**
     *
     * @param actionEvent
     */
    public void OBAll(ActionEvent actionEvent) {
        AppointmentTable.setItems(AllAppointments);
    }



}
