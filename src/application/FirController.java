package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class FirController extends BaseController {

    @FXML private TextField mobileNoField;
    @FXML private TextField firstNameField;
    @FXML private TextField fatherNameField;
    @FXML private TextField cnicNoField;
    @FXML private TextField emailField;
    @FXML private TextField landlineNoField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField countryField;
    @FXML private TextField provinceField;
    @FXML private TextField homeDistrictField;
    @FXML private TextField policeStationField;
    @FXML private TextArea addressArea;
    @FXML private TextField relevantDistrictField;
    @FXML private TextField relevantStationField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField complaintAgainstField;
    @FXML private TextField designationField;
    @FXML private TextField firNoField;
    @FXML private TextArea complaintArea;
    @FXML private Button backButton;
    @FXML private DatePicker datePicker;

    final String dbUrl = "jdbc:mysql://localhost:3306/cms";
    final String dbUsername = "root";
    final String dbPassword = "12345";

    @FXML
    private void handleSubmitAction()
    {
    	 if (!validateFields()) {
             return; // Validation failed, error dialog is shown in the method
         }
    	
    	
    	  String firNumber = generateFirNumber();
          String sql = "INSERT INTO complaints (mobile_no, first_name, father_name, cnic_no, email_address, " +
                       "landline_no, age, gender, country, province, home_district, police_station, address, " +
                       "relevant_district, relevant_station, category, complaint_against, designation, fir_no, complaint, dateColumn) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, mobileNoField.getText());
            pstmt.setString(2, firstNameField.getText());
            pstmt.setString(3, fatherNameField.getText());
            pstmt.setString(4, cnicNoField.getText());
            pstmt.setString(5, emailField.getText());
            pstmt.setString(6, landlineNoField.getText());
            pstmt.setInt(7, Integer.parseInt(ageField.getText()));
            pstmt.setString(8, genderComboBox.getValue());
            pstmt.setString(9, countryField.getText());
            pstmt.setString(10, provinceField.getText());
            pstmt.setString(11, homeDistrictField.getText());
            pstmt.setString(12, policeStationField.getText());
            pstmt.setString(13, addressArea.getText());
            pstmt.setString(14, relevantDistrictField.getText());
            pstmt.setString(15, relevantStationField.getText());
            pstmt.setString(16, categoryComboBox.getValue());
            pstmt.setString(17, complaintAgainstField.getText());
            pstmt.setString(18, designationField.getText());
            pstmt.setString(19, firNumber); // Set the generated FIR number
            pstmt.setString(20, complaintArea.getText());
            pstmt.setString(20, complaintArea.getText());

         
            
            // Add the date from the DatePicker
            if (datePicker.getValue() != null) {
                pstmt.setDate(21, java.sql.Date.valueOf(datePicker.getValue()));
            } else {
                pstmt.setNull(21, java.sql.Types.DATE);
            }
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert("Success", "FIR submitted successfully.", AlertType.INFORMATION);
            } else {
                showAlert("Error", "FIR submission failed.", AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error while submitting FIR.", AlertType.ERROR);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showAlert("Input Error", "Error in processing input data.", AlertType.ERROR);
        }
    }

    
   
    private String generateFirNumber() {
        Random random = new Random();
        return "FIR" + random.nextInt(10000); 
    }
    @FXML
    private void initialize() {
        
    	mobileNoField.setPromptText("Enter Mobile No");
        firstNameField.setPromptText("Enter First Name");
        fatherNameField.setPromptText("Enter Father's Name");
        cnicNoField.setPromptText("Enter 13 Digit CNIC No");
        emailField.setPromptText("Enter Email Address ");
        landlineNoField.setPromptText("Enter Landline No");
        ageField.setPromptText("Enter Age");
        countryField.setPromptText("Enter Country");
        provinceField.setPromptText("Enter Province");
        homeDistrictField.setPromptText("Enter Home District");
        policeStationField.setPromptText("Enter Police Station");
        addressArea.setPromptText("Enter Address");
        relevantDistrictField.setPromptText("Enter Relevant District");
        relevantStationField.setPromptText("Enter Relevant Station");
        complaintAgainstField.setPromptText("Enter Complaint Against");
        designationField.setPromptText("Enter Designation");
        complaintArea.setPromptText("Enter Complaint Details");
    	
    	
    	genderComboBox.getItems().addAll("Male", "Female", "Other");
        categoryComboBox.getItems().addAll("Against Police", "Against Public", "Terrorism", "General Crime & Law Order", "Violence Against Women", "Violence Against Children", "General Complaint", "Traffic Jam", "Fraud", "Murder", "Cyber Crime", "Sexual Abuse", "Other");
        
        
    }
   
    private boolean validateFields() {
        if (!isValidMobileNumber(mobileNoField.getText())) {
            showAlert("Validation Error", "Invalid Mobile Number. Format should be 03XX1234567", AlertType.ERROR);
            return false;
        }
        if (firstNameField.getText().isEmpty()) {
            showAlert("Validation Error", "First Name is required.", AlertType.ERROR);
            return false;
        }
        if (fatherNameField.getText().isEmpty()) {
            showAlert("Validation Error", "Father's Name is required.", AlertType.ERROR);
            return false;
        }
        if (cnicNoField.getText().isEmpty() || !cnicNoField.getText().matches("\\d{13}")) {
            showAlert("Validation Error", "Valid CNIC Number is required (13 digits without dashes).", AlertType.ERROR);
            return false;
        }
        if (emailField.getText().isEmpty() || !emailField.getText().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            showAlert("Validation Error", "Valid Email is required.", AlertType.ERROR);
            return false;
        }
        if (!isValidPhoneNumber(landlineNoField.getText())) {
            showAlert("Validation Error", "Invalid Landline Number. Format should be 0421234567", AlertType.ERROR);
            return false;
        }
        if (ageField.getText().isEmpty() || !ageField.getText().matches("\\d+")) {
            showAlert("Validation Error", "Valid Age is required.", AlertType.ERROR);
            return false;
        }
        
        if (datePicker.getValue() == null) {
            showAlert("Validation Error", "Incident Date is required.", AlertType.ERROR);
            return false;
        }
        
        if (relevantDistrictField.getText().isEmpty()) {
            showAlert("Validation Error", "Relevant District is required.", AlertType.ERROR);
            return false;
        }
        if (relevantStationField.getText().isEmpty()) {
            showAlert("Validation Error", "Relevant Station is required.", AlertType.ERROR);
            return false;
        }
        if (categoryComboBox.getValue() == null) {
            showAlert("Validation Error", "Category selection is required.", AlertType.ERROR);
            return false;
        }
        if (complaintArea.getText().isEmpty()) {
            showAlert("Validation Error", "Complaint Details are required.", AlertType.ERROR);
            return false;
        }
      
        
        if (genderComboBox.getValue() == null) {
            showAlert("Validation Error", "Gender selection is required.", AlertType.ERROR);
            return false;
        }
        if (countryField.getText().isEmpty()) {
            showAlert("Validation Error", "Country is required.", AlertType.ERROR);
            return false;
        }
        if (provinceField.getText().isEmpty()) {
            showAlert("Validation Error", "Province is required.", AlertType.ERROR);
            return false;
        }
        if (homeDistrictField.getText().isEmpty()) {
            showAlert("Validation Error", "Home District is required.", AlertType.ERROR);
            return false;
        }
        if (policeStationField.getText().isEmpty()) {
            showAlert("Validation Error", "Police Station is required.", AlertType.ERROR);
            return false;
        }
        if (addressArea.getText().isEmpty()) {
            showAlert("Validation Error", "Address is required.", AlertType.ERROR);
            return false;
        }
       
        if (complaintAgainstField.getText().isEmpty()) {
            showAlert("Validation Error", "Complaint Against is required.", AlertType.ERROR);
            return false;
        }
        if (designationField.getText().isEmpty()) {
            showAlert("Validation Error", "Designation is required.", AlertType.ERROR);
            return false;
        }
       

        return true;
    }
    
    private boolean isValidMobileNumber(String mobileNo) {
        return mobileNo != null && mobileNo.matches("03\\d{2}\\d{7}");
    }
    
    private boolean isValidPhoneNumber(String number) {
        return number != null && number.matches("0\\d{10}");
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
