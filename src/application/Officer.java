package application;

public class Officer {
    private int id;
    private String username;

    public Officer(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return username; // This is what will be displayed in the ComboBox
    }
    
    
}
