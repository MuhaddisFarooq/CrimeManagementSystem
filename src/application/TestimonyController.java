package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestimonyController extends BaseController {
    // Encapsulation: Using private fields for UI components and database configuration

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField mobileNoField;
    @FXML private TextField relationshipField;
    @FXML private TextField activityField;
    @FXML private TextArea descriptionArea;
    @FXML private RadioButton truthfulnessYes;
    @FXML private RadioButton truthfulnessNo;

    private final String dbUrl = "jdbc:mysql://localhost:3306/cms";
    private final String dbUsername = "root";
    private final String dbPassword = "12345";

    // Initialization method - High Cohesion (GRASP) and Controller Class (GRASP)

    @FXML
    public void initialize() {
        ToggleGroup truthfulnessGroup = new ToggleGroup();
        truthfulnessYes.setToggleGroup(truthfulnessGroup);
        truthfulnessNo.setToggleGroup(truthfulnessGroup);

        int userId = SessionManager.getCurrentUserId();
        loadComplaintData(userId);
        
        // Set prompt text for the fields
        nameField.setPromptText("Enter Name");
        emailField.setPromptText("Enter Email Address");
        mobileNoField.setPromptText("Enter Mobile Number");
        relationshipField.setPromptText("Enter Relationship to Case");
        activityField.setPromptText("Enter Activity During Incident");
        descriptionArea.setPromptText("Enter Incident Description");
        
    }
    // Loading complaint data from database - Information Expert (GRASP)

    private void loadComplaintData(int userId) {
        String query = "SELECT first_name, email_address, mobile_no FROM complaints WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("first_name"));
                emailField.setText(rs.getString("email_address"));
                mobileNoField.setText(rs.getString("mobile_no"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Handling the submission - Controller Class (GRASP)

    @FXML
    private void handleSubmitAction() {
        if (!validateFields()) {
            return; // Validation failed, error dialog is shown
        }
        String name = nameField.getText();
        String email = emailField.getText();
        String mobileNo = mobileNoField.getText();

        if (!isComplaintValid(name, email, mobileNo)) {
            // Display an error message or take appropriate action
            showAlert("Error", "The entered details do not match any existing complaint.", AlertType.ERROR);
            return;
        }

        String relationship = relationshipField.getText();
        String activity = activityField.getText();
        String description = descriptionArea.getText();
        boolean truthfulness = truthfulnessYes.isSelected();

        insertTestimony(name, email, mobileNo, relationship, activity, description, truthfulness);
        showAlert("Success", "Testimony Submitted Successfully.", AlertType.INFORMATION);
    }

    private boolean validateFields() {
        if (nameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty() ||
            mobileNoField.getText().trim().isEmpty() ||
            relationshipField.getText().trim().isEmpty() ||
            activityField.getText().trim().isEmpty() ||
            descriptionArea.getText().trim().isEmpty()) {

            showAlert("Validation Error", "All fields are required.", AlertType.ERROR);
            return false;
        }
        return true;
    }

    private boolean isComplaintValid(String name, String email, String mobileNo) {
        String query = "SELECT COUNT(*) FROM complaints WHERE first_name = ? AND email_address = ? AND mobile_no = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, mobileNo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void insertTestimony(String name, String email, String mobileNo, String relationship, String activity, String description, boolean truthfulness) {
        String insertQuery = "INSERT INTO testimonies (name, email, mobile_no, relationship_to_case, activity_during_incident, incident_description, truthfulness) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, mobileNo);
            pstmt.setString(4, relationship);
            pstmt.setString(5, activity);
            pstmt.setString(6, description);
            pstmt.setBoolean(7, truthfulness);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Handling navigation back action - Factory Method (GoF)

    @FXML
    private void handleBackAction(ActionEvent event) {
        goBack(event, "track_status.fxml"); // Pass the specific FXML file for going back
    }

    @FXML
    private void handleLogoutAction(ActionEvent event) {
        logout(event); // This will always go to the login_registration.fxml
    }
    
    
    
    
}
