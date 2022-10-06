package controllers;
import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.Locale;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;

public class LoginController {

    public Label Region;
    public ZoneId zoneId = ZoneId.systemDefault();
    public Locale locale = Locale.getDefault();

    public Label usernameLabel;
    public TextField usernameField;
    public Label passwordLabel;
    public PasswordField passwordField;

    /**
     * Initializes the window and displays information according to locale
     */
    public void initialize() {
        if (locale.getLanguage().equals("en")) {
            Region.setText("Region: " + zoneId);
            usernameLabel.setText("Username");
            passwordLabel.setText("Password");
        } else if (locale.getLanguage().equals("fr")) {
            Region.setText("Région: " + zoneId);
            usernameLabel.setText("Nom d'utilisateur");
            passwordLabel.setText("Mot de passe");
        }
        System.out.println("Language: " + locale.getLanguage());



    }
    public void handleUsername(ActionEvent actionEvent) {
    }
    public void handlePassword(ActionEvent actionEvent) {

    }

    /**
     * Function handles Login and writes to login activity whether it was successful or not.
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void handleLogin(ActionEvent actionEvent) throws SQLException, IOException {
        JDBC.openConnection();
        Connection connection = JDBC.connection;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM client_schedule.users WHERE User_Name = '" + usernameField.getText() + "' AND Password = '" + passwordField.getText() + "'");
        if (resultSet.next()) {
            Writer output;
            output = new BufferedWriter(new FileWriter("login_activity.txt", true));
            output.append("Login attempt failed for user: " + usernameField.getText() + " at " + java.time.LocalDateTime.now() + " successful");
            output.append(System.lineSeparator());
            output.close();

            System.out.println("Login successful");
            passwordField.setText("");
            usernameField.setText("");
            passwordField.setStyle("-fx-border-color: none");
            usernameField.setStyle("-fx-border-color: none");



            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/menu.fxml"));
            Parent root = loader.load();
            MenuContoller controller = loader.getController();
            controller.setUserID(resultSet.getInt("User_ID"));

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();


        } else {
            Writer output;
            output = new BufferedWriter(new FileWriter("login_activity.txt", true));
            output.append("Login attempt failed for user: " + usernameField.getText() + " at " + java.time.LocalDateTime.now() + " unsuccessful");
            output.append(System.lineSeparator());
            output.close();
            if (locale.getLanguage().equals("en")) {
                passwordField.setStyle("-fx-border-color: red");
                usernameField.setStyle("-fx-border-color: red");
                passwordField.setPromptText("Incorrect password");
                usernameField.setPromptText("Incorrect username");
            } else if (locale.getLanguage().equals("fr")) {
                passwordField.setStyle("-fx-border-color: red");
                usernameField.setStyle("-fx-border-color: red");
                passwordField.setPromptText("Mot de passe incorrect");
                usernameField.setPromptText("Nom d'utilisateur incorrect");
            }
        }

    JDBC.closeConnection();
    }
}
