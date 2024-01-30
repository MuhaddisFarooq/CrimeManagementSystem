module Firstjavaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
	requires javafx.graphics;

    // Export the 'application' package to at least 'javafx.graphics' and 'javafx.fxml'
    exports application to javafx.graphics, javafx.fxml;
    opens application to javafx.fxml, javafx.base;
}
