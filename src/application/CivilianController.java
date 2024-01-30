package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CivilianController extends BaseController {

    @FXML
    private void handleBackAction(ActionEvent event) {
        // Polymorphism & Delegation: Utilizing a method from the BaseController
        goBack(event, "login_registration.fxml");
    }

    @FXML
    private void handleFirFormAction(ActionEvent event) {
        // Polymorphism & Delegation: Utilizing a method from the BaseController
        switchScene("FIR_form.fxml", event);
    }
    
    @FXML
    private void handleTrackStatusAction(ActionEvent event) {
        // Polymorphism & Delegation: Utilizing a method from the BaseController
        switchScene("track_status.fxml", event);
    }
    
    @FXML
    private void handleFeedbackAction(ActionEvent event) {
        // Polymorphism & Delegation: Utilizing a method from the BaseController
        switchScene("Feedback.fxml", event);
    }
}
