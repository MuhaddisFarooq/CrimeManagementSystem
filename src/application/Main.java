package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
    // Define constants for width and height
    public static final int SCENE_WIDTH = 600;
    public static final int SCENE_HEIGHT = 600;

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StartSystem.fxml"));
            BorderPane root = loader.load();
            Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            primaryStage.setTitle("APS");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
