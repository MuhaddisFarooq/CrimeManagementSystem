package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start {

    @FXML
    private Button startButton;

    @FXML
    void startButtonAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login_registration.fxml"));
            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
