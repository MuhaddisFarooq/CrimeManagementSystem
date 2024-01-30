package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedbackController extends BaseController {

    @FXML private ComboBox<String> officerComboBox;
    @FXML private Button submitButton, backButton;
    @FXML private TextArea feedbackArea;
    @FXML private TextField nameField, mobileNoField, emailField;

    private final String dbUrl = "jdbc:mysql://localhost:3306/cms";
    private final String dbUsername = "root";
    private final String dbPassword = "12345";

    // Assuming you have a way to get the current user's CNIC
    private String currentUserCnic = "current_user_cnic"; // replace with actual method to get CNIC

    @FXML
    private void initialize() {
        loadOfficers();
        setUserDetails(currentUserCnic); // Method to set user details
        setFieldPrompts();
    }

    private void setFieldPrompts() {
        nameField.setPromptText("Enter Your Name");
        mobileNoField.setPromptText("Enter Mobile No");
        emailField.setPromptText("Enter Email Address");
        feedbackArea.setPromptText("Enter Feedback");
    }

    private void setUserDetails(String cnic) {
        String query = "SELECT first_name, mobile_no, email_address FROM complaints WHERE cnic_no = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, cnic);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    nameField.setText(rs.getString("first_name"));
                    mobileNoField.setText(rs.getString("mobile_no"));
                    emailField.setText(rs.getString("email_address"));
                }
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Error fetching user details.", Alert.AlertType.ERROR);
        }
    }

    private void loadOfficers() {
        String query = "SELECT username FROM users WHERE userType = 'Officer'";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            ObservableList<String> officers = FXCollections.observableArrayList();
            while (rs.next()) {
                officers.add(rs.getString("username"));
            }
            officerComboBox.setItems(officers);
        } catch (SQLException e) {
            showAlert("Database Error", "Error loading officers.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSubmitAction(ActionEvent event) {
        if (!validateInput()) {
            return;
        }
        saveFeedback();
    }

    private boolean validateInput() {
        if (feedbackArea.getText().isEmpty() || officerComboBox.getValue() == null) {
            showAlert("Validation Error", "All fields are required.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void saveFeedback() {
        String sql = "INSERT INTO feedback (officer_id, officer_name, feedback, civilian_name) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Here, you will need to fetch the officer's ID based on the selected name
            int officerId = getOfficerId(officerComboBox.getValue());
            
            pstmt.setInt(1, officerId); // Set officer_id
            pstmt.setString(2, officerComboBox.getValue());
            pstmt.setString(3, feedbackArea.getText());
            pstmt.setString(4, nameField.getText()); // Civilian name

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert("Success", "Feedback submitted successfully.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Feedback submission failed.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // This prints the full stack trace to the console
            showAlert("Database Error", "Error submitting feedback: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private int getOfficerId(String officerName) {
        String query = "SELECT id FROM users WHERE username = ? AND userType = 'Officer'";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, officerName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error fetching officer ID.", Alert.AlertType.ERROR);
        }
        return -1; // Return -1 or another invalid value to indicate the ID was not found
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
