package com.denistechs.carrentalgui3;

import com.denistechs.carrentalgui3.service.ExceptionHandler;
import com.denistechs.carrentalgui3.service.PropertiesHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.denistechs.carrentalgui3.fileHandlers.FilePathHandler.getFilePath;

public class MainGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {

        PropertiesHandler propertiesHandler = new PropertiesHandler();
        //propertiesHandler.init(getClass().getClassLoader().getResource("settings.properties").toString().substring(6));
        propertiesHandler.init(getFilePath("settings.properties"));
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        exceptionHandler.init(getFilePath(propertiesHandler.getProperty("errorCodesPath")));
        HypervisorGUI hypervisorGUI = new HypervisorGUI(propertiesHandler, exceptionHandler);

        //FXML part
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Car-view.fxml"));
        fxmlLoader.setController(hypervisorGUI);
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}