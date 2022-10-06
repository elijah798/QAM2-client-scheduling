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
 *
 */
public class AddAppointmentController {



    public TextField title;
    public TextField description;
    public TextField applocation;
    public TextField type;
    public ChoiceBox StartTime;
    public DatePicker startDate;
    public DatePicker endDate;
    public ChoiceBox EndTime;
    public TextField CustomerID;
    public TextField UserID;

    public ObservableList<appointments> appointments = FXCollections.observableArrayList();

    public ChoiceBox contactbox;

    /**
     *
     * @throws SQLException
     */
    public void initialize() throws SQLException {
        appointments.clear();
        appointments = getAllAppointments();
        System.out.println(appointments.get(0).getStart());
        contactbox.getItems().clear();
        for(int i =0; i< getAllContacts().size(); i++){
            contactbox.getItems().add(getAllContacts().get(i).getContactName());
        }





}


    /**
     *
     * @return
     * @throws SQLException
     */
    public ObservableList<appointments> getAllAppointments() throws SQLException {

    JDBC.openConnection();
    Connection connection = JDBC.connection;
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM client_schedule.appointments");
    ObservableList<appointments> appointments = FXCollections.observableArrayList();
    while (resultSet.next()) {
        ZoneId zoneId = ZoneId.systemDefault();
        appointments.add(new appointments(resultSet.getInt("Appointment_ID"), resultSet.getString("Title"), resultSet.getString("Description"), resultSet.getString("Location"), resultSet.getInt("Contact_ID"), resultSet.getString("Type"), resultSet.getTimestamp("Start"), resultSet.getTimestamp("End"), resultSet.getInt("Customer_ID"), resultSet.getInt("User_ID")));
    }
    return appointments;

}

    /**
     *
     * @return
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
     *
     * @param contactName
     * @return
     * @throws SQLException
     */
    public String getContactID(String contactName) throws SQLException {
        String contactID = "";
        for(int i =0; i< getAllContacts().size(); i++){
            if(getAllContacts().get(i).getContactName().equals(contactName)){
                contactID = String.valueOf(getAllContacts().get(i).getContactID());
            }
        }
        return contactID;
    }


    /**
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void handleSave(ActionEvent actionEvent) throws SQLException {

        String title = this.title.getText();
        String description = this.description.getText();
        String location = this.applocation.getText();
        String type = this.type.getText();
        String contact = getContactID(contactbox.getValue().toString());


        String start = startDate.getValue().toString() + " " + StartTime.getValue().toString();
        LocalTime localStartTime = LocalTime.parse(StartTime.getValue().toString());
        LocalDateTime localDateTime = LocalDateTime.of(startDate.getValue(), localStartTime);
        Timestamp startTimestamp = Timestamp.valueOf(localDateTime);

        LocalTime localEndTime = LocalTime.parse(EndTime.getValue().toString());
        localDateTime = LocalDateTime.of(endDate.getValue(), localEndTime);
        Timestamp endTimestamp = Timestamp.valueOf(localDateTime);





        String customerID = this.CustomerID.getText();
        String userID = this.UserID.getText();

        appointments appointment = new appointments(0, title, description, location, Integer.parseInt(contact), type, startTimestamp, endTimestamp, 0, 0);

        if (ValidateAppointment(appointment)){
            String sql = "INSERT INTO client_schedule.appointments (Title, Description, Location, Contact_ID, Type, Start, End, Customer_ID, User_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            JDBC.openConnection();
            Connection connection = JDBC.connection;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, location);
            statement.setString(4, contact);
            statement.setString(5, type);
            statement.setTimestamp(6, startTimestamp);
            statement.setTimestamp(7, endTimestamp);
            statement.setString(8, customerID);
            statement.setString(9, userID);

            statement.executeUpdate();
            Stage stage = (Stage) this.title.getScene().getWindow();
            stage.close();
        }


    }


    /**
     *
     * @param actionEvent
     */
    public void handleCancel(ActionEvent actionEvent) {

        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();
    }

    /**
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void handleStartTime(ActionEvent actionEvent) throws SQLException {


        StartTime.disableProperty().setValue(false);
        ObservableList<LocalDateTime> times = FXCollections.observableArrayList();
        LocalDate start = startDate.getValue();
        for(int i = 8; i <= 22; i++) {
            times.add(LocalDateTime.of(start, LocalTime.of(i, 0)));
            times.add(LocalDateTime.of(start, LocalTime.of(i, 15)));
            times.add(LocalDateTime.of(start, LocalTime.of(i, 30)));
            times.add(LocalDateTime.of(start, LocalTime.of(i, 45)));

        }
        for(int i = 0; i < times.size(); i++){

                StartTime.getItems().add(times.get(i).atZone(ZoneId.of("US/Eastern")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        }
        StartTime.getSelectionModel().selectFirst();


        }

    /**
     *
     * @param actionEvent
     */
    public void handleEndTime(ActionEvent actionEvent) {
        EndTime.disableProperty().setValue(false);
        ObservableList<LocalDateTime> times = FXCollections.observableArrayList();
        LocalDate end = endDate.getValue();
        for(int i = 8; i <= 22; i++) {
            times.add(LocalDateTime.of(end, LocalTime.of(i, 0)));
            times.add(LocalDateTime.of(end, LocalTime.of(i, 15)));
            times.add(LocalDateTime.of(end, LocalTime.of(i, 30)));
            times.add(LocalDateTime.of(end, LocalTime.of(i, 45)));

        }
        for(int i = 0; i < times.size(); i++){

            EndTime.getItems().add(times.get(i).atZone(ZoneId.of("US/Eastern")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        }
        EndTime.getSelectionModel().selectFirst();


    }

    /**
     *
     * @param appointment
     * @return
     * @throws SQLException
     */
    public boolean ValidateAppointment(appointments appointment) throws SQLException {
        AtomicBoolean valid = new AtomicBoolean(true);

        try {
            String sql = "SELECT * FROM client_schedule.appointments WHERE Customer_ID = ?";
            JDBC.openConnection();
            Connection connection = JDBC.connection;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, CustomerID.getText());
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



}
