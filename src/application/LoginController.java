package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;

// Inherits from BaseController - Demonstrating Inheritance principle
public class LoginController extends BaseController{

    // UI elements annotated with @FXML - Encapsulation
    @FXML private Hyperlink registerLink;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> userTypeComboBox;
    @FXML private Button loginButton;

    // Encapsulating database operations in a separate DAO class - Data Access Object (DAO) Pattern
    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void initialize() {
        // Setting items in ComboBox - Encapsulation
        userTypeComboBox.setItems(FXCollections.observableArrayList("Civilian", "Officer"));

        // Event handling for registerLink - Controller Class in GRASP
        registerLink.setOnAction(event -> switchToRegistration());
    }

    @FXML
    private void handleLoginButtonClick() {
        // Handling login logic with encapsulated validation methods - High Cohesion (GRASP)
        if (!isValidUsername(usernameField.getText()) || !isValidPassword(passwordField.getText())) {
            return;
        }

        // Conditional logic based on user input - Protected Variations (GRASP)
        if (userTypeComboBox.getValue() == null) {
            showAlert("Error", "Please select a user type.", Alert.AlertType.ERROR);
            return;
        }

        // Using DAO for authentication - Separation of concerns
        if (userDAO.authenticateUser(usernameField.getText(), passwordField.getText(), userTypeComboBox.getValue())) {
            // Setting session details upon successful login - Information Expert (GRASP)
            SessionManager.setCurrentUserName(usernameField.getText());
            SessionManager.setCurrentUserId(userDAO.getUserId(usernameField.getText(), userTypeComboBox.getValue()));
            
            navigateToMenu(userTypeComboBox.getValue());
        } else {
            showAlert("Error", "Credentials are incorrect.", Alert.AlertType.ERROR);
        }
    }

    // Validation methods for username and password - Information Expert (GRASP)
    private boolean isValidUsername(String username) {
        String upperCaseChars = "(.*[A-Z].*)";
        if (username == null || username.isEmpty() || username.length() < 3 || username.length() > 25 || !username.matches(upperCaseChars)) {
            showAlert("Invalid Username", "Username must be between 3 and 25 characters and contain at least one uppercase letter.", AlertType.ERROR);
            return false;
        }
        return true;
    }


    private boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            showAlert("Invalid Password", "Password cannot be empty.", AlertType.ERROR);
            return false;
        }
        return true;
    }

    // Navigates to different menus based on user type - Polymorphism
    private void navigateToMenu(String userType) {
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        if ("Civilian".equals(userType)) {
            navigateTo("Civilian_Menu.fxml", currentStage);
        } else if ("Officer".equals(userType)) {
            navigateTo("Police_Menu.fxml", currentStage);
        }
    }

    // Method to switch to the registration scene - Factory Method (GoF) pattern in navigateTo
    private void switchToRegistration() {
        Stage currentStage = (Stage) registerLink.getScene().getWindow();
        navigateTo("registration.fxml", currentStage);
    }
}
