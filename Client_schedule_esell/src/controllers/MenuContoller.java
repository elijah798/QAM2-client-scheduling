package controllers;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.appointments;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author
 *
 */
public class MenuContoller {
    private static int userID;

    /**
     *
     * @param user_ID
     * @throws SQLException
     */
    public void setUserID(int user_ID) throws SQLException {
        this.userID = user_ID;
        ObservableList<appointments> AllAppointments = FXCollections.observableArrayList();

        String sql = "SELECT * FROM client_schedule.appointments WHERE user_ID = " + userID;
        JDBC.openConnection();
        Connection connection = JDBC.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            AllAppointments.add(new appointments(
                    resultSet.getInt("appointment_ID"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("location"),
                    resultSet.getInt("contact_ID"),
                    resultSet.getString("type"),
                    resultSet.getTimestamp("start"),
                    resultSet.getTimestamp("end"),
                    resultSet.getInt("customer_ID"),
                    resultSet.getInt("user_ID")
            ));
        }
        AllAppointments.forEach(appointments -> {
            if (appointments.getStart().toLocalDateTime().isBefore(LocalDateTime.now().plusMinutes(15)) && appointments.getStart().toLocalDateTime().isAfter(LocalDateTime.now())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment Alert");
                alert.setHeaderText("Appointment Soon");
                alert.setContentText("Appointment "+ appointments.getAppointmentID() + " starts soon at: " + appointments.getStart().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                alert.showAndWait();
                System.out.println("Appointment " + appointments.getAppointmentID() + " has already passed");
            }
        });

    }
    /**
     *
     * @return
     */
    public static int getUserID() {
        return userID;
    }

    /**
     *
     * @param actionEvent
     * @throws IOException
     */
    public void handleCustomers(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/customers.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     *
     * @param actionEvent
     * @throws IOException
     */
    public void handleAppointments(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/scheduling.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

    }

    /**
     *
     * @param actionEvent
     * @throws IOException
     */
    public void handleReports(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/reports.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();


    }
}
