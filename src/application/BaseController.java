package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class BaseController {

	
	
	
    protected void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    protected void navigateTo(String fxmlPath, Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Error navigating to " + fxmlPath, Alert.AlertType.ERROR);
        }
    }
    

        protected void switchScene(String fxmlFile, ActionEvent event) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error (show error dialog, log error, etc.)
            }
        }
    
        protected void goBack(ActionEvent event, String fxmlFile) {
            switchScene(fxmlFile, event);
        }

        protected void logout(ActionEvent event) {
            switchScene("login_registration.fxml", event);
        }
    
}
