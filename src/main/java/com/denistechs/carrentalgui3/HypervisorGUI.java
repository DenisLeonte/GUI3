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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private boolean filterType = false;

    public HypervisorGUI(PropertiesHandler PH, ExceptionHandler EH){
        this.CRFH = new CarRepositoryFileHandler();
        this.CRRFH = new CarRentalRepositoryFileHandler();
        this.PH = PH;
        this.EH = EH;
        this.V = new Validator();
        init();
    }

    public HypervisorGUI(PropertiesHandler PH, ExceptionHandler EH, CarRepository CR, CarRentalRepository CRR){
        this.CRFH = new CarRepositoryFileHandler();
        this.CRRFH = new CarRentalRepositoryFileHandler();
        this.PH = PH;
        this.EH = EH;
        this.CR = CR;
        this.CRR = CRR;
        this.V = new Validator();
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
    @FXML
    private TextField carMakeInput = new TextField();
    @FXML
    private TextField carModelInput = new TextField();
    @FXML
    private TextField carPriceInput = new TextField();
    @FXML
    private ChoiceBox<String> makeFilterCombo = new ChoiceBox<>();
    @FXML
    private Slider priceFilterSlider = new Slider();
    @FXML
    private Label priceSliderLabel = new Label();
    @FXML
    private RadioButton ascFilterToggle = new RadioButton();
    @FXML
    private RadioButton descFilterToggle = new RadioButton();

    @FXML
    private void initialize(){
        carIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        carMakeColumn.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));
        carModelColumn.setCellValueFactory(new PropertyValueFactory<>("Model"));
        carPriceColumn.setCellValueFactory(new PropertyValueFactory<>("CostPerDay"));
        carAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("Taken"));

        for(String key: CR.getAllManufacturers().keySet()){
            makeFilterCombo.getItems().add(key);
        }

        this.ascFilterToggle.setSelected(true);
        this.descFilterToggle.setSelected(false);

        this.priceFilterSlider.setMin(this.CR.getMinPrice());
        this.priceFilterSlider.setMax(this.CR.getMaxPrice());

        this.priceSliderLabel.setText(String.valueOf(this.priceFilterSlider.getValue()));

        this.priceSliderLabel.textProperty().bind(this.priceFilterSlider.valueProperty().asString("%.2f"));

        for(Car c: CR.getValues()){
            carTable.getItems().add(c);
        }
    }

    private void refreshCarTable(){
        carTable.getItems().clear();
        for(Car c: CR.getValues()){
            carTable.getItems().add(c);
        }
    }

    @FXML
    public void addCarClick(){
        if(!(this.carMakeInput.getText().isEmpty() || this.carModelInput.getText().isEmpty() || this.carPriceInput.getText().isEmpty()))
            {
                Car c = new Car(this.carModelInput.getText(),this.carMakeInput.getText(),Double.parseDouble(this.carPriceInput.getText()),this.CR.getGreatestID() + 1, false);
                try {
                    V.validateCar(c);
                    this.CR.add((Integer) c.getID(),c);
                } catch (ExceptionCode ex) {
                    this.EH.GUIHandle(ex);
                } catch(RuntimeException ex)
                {
                    this.EH.GUIHandle(ex);
                }
                refreshCarTable();
                this.carModelInput.clear();
                this.carMakeInput.clear();
                this.carPriceInput.clear();
            }
    }

    @FXML
    public void removeCarClick(){
        Car c = carTable.getSelectionModel().getSelectedItem();
        try {
            this.CR.remove((Integer) c.getID(), c);
        } catch (ExceptionCode ex) {
            this.EH.GUIHandle(ex);
        }
        refreshCarTable();
    }

    @FXML
    public void onCarTableClick()
    {
        if(this.carTable.getSelectionModel().getSelectedItem() != null)
        {
            Car c = this.carTable.getSelectionModel().getSelectedItem();
            this.carMakeInput.setText(c.getManufacturer());
            this.carModelInput.setText(c.getModel());
            this.carPriceInput.setText(String.valueOf(c.getCostPerDay()));
        }
    }

    @FXML
    public void modifyCarClick(){

        if(!(this.carMakeInput.getText().isEmpty() || this.carModelInput.getText().isEmpty() || this.carPriceInput.getText().isEmpty()))
        {
            Car oc = carTable.getSelectionModel().getSelectedItem();
            Car nc = new Car(this.carModelInput.getText(),this.carMakeInput.getText(),Double.parseDouble(this.carPriceInput.getText()),this.CR.getGreatestID() + 1, false);
            V.validateCar(nc);
            try {
                this.CR.modify((Integer) oc.getID(),oc, nc);
            } catch (ExceptionCode ex) {
                throw new RuntimeException(ex);
            }
            refreshCarTable();
            this.carModelInput.clear();
            this.carMakeInput.clear();
            this.carPriceInput.clear();
        }
    }

    @FXML
    public void clearInputClick(){
        this.carModelInput.clear();
        this.carMakeInput.clear();
        this.carPriceInput.clear();
    }

    @FXML
    public void filterCarsByMakeClick(){

        if(this.makeFilterCombo.getValue() != null){
            carTable.getItems().clear();
            for(Car c: CR.getValues()){
                if(c.getManufacturer().equals(this.makeFilterCombo.getValue())){
                    carTable.getItems().add(c);
                }
            }
        }
    }

    @FXML
    public void armAscFilter(){
        this.descFilterToggle.setSelected(false);
        this.filterType = false;
    }

    @FXML
    public void armDescFilter(){
        this.ascFilterToggle.setSelected(false);
        this.filterType = true;
    }

    @FXML
    public void filterCarsByPriceClick(){
        carTable.getItems().clear();
        for(Car c: CR.getValues()){
            if(filterType){
                if(c.getCostPerDay() <= this.priceFilterSlider.getValue()){
                    carTable.getItems().add(c);
                }
            }
            else{
                if(c.getCostPerDay() >= this.priceFilterSlider.getValue()){
                    carTable.getItems().add(c);
                }
            }
        }
    }

    @FXML
    public void resetFiltersClick(){
        refreshCarTable();
    }

    @FXML
    public void swapToRentalMode(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Rental-view.fxml"));
            stop();
            RentalHypervisorGUI rentalHypervisorGUI = new RentalHypervisorGUI(PH, EH, CR, CRR);
            loader.setController(rentalHypervisorGUI);
            Parent root = loader.load();
            Scene scene = new Scene(root, 522,563);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void stop() {
        try {
            if (this.PH.getProperty("textOutput").equals("true")) {
                this.CRFH.saveCarRepository(this.CR, getFilePath(this.PH.getProperty("textCarsPath")), "txt");
                this.CRRFH.saveCarRentalRepository(this.CRR, getFilePath(this.PH.getProperty("textRentalsPath")), "txt");
            }
            if (this.PH.getProperty("binaryOutput").equals("true")) {
                this.CRFH.saveCarRepository(this.CR, getFilePath(this.PH.getProperty("binaryCarsPath")), "bin");
                this.CRRFH.saveCarRentalRepository(this.CRR, getFilePath(this.PH.getProperty("binaryRentalsPath")), "bin");
            }
            if (this.PH.getProperty("JSONOutput").equals("true")) {
                this.CRFH.saveCarRepository(this.CR, getFilePath(this.PH.getProperty("JSONCarsPath")), "JSON");
                this.CRRFH.saveCarRentalRepository(this.CRR, getFilePath(this.PH.getProperty("JSONRentalsPath")), "JSON");
            }
            if (this.PH.getProperty("XMLOutput").equals("true")) {
                this.CRFH.saveCarRepository(this.CR, getFilePath(this.PH.getProperty("XMLCarsPath")), "XML");
                this.CRRFH.saveCarRentalRepository(this.CRR, getFilePath(this.PH.getProperty("XMLRentalsPath")), "XML");
            }
            if (this.PH.getProperty("SQLOutput").equals("true")) {
                this.CRFH.saveToSQL(this.CR, this.PH.getProperty("SQLURL"), this.PH.getProperty("SQLUSER"), this.PH.getProperty("SQLPASS"));
                this.CRRFH.saveToSQL(this.CRR, this.PH.getProperty("SQLURL"), this.PH.getProperty("SQLUSER"), this.PH.getProperty("SQLPASS"));
            }
        }catch (Exception e){
            e.printStackTrace();
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