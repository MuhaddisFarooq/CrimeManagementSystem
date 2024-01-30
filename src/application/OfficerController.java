package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class OfficerController {

    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("login_registration.fxml")); // Replace with your login FXML file path
            Scene loginScene = new Scene(loginRoot);
            Stage primaryStage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            primaryStage.setScene(loginScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @FXML
    private void handleManageComplaints(ActionEvent event)
    {
    	
    	try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("Approve.fxml")); 
            Scene loginScene = new Scene(loginRoot);
            Stage primaryStage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            primaryStage.setScene(loginScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	
    }
}
