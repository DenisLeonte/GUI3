package com.denistechs.carrentalgui3;

import com.denistechs.carrentalgui3.domain.Car;
import com.denistechs.carrentalgui3.fileHandlers.CarRentalRepositoryFileHandler;
import com.denistechs.carrentalgui3.fileHandlers.CarRepositoryFileHandler;
import com.denistechs.carrentalgui3.repository.CarRentalRepository;
import com.denistechs.carrentalgui3.repository.CarRepository;
import com.denistechs.carrentalgui3.service.ExceptionCode;
import com.denistechs.carrentalgui3.service.ExceptionHandler;
import com.denistechs.carrentalgui3.service.PropertiesHandler;
import com.denistechs.carrentalgui3.service.Validator;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.denistechs.carrentalgui3.fileHandlers.FilePathHandler.getFilePath;

public class HypervisorGUI {
    //Controller components
    private final CarRentalRepositoryFileHandler CRRFH;
    private final CarRepositoryFileHandler CRFH;
    private final PropertiesHandler PH;
    private final ExceptionHandler EH;
    private final Validator V;
    private CarRepository CR;
    private CarRentalRepository CRR;

    public HypervisorGUI(PropertiesHandler PH, ExceptionHandler EH){
        this.CRFH = new CarRepositoryFileHandler();
        this.CRRFH = new CarRentalRepositoryFileHandler();
        this.PH = PH;
        this.EH = EH;
        this.V = new Validator();
        init();
        initTable();
    }

    //FXML components
    @FXML
    private TableView<Car> carTable = new TableView<>();
    @FXML
    private TableColumn<Car, Integer> carIDColumn = new TableColumn<>();
    @FXML
    private TableColumn<Car, String> carMakeColumn = new TableColumn<>();
    @FXML
    private TableColumn<Car, String> carModelColumn = new TableColumn<>();
    @FXML
    private TableColumn<Car, Double> carPriceColumn = new TableColumn<>();
    @FXML
    private TableColumn<Car, String> carAvailableColumn = new TableColumn<>();

    private void initTable(){
        carIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        carMakeColumn.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));
        carModelColumn.setCellValueFactory(new PropertyValueFactory<>("Model"));
        carPriceColumn.setCellValueFactory(new PropertyValueFactory<>("CostPerDay"));
        carAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("Taken"));
        ObservableList<Car> cars = FXCollections.observableArrayList();
        for(Car c: CR.getValues()){
            cars.add(c);
        }
        carTable.setItems(cars);
    }

    public void stop() throws ExceptionCode {
        if(this.PH.getProperty("textOutput").equals("true")) {
            this.CRFH.saveCarRepository(this.CR, getFilePath(this.PH.getProperty("textCarsPath")), "txt");
            this.CRRFH.saveCarRentalRepository(this.CRR, getFilePath(this.PH.getProperty("textRentalsPath")), "txt");
        }
        if(this.PH.getProperty("binaryOutput").equals("true")) {
            this.CRFH.saveCarRepository(this.CR, getFilePath(this.PH.getProperty("binaryCarsPath")), "bin");
            this.CRRFH.saveCarRentalRepository(this.CRR, getFilePath(this.PH.getProperty("binaryRentalsPath")), "bin");
        }
        if(this.PH.getProperty("JSONOutput").equals("true")) {
            this.CRFH.saveCarRepository(this.CR, getFilePath(this.PH.getProperty("JSONCarsPath")), "JSON");
            this.CRRFH.saveCarRentalRepository(this.CRR, getFilePath(this.PH.getProperty("JSONRentalsPath")), "JSON");
        }
        if(this.PH.getProperty("XMLOutput").equals("true")) {
            this.CRFH.saveCarRepository(this.CR, getFilePath(this.PH.getProperty("XMLCarsPath")), "XML");
            this.CRRFH.saveCarRentalRepository(this.CRR, getFilePath(this.PH.getProperty("XMLRentalsPath")), "XML");
        }
        if(this.PH.getProperty("SQLOutput").equals("true")) {
            this.CRFH.saveToSQL(this.CR,this.PH.getProperty("SQLURL"),this.PH.getProperty("SQLUSER"),this.PH.getProperty("SQLPASS"));
            this.CRRFH.saveToSQL(this.CRR, this.PH.getProperty("SQLURL"),this.PH.getProperty("SQLUSER"),this.PH.getProperty("SQLPASS"));
        }
    }

    private void init(){
        if(this.PH.getProperty("textInput").equals("true")) {
            this.CR = this.CRFH.loadCarRepository(getFilePath(this.PH.getProperty("textCarsPath")), "txt");
            this.CRR = this.CRRFH.loadCarRentalRepository(getFilePath(this.PH.getProperty("textRentalsPath")), "txt", this.CR);
        }
        if(this.PH.getProperty("binaryInput").equals("true")) {
            this.CR = this.CRFH.loadCarRepository(getFilePath(this.PH.getProperty("binaryCarsPath")), "bin");
            this.CRR = this.CRRFH.loadCarRentalRepository(getFilePath(this.PH.getProperty("binaryRentalsPath")), "bin", this.CR);
        }
        if(this.PH.getProperty("JSONInput").equals("true")) {
            this.CR = this.CRFH.loadCarRepository(getFilePath(this.PH.getProperty("JSONCarsPath")), "JSON");
            this.CRR = this.CRRFH.loadCarRentalRepository(getFilePath(this.PH.getProperty("JSONRentalsPath")), "JSON", this.CR);
        }
        if(this.PH.getProperty("XMLInput").equals("true")) {
            this.CR = this.CRFH.loadCarRepository(getFilePath(this.PH.getProperty("XMLCarsPath")),"XML");
            this.CRR = this.CRRFH.loadCarRentalRepository(getFilePath(this.PH.getProperty("XMLRentalsPath")),"XML", this.CR);
        }
        if(this.PH.getProperty("SQLInput").equals("true")){
            this.CR = this.CRFH.loadFromSQL(this.PH.getProperty("SQLURL"),this.PH.getProperty("SQLUSER"),this.PH.getProperty("SQLPASS"));
            this.CRR = this.CRRFH.loadFromSQL(this.PH.getProperty("SQLURL"),this.PH.getProperty("SQLUSER"),this.PH.getProperty("SQLPASS"), this.CR);
        }
    }
}