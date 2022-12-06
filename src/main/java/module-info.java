module com.denistechs.carrentalgui3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires jakarta.xml.bind;
    requires java.sql;
    requires com.google.gson;

    opens com.denistechs.carrentalgui3 to javafx.fxml;
    exports com.denistechs.carrentalgui3;
}