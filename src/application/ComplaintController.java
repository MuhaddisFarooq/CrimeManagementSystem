package application;

import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class ComplaintController extends BaseController {

    @FXML private TableView<Complaint> complaintsTable;
    @FXML private TableColumn<Complaint, Integer> idColumn;
    @FXML private TableColumn<Complaint, String> dateColumn;
    @FXML private TableColumn<Complaint, String> firNoColumn;
    @FXML private TableColumn<Complaint, String> descriptionColumn;
    @FXML private TableColumn<Complaint, String> statusColumn;

    @FXML private ComboBox<String> decisionComboBox;

    private final String dbUrl = "jdbc:mysql://localhost:3306/cms";
    private final String dbUsername = "root";
    private final String dbPassword = "12345";

    public void initialize() {
    	setupTableColumns(); // Abstracting 
        loadComplaints();

        decisionComboBox.getItems().addAll("Approve Complaint", "Deny Complaint");
        decisionComboBox.setPromptText("Select Option");

        loadComplaints();
    }
    
    // High Cohesion: The setupTableColumns method is highly focused on a single task

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        firNoColumn.setCellValueFactory(new PropertyValueFactory<>("firNo"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    // Information Expert: This method contains the knowledge to load complaints

    private void loadComplaints() {
        ObservableList<Complaint> complaints = FXCollections.observableArrayList();
        String query = "SELECT id, dateColumn, fir_no, complaint, status FROM complaints";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String date = rs.getString("dateColumn");
                String firNo = rs.getString("fir_no");
                String description = rs.getString("complaint");
                String status = rs.getString("status");
                complaints.add(new Complaint(id, date, firNo, description, status)); // Include status in constructor
            }
            complaintsTable.setItems(complaints);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle errors here
        }
    }

    @FXML
    // Controller: Acts as a controller in MVC, handling UI events

    private void handleDecisionAction() {
        Complaint selectedComplaint = complaintsTable.getSelectionModel().getSelectedItem();
        String selectedOption = decisionComboBox.getSelectionModel().getSelectedItem();

        if (selectedComplaint == null) {
            showAlert("Error", "No complaint selected.", AlertType.ERROR);
            return;
        }

        if (selectedOption == null) {
            showAlert("Error", "Please select an option (Approve or Deny).", AlertType.ERROR);
            return;
        }

        String status = "Approve Complaint".equals(selectedOption) ? "Approved" : "Denied";
        updateComplaintStatus(selectedComplaint.getId(), status);
        showAlert("Success", "Complaint " + status.toLowerCase() + ".", AlertType.INFORMATION);
    }
    private void updateComplaintStatus(int complaintId, String status) {
        boolean isApproved = "Approved".equals(status);
        String query = "UPDATE complaints SET is_approved = ?, status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setBoolean(1, isApproved);
            pstmt.setString(2, status);
            pstmt.setInt(3, complaintId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // Update was successful
                loadComplaints(); // Refresh the table view
            } else {
                // Update failed
                // Handle update failure (e.g., show error message)
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL errors (e.g., show error message)
        }
    }

    @FXML
    private void handleRequestTestimonyAction(ActionEvent event) {

        // Set the testimony requested state
        AppState.getInstance().setTestimonyRequested(true);
        
        showAlert("Success", "Testimony Requested Successfully.", AlertType.INFORMATION);

    }

    
    
    @FXML
    private void handleDeleteAction() {
        Complaint selectedComplaint = complaintsTable.getSelectionModel().getSelectedItem();
        if (selectedComplaint == null) {
            showAlert("Error", "No complaint selected.", AlertType.ERROR);
            return;
        }

        // Optional: Confirmation dialog before deletion
        if (!confirmDeletion()) {
            return; // Stop if user does not confirm
        }

        deleteComplaint(selectedComplaint.getId());
        loadComplaints(); // Refresh the table view
    }

    // Factory Method: Using Alert class to create a confirmation dialog

    private boolean confirmDeletion() {
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this record?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }

    private void deleteComplaint(int complaintId) {
        String query = "DELETE FROM complaints WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, complaintId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert("Success", "Complaint deleted successfully.", AlertType.INFORMATION);
            } else {
                showAlert("Error", "Error deleting complaint.", AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error while deleting complaint.", AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleBackAction(ActionEvent event) {
        goBack(event, "Police_Menu.fxml"); 
    }

    @FXML
    private void handleLogoutAction(ActionEvent event) {
        logout(event);
    }
    
   
}
