package application;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType; // Import AlertType
import javafx.stage.Stage;

// Inherits from BaseController - Inheritance principle
public class RegisterController extends BaseController {

    @FXML private Button backButton;
    @FXML private TextField usernameField, emailField;
    @FXML private PasswordField passwordField;
    @FXML private RadioButton civilianRadioButton, officerRadioButton;

    // Using a ToggleGroup for RadioButtons - Encapsulation principle
    private final ToggleGroup userTypeGroup = new ToggleGroup();

    // UserDAO for database operations - Data Access Object (DAO) Pattern
    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void initialize() {
        // Setting up the toggle group - Low Coupling (GRASP)
        civilianRadioButton.setToggleGroup(userTypeGroup);
        officerRadioButton.setToggleGroup(userTypeGroup);

        // Event handling - Controller Class in GRASP
        backButton.setOnAction(event -> goBackToLogin());
    }

    @FXML
    private void handleSignUp() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText(); // Ideally, you should hash the password
        String userType = civilianRadioButton.isSelected() ? "Civilian" : "Officer";

        // Validation logic - Information Expert (GRASP)
        if (!isValidUsername(username) || !isValidEmail(email) || !isValidPassword(password)) {
            return;
        }

        try {
            // Registering user - High Cohesion (GRASP)
            userDAO.registerUser(username, email, password, userType);
            showAlert("Registration Successful", "User registered successfully!", AlertType.INFORMATION);
            goBackToLogin();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Registration Error", "An error occurred during registration.", AlertType.ERROR);
        }
    }

    private boolean isValidUsername(String username) {
        String upperCaseChars = "(.*[A-Z]+.*)"; // Regex to check for at least one uppercase letter
        String allowedChars = "^[a-zA-Z]+$"; // Regex to allow only uppercase and lowercase letters

        if (username == null || username.isEmpty()) {
            showAlert("Invalid Username", "Username cannot be empty.", AlertType.ERROR);
            return false;
        }
        if (username.length() < 3 || username.length() > 25) {
            showAlert("Invalid Username", "Username must be between 3 and 25 characters.", AlertType.ERROR);
            return false;
        }
        if (!username.matches(upperCaseChars)) {
            showAlert("Invalid Username", "Username must contain at least one uppercase letter.", AlertType.ERROR);
            return false;
        }
        if (!username.matches(allowedChars)) {
            showAlert("Invalid Username", "Username can only contain letters.", AlertType.ERROR);
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (email == null || !email.matches(emailRegex)) {
            showAlert("Invalid Email", "Please enter a valid email address.", AlertType.ERROR);
            return false;
        }
        return true;
    }


    private boolean isValidPassword(String password) {
        if (password == null || password.isEmpty() || password.length() < 8) {
            showAlert("Invalid Password", "Password must be at least 8 characters long.", AlertType.ERROR);
            return false;
        }
        return true;
    }

    // Method to navigate back to the login screen - Factory Method (GoF) pattern
    private void goBackToLogin() {
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        navigateTo("login_registration.fxml", currentStage);
    }
}
