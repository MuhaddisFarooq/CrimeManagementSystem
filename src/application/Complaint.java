package application;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Complaint {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty date;
    private final SimpleStringProperty firNo; 
    private final SimpleStringProperty description;
    private final SimpleStringProperty status; 

    

    public Complaint(int id, String date, String firNo, String description,String status) {
        this.id = new SimpleIntegerProperty(id);
        this.date = new SimpleStringProperty(date);
        this.firNo = new SimpleStringProperty(firNo);
        this.description = new SimpleStringProperty(description);
        this.status = new SimpleStringProperty(status); // Initialize status

    
    }

    // Property Getters
    @SuppressWarnings("exports")
	public IntegerProperty idProperty() {
        return id;
    }

    @SuppressWarnings("exports")
	public StringProperty dateProperty() {
        return date;
    }

    @SuppressWarnings("exports")
	public StringProperty firNoProperty() { // Add getter for FIR number property
        return firNo;
    }

    @SuppressWarnings("exports")
	public StringProperty descriptionProperty() {
        return description;
    }

    
    @SuppressWarnings("exports")
	public StringProperty statusProperty() { 
        return status;
    }

    
    // Regular Getters
    public int getId() {
        return id.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getFirNo() { 
        return firNo.get();
    }

    public String getDescription() {
        return description.get();
    }
    
    public String getStatus() { 
        return status.get();
    }


    public void setId(int id) {
        this.id.set(id);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setFirNo(String firNo) { 
        this.firNo.set(firNo);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
    
    public void setStatus(String status) { 
        this.status.set(status);
    }
}
