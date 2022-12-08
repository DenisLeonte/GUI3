module com.denistechs.carrentalgui3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires jakarta.xml.bind;
    requires java.sql;
    requires com.google.gson;
    requires org.burningwave.core;

    opens com.denistechs.carrentalgui3 to javafx.fxml;
    opens com.denistechs.carrentalgui3.domain to javafx.base;
    opens com.denistechs.carrentalgui3.repository to com.fasterxml.jackson.databind, jakarta.xml.bind;
    exports com.denistechs.carrentalgui3;
    exports com.denistechs.carrentalgui3.domain to com.fasterxml.jackson.databind;
    exports com.denistechs.carrentalgui3.repository to com.google.gson;
}