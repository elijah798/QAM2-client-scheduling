package controllers;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.appointments;
import models.contacts;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * FXML Controller class
 *
 * @author
 */
public class UpdateAppointmentController {

    public TextField appointmentID;
    public TextField title;
    public TextField description;
    public TextField applocation;
    public ChoiceBox contactbox;
    public TextField type;
    public DatePicker startDate;
    public ChoiceBox StartTime;
    public DatePicker endDate;
    public ChoiceBox EndTime;
    public TextField CustomerID;
    public TextField UserID;
    private appointments appointment;

    public ObservableList<appointments> AllAppointments = FXCollections.observableArrayList();

    /**
     * @param allAppointments
     */
    public void setAllAppointments(ObservableList<appointments> allAppointments) {
        AllAppointments = allAppointments;
    }

    /**
     * @param appointment
     */
    public void setAppointment(appointments appointment) throws SQLException {
        this.appointment = appointment;
        System.out.println(appointment.getAppointmentID());
        populateFields();
        addTime();
    }
    /**
     * Initializes the controller class.
     * @throws SQLException
     */
    public void initialize() throws SQLException {
        contactbox.getItems().clear();
        for(int i =0; i< getAllContacts().size(); i++){
            contactbox.getItems().add(getAllContacts().get(i).getContactName());
        }

    }

    /**
     * @throws SQLException
     */
    public ObservableList<contacts> getAllContacts() throws SQLException {
        JDBC.openConnection();
        Connection connection = JDBC.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM client_schedule.contacts");
        ObservableList<contacts> contacts = FXCollections.observableArrayList();
        while (resultSet.next()) {
            contacts.add(new contacts(resultSet.getInt("Contact_ID"), resultSet.getString("Contact_Name"), resultSet.getString("Email")));
        }
        return contacts;

    }

    /**
     * @throws SQLException
     */
    public int getContactID(String contactName) throws SQLException {
        String contactID = "";
        for(int i =0; i< getAllContacts().size(); i++){
            if(getAllContacts().get(i).getContactName().equals(contactName)){
                contactID = String.valueOf(getAllContacts().get(i).getContactID());
            }
        }
        return Integer.parseInt(contactID);
    }

    /**
     * @throws SQLException
     */
    public void populateFields() throws SQLException {
        appointmentID.setText(String.valueOf(appointment.getAppointmentID()));
        title.setText(appointment.getTitle());
        description.setText(appointment.getDescription());
        applocation.setText(appointment.getLocation());
        for(int i =0; i< getAllContacts().size(); i++){
            if(getAllContacts().get(i).getContactID() == appointment.getContact()){
                contactbox.getSelectionModel().select(getAllContacts().get(i).getContactName());
            }
        }

        type.setText(appointment.getType());
        startDate.setValue(appointment.getStart().toLocalDateTime().toLocalDate());
        StartTime.setValue(appointment.getStart().toLocalDateTime().toLocalTime());
        endDate.setValue(appointment.getEnd().toLocalDateTime().toLocalDate());
        EndTime.setValue(appointment.getEnd().toLocalDateTime().toLocalTime());
        CustomerID.setText(String.valueOf(appointment.getCustomerID()));
        UserID.setText(String.valueOf(appointment.getUserID()));
    }

    /**
     * @throws SQLException
     */
    public void handleUpdate(ActionEvent actionEvent) throws SQLException {

        String title = this.title.getText();
        String description = this.description.getText();
        String location = this.applocation.getText();
        String type = this.type.getText();
        int contact = getContactID(contactbox.getValue().toString());


        LocalTime localStartTime = LocalTime.parse(StartTime.getValue().toString());
        LocalDateTime localDateTime = LocalDateTime.of(startDate.getValue(), localStartTime);
        Timestamp startTimestamp = Timestamp.valueOf(localDateTime);

        LocalTime localEndTime = LocalTime.parse(EndTime.getValue().toString());
        localDateTime = LocalDateTime.of(endDate.getValue(), localEndTime);
        Timestamp endTimestamp = Timestamp.valueOf(localDateTime);

        String customerID = this.CustomerID.getText();
        String userID = this.UserID.getText();



        appointments appointment = new appointments(0, title, description, location, contact, type, startTimestamp, endTimestamp, 0, 0);

        if (ValidateAppointment(appointment)){
            String sql = "UPDATE client_schedule.appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            JDBC.openConnection();
            Connection connection = JDBC.connection;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, location);
            statement.setString(4, type);
            statement.setTimestamp(5, startTimestamp);
            statement.setTimestamp(6, endTimestamp);
            statement.setString(7, customerID);
            statement.setString(8, userID);
            statement.setString(9, String.valueOf(contact));
            statement.setString(10, appointmentID.getText());
            statement.executeUpdate();
            Stage stage = (Stage) this.title.getScene().getWindow();
            stage.close();
        }





    }

    /**
     *
     */
    public void addTime(){
        ObservableList<LocalDateTime> starttimes = FXCollections.observableArrayList();
        ObservableList<LocalDateTime> endtimes = FXCollections.observableArrayList();

        LocalDateTime start = appointment.getStart().toLocalDateTime();
        LocalDateTime end = appointment.getEnd().toLocalDateTime();

        LocalDate startdate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();

        for(int i = 8; i <= 22; i++) {
            for (int k = 0; k < 60; k += 15) {
                starttimes.add(LocalDateTime.of(startdate, LocalTime.of(i, k)));
                endtimes.add(LocalDateTime.of(endDate, LocalTime.of(i, k)));
            }
        }

        for(int i = 0; i < starttimes.size(); i++){

            StartTime.getItems().add(starttimes.get(i).atZone(ZoneId.of("US/Eastern")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        }

        for(int i = 0; i < endtimes.size(); i++){
            EndTime.getItems().add(endtimes.get(i).atZone(ZoneId.of("US/Eastern")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        }

    }

    /**
     *
     */
    public boolean ValidateAppointment(appointments appointment){
           AtomicBoolean valid = new AtomicBoolean(true);



            try {
                String sql = "SELECT * FROM client_schedule.appointments WHERE Customer_ID = ? and appointment_ID != ?";
                JDBC.openConnection();
                Connection connection = JDBC.connection;
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, CustomerID.getText());
                statement.setString(2, appointmentID.getText());
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Timestamp start = resultSet.getTimestamp("Start");
                    Timestamp end = resultSet.getTimestamp("End");
                    if (appointment.getStart().before(end) && appointment.getEnd().after(start)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Appointment");
                        alert.setContentText("Start and End times overlap with another appointment. Please Change times and try again.");
                        alert.showAndWait();
                        valid.set(false);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            switch (appointment.getStart().toLocalDateTime().getDayOfWeek()){
                case MONDAY:
                case TUESDAY:
                case WEDNESDAY:
                case THURSDAY:
                case FRIDAY:
                    System.out.println(appointment.getStart().toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("US/Eastern")).getHour());
                    if(appointment.getStart().toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("US/Eastern")).getHour() < 8 || appointment.getEnd().toLocalDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("US/Eastern")).getHour() > 22){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Appointment");
                        alert.setContentText("Start and End Times are outside business hours: 8:00 AM - 10:00 PM EST, Weekdays Only");
                        alert.showAndWait();
                        valid.set(false);
                    }
                    break;
                case SATURDAY:
                case SUNDAY:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Appointment");
                    alert.setContentText("Start and End Times are outside business hours: 8:00 AM - 10:00 PM EST, Weekdays Only");
                    alert.showAndWait();
                    valid.set(false);
                    break;
            }
        return valid.get();
    }






    /**
     * @param actionEvent
     */
    public void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
