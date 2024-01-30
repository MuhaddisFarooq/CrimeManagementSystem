package application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Inheritance: TrackController is a subclass of BaseController.

public class TrackController extends BaseController{
	 
	 @FXML private TextField cnicTextField;
	 @FXML
	    private Button submitButton;
	    
	    @FXML
	    private Button backButton;

	    @FXML private Button addTestimonyButton;

	 @FXML
	    // Encapsulation: Private fields for database connection details.

	    private Label statusLabel; 
	    final String dbUrl = "jdbc:mysql://localhost:3306/cms";
	    final String dbUsername = "root";
	    final String dbPassword = "12345";

	    @FXML
	    private void handleSubmitAction() {
	        String cnic = cnicTextField.getText();
	        
	        if (!isValidCnic(cnic)) {
	            showAlert("Validation Error", "Invalid CNIC format. CNIC should be 13 digits without dashes.", AlertType.ERROR);
	            return;
	        }
	        String sql = "SELECT status FROM complaints WHERE cnic_no = ?";
	        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            pstmt.setString(1, cnic);
	            try (ResultSet rs = pstmt.executeQuery()) {
	                if (rs.next()) {
	                    String status = rs.getString("status");
	                    statusLabel.setText("Status: " + (status != null ? status : "Pending"));
	                } else {
	                    statusLabel.setText("No complaint found for the given CNIC.");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            statusLabel.setText("Error checking status.");
	        }
	    }

	    private boolean isValidCnic(String cnic) {
	        return cnic != null && cnic.matches("\\d{13}");
	    }
	    
	    
	    
	    @FXML
	    private void handleAddTestimonyAction(ActionEvent event) {
	        // Factory Method: Using FXMLLoader to create new scenes.

	    	try {
	            Parent testimonyRoot = FXMLLoader.load(getClass().getResource("Add_testimony.fxml")); // Replace with the correct file path
	            Scene testimonyScene = new Scene(testimonyRoot);
	            Stage primaryStage = (Stage) ((Button)event.getSource()).getScene().getWindow();
	            primaryStage.setScene(testimonyScene);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public void refreshButtonState() {
	        addTestimonyButton.setVisible(AppState.getInstance().isTestimonyRequested());
	    }

	    
	    @FXML
	    private void initialize() {
	        // Low Coupling: Uses methods from BaseController, reducing dependency on internal workings.

	    	addTestimonyButton.setOnAction(this::handleAddTestimonyAction);
	        addTestimonyButton.setVisible(AppState.getInstance().isTestimonyRequested());
	        refreshButtonState();
	        
	        addTestimonyButton.sceneProperty().addListener((observable, oldScene, newScene) -> {
	            if (newScene != null) {
	                newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
	                    if (newWindow != null) {
	                        refreshButtonState();
	                    }
	                });
	            }
	        });
	        
	        
	        cnicTextField.setPromptText("Enter 13 Digit's CNIC");
	        
	    }
	    
	    
	    @FXML
	    private void handleBackAction(ActionEvent event) {
	        goBack(event, "Civilian_menu.fxml"); 
	    }

	    @FXML
	    private void handleLogoutAction(ActionEvent event) {
	        logout(event);
	    }
}
