package com.denistechs.carrentalgui3;

import com.denistechs.carrentalgui3.service.ExceptionHandler;
import com.denistechs.carrentalgui3.service.PropertiesHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.denistechs.carrentalgui3.fileHandlers.FilePathHandler.getFilePath;
import static com.denistechs.carrentalgui3.service.ExceptionHandler.GUIHandle;

public class MainGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        PropertiesHandler propertiesHandler = new PropertiesHandler();
        try{
            propertiesHandler.init(getFilePath("settings.properties"));
        }catch(RuntimeException ex){
            GUIHandle(ex);
            return;
        }
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        try{
            exceptionHandler.init(getFilePath(propertiesHandler.getProperty("errorCodesPath")));
        } catch (RuntimeException ex){
            GUIHandle(ex);
            return;
        }
        HypervisorGUI hypervisorGUI = new HypervisorGUI(propertiesHandler, exceptionHandler);

        //FXML part
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Car-view.fxml"));
        fxmlLoader.setController(hypervisorGUI);
        Scene scene = new Scene(fxmlLoader.load(), 522, 531);
        stage.setTitle("Car Rental");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            e.consume();
            stage.close();
            hypervisorGUI.stop();
        });
        stage.show();
    }

    public static void main(String[] args) {
        org.burningwave.core.assembler.StaticComponentContainer.Modules.exportAllToAll();
        launch();
    }
}