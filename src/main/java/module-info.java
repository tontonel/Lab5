module com.app.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.testng;
    requires org.junit.jupiter.api;


    opens com.app.gui to javafx.fxml;
    exports com.app.gui;
}