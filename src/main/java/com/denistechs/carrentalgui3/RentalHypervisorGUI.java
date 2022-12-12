//TODO: Implement buttons for both undo and redo
//TODO: Implement kind of a settings windows for modifying the properties file

package com.denistechs.carrentalgui3;

import com.denistechs.carrentalgui3.domain.Car;
import com.denistechs.carrentalgui3.domain.CarRental;
import com.denistechs.carrentalgui3.fileHandlers.CarRentalRepositoryFileHandler;
import com.denistechs.carrentalgui3.fileHandlers.CarRepositoryFileHandler;
import com.denistechs.carrentalgui3.repository.CarRentalRepository;
import com.denistechs.carrentalgui3.repository.CarRepository;
import com.denistechs.carrentalgui3.service.ExceptionCode;
import com.denistechs.carrentalgui3.service.ExceptionHandler;
import com.denistechs.carrentalgui3.service.PropertiesHandler;
import com.denistechs.carrentalgui3.service.Validator;
import com.denistechs.carrentalgui3.service.actionHandler.ActionRentalHandler;
import com.denistechs.carrentalgui3.service.actionHandler.actions.AddRentalAction;
import com.denistechs.carrentalgui3.service.actionHandler.actions.ModifyRentalAction;
import com.denistechs.carrentalgui3.service.actionHandler.actions.RemoveRentalAction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.EmptyStackException;

import static com.denistechs.carrentalgui3.fileHandlers.FilePathHandler.getFilePath;

public class RentalHypervisorGUI {
    private final CarRentalRepositoryFileHandler CRRFH;
    private final CarRepositoryFileHandler CRFH;
    private final PropertiesHandler PH;
    private final ExceptionHandler EH;
    private final Validator V;
    private CarRepository CR;
    private CarRentalRepository CRR;
    private boolean filterType = false;;
    private final ActionRentalHandler ARH;

    public RentalHypervisorGUI(PropertiesHandler PH, ExceptionHandler EH){
        this.CRFH = new CarRepositoryFileHandler();
        this.CRRFH = new CarRentalRepositoryFileHandler();
        this.PH = PH;
        this.EH = EH;
        this.V = new Validator();
        this.filterType = true;
        this.ARH = new ActionRentalHandler();
        init();
    }

    public RentalHypervisorGUI(PropertiesHandler PH, ExceptionHandler EH, CarRepository CR, CarRentalRepository CRR){
        this.CRFH = new CarRepositoryFileHandler();
        this.CRRFH = new CarRentalRepositoryFileHandler();
        this.PH = PH;
        this.EH = EH;
        this.V = new Validator();
        this.CR = CR;
        this.CRR = CRR;
        this.filterType = true;
        this.ARH = new ActionRentalHandler();
    }

    @FXML
    private TableView<CarRental> rentalTable;
    @FXML
    private TableColumn<Car, Integer> rentalIDColumn = new TableColumn<>();
    @FXML
    private TableColumn<Car, String> rentalCarColumn = new TableColumn<>();
    @FXML
    private TableColumn<Car, String> rentalNameColumn = new TableColumn<>();
    @FXML
    private TableColumn<Car, String> rentalDaysColumn = new TableColumn<>();
    @FXML
    private TableColumn<Car, String> rentalPriceColumn = new TableColumn<>();
    @FXML
    private TextField rentalNameInput = new TextField();
    @FXML
    private TextField rentalDaysInput = new TextField();
    @FXML
    private ChoiceBox<Integer> carIDCombo = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> nameFilterCombo = new ChoiceBox<>();
    @FXML
    private RadioButton ascFilterToggle = new RadioButton();
    @FXML
    private RadioButton descFilterToggle = new RadioButton();
    @FXML
    private Slider priceFilterSlider = new Slider();
    @FXML
    private Label priceSliderLabel = new Label();
    @FXML
    private MenuItem undoMenu = new MenuItem();
    @FXML
    private MenuItem redoMenu = new MenuItem();

    final KeyCombination keyCombCtrZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);
    final KeyCombination keyCombCtrY = new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN);

    @FXML
    public void initialize(){
        rentalIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        rentalCarColumn.setCellValueFactory(new PropertyValueFactory<>("carString"));
        rentalNameColumn.setCellValueFactory(new PropertyValueFactory<>("rentalName"));
        rentalDaysColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfDays"));
        rentalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

        ascFilterToggle.setSelected(filterType);


        this.priceFilterSlider.setMin(this.CRR.getMinPrice());
        this.priceFilterSlider.setMax(this.CRR.getMaxPrice());

        this.priceSliderLabel.setText(String.valueOf(this.priceFilterSlider.getValue()));

        this.priceSliderLabel.textProperty().bind(this.priceFilterSlider.valueProperty().asString("%.2f"));


        for(Car C: CR.getValues()) {
            if(C.getTaken() == false) {
                carIDCombo.getItems().add((Integer) C.getID());
            }
        }
        for(CarRental CR : CRR.getValues()){
            rentalTable.getItems().add(CR);
        }
        for(CarRental CR: CRR.getValues()){
            if(!nameFilterCombo.getItems().contains(CR.getRentalName())){
                nameFilterCombo.getItems().add(CR.getRentalName());
            }
        }
    }

    public void refreshCarIDCombo(){
        carIDCombo.getItems().clear();
        for(Car C: CR.getValues()) {
            if(C.getTaken() == false) {
                carIDCombo.getItems().add((Integer) C.getID());
            }
        }
    }

    public void refreshRentalTable(){
        rentalTable.getItems().clear();
        for(CarRental CR : CRR.getValues()){
            rentalTable.getItems().add(CR);
        }
    }

    public void refreshNameFilterCombo(){
        nameFilterCombo.getItems().clear();
        for(CarRental CR: CRR.getValues()){
            if(!nameFilterCombo.getItems().contains(CR.getRentalName())){
                nameFilterCombo.getItems().add(CR.getRentalName());
            }
        }
    }

    @FXML
    public void addRentalClick(){
        if(!(this.rentalNameInput.getText().isEmpty() || this.rentalDaysInput.getText().isEmpty() || this.carIDCombo.getValue() == null)){
            try {
                Car c, oc = this.CR.get(this.carIDCombo.getValue());
                c = oc;
                c.setTaken(true);
                CarRental cr = new CarRental(this.CRR.getGreatestID() + 1, c, Integer.parseInt(this.rentalDaysInput.getText()), this.rentalNameInput.getText());
                V.validateCarRental(cr);
                this.CR.modify((Integer) oc.getID(), oc,c);
                this.CRR.add((Integer) cr.getID(), cr);
                this.ARH.addAction(new AddRentalAction(cr));
            } catch (Exception e) {
                EH.GUIHandle(e);
            }
            refreshRentalTable();
            clearInputClick();
            refreshCarIDCombo();
            refreshNameFilterCombo();
        }
    }

    @FXML
    public void removeRentalClick(){
        if(this.rentalTable.getSelectionModel().getSelectedItem() != null){
            try {
                Car c, oc = this.CR.get((Integer) this.rentalTable.getSelectionModel().getSelectedItem().getRentedCar().getID());
                c = oc;
                c.setTaken(false);
                this.CR.modify((Integer) oc.getID(), oc,c);
                this.CRR.remove((Integer) this.rentalTable.getSelectionModel().getSelectedItem().getID(), this.rentalTable.getSelectionModel().getSelectedItem());
                this.ARH.addAction(new RemoveRentalAction(this.rentalTable.getSelectionModel().getSelectedItem()));
            } catch (Exception e) {
                EH.GUIHandle(e);
            }
            refreshRentalTable();
            clearInputClick();
            refreshCarIDCombo();
            refreshNameFilterCombo();
        }
    }

    @FXML
    public void onRentalTableClick()
    {
        if(this.rentalTable.getSelectionModel().getSelectedItem() != null)
        {
            CarRental cr = this.rentalTable.getSelectionModel().getSelectedItem();
            this.rentalNameInput.setText(cr.getRentalName());
            this.rentalDaysInput.setText(String.valueOf(cr.getNumberOfDays()));
            this.carIDCombo.setValue((Integer) cr.getRentedCar().getID());
        }
    }

    @FXML
    public void modifyRentalClick(){
        if(!(this.carIDCombo.getValue()==null || this.rentalDaysInput.getText().isEmpty() || this.rentalNameInput.getText().isEmpty())){
            try{
                Car c, oc = this.CR.get(this.carIDCombo.getValue());
                Car c2, oc2 = this.rentalTable.getSelectionModel().getSelectedItem().getRentedCar();
                c = oc;
                c2 = oc2;
                c2.setTaken(false);
                c.setTaken(true);
                CarRental cr = new CarRental(this.rentalTable.getSelectionModel().getSelectedItem().getID(), c, Integer.parseInt(this.rentalDaysInput.getText()), this.rentalNameInput.getText());
                V.validateCarRental(cr);
                this.CR.modify((Integer) oc.getID(), oc,c);
                this.CR.modify((Integer) oc2.getID(), oc2,c2);
                this.CRR.modify((Integer) this.rentalTable.getSelectionModel().getSelectedItem().getID(), this.rentalTable.getSelectionModel().getSelectedItem(), cr);
                this.ARH.addAction(new ModifyRentalAction(this.rentalTable.getSelectionModel().getSelectedItem(), cr));
            } catch (Exception e) {
                EH.GUIHandle(e);
            }
            refreshRentalTable();
            clearInputClick();
            refreshCarIDCombo();
            refreshNameFilterCombo();
        }
    }

    @FXML
    public void clearInputClick(){
        this.rentalNameInput.clear();
        this.rentalDaysInput.clear();
        this.carIDCombo.setValue(null);
    }

    @FXML
    public void resetFiltersClick(){
        refreshRentalTable();
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
    public void filterByNameClick(){
        if(!this.nameFilterCombo.getValue().isEmpty()){
            rentalTable.getItems().clear();
            for(CarRental CR : CRR.getValues()){
                if(CR.getRentalName().equals(this.nameFilterCombo.getValue())){
                    rentalTable.getItems().add(CR);
                }
            }
        }
    }

    @FXML
    public void filterByPriceClick(){
        rentalTable.getItems().clear();
        for(CarRental cr: CRR.getValues()){
            if(filterType){
                if(cr.getCost() <= this.priceFilterSlider.getValue()){
                    rentalTable.getItems().add(cr);
                }
            }
            else{
                if(cr.getCost() >= this.priceFilterSlider.getValue()){
                    rentalTable.getItems().add(cr);
                }
            }
        }
    }

    @FXML
    public void swapToCarMode(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Car-view.fxml"));
            HypervisorGUI HypervisorGUI = new HypervisorGUI(PH, EH, CR, CRR);
            loader.setController(HypervisorGUI);
            Parent root = loader.load();
            Scene scene = new Scene(root, 522,531);
            Stage mainStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            event.consume();
            mainStage.setScene(scene);
        } catch (Exception ex) {
            EH.GUIHandle(ex);
        }
    }

    @FXML
    public void undoClick(){
        try {
            Pair<CarRentalRepository, CarRepository> p = this.ARH.undo(this.CRR, this.CR);
            this.CRR = p.getKey();
            this.CR = p.getValue();
            refreshRentalTable();
        }catch (ExceptionCode e){
            EH.GUIHandle(e);
        }catch (EmptyStackException e){//do nothing
        }
    }

    @FXML
    public void redoClick(){
        try {
            Pair<CarRentalRepository, CarRepository> p = this.ARH.redo(this.CRR, this.CR);
            this.CRR = p.getKey();
            this.CR = p.getValue();
            refreshRentalTable();
        }catch (ExceptionCode e){
            EH.GUIHandle(e);
        } catch (EmptyStackException e){//do nothing
        }
    }

    @FXML
    public void handleUndoRedo(KeyEvent event){
        if(keyCombCtrZ.match(event)){
            event.consume();
            try {
                Pair<CarRentalRepository, CarRepository> p = this.ARH.undo(this.CRR, this.CR);
                this.CRR = p.getKey();
                this.CR = p.getValue();
                refreshRentalTable();
            }catch (ExceptionCode e){
                EH.GUIHandle(e);
            }catch (EmptyStackException e){//do nothing
            }
        }
        if(keyCombCtrY.match(event)){
            event.consume();
            try {
                Pair<CarRentalRepository, CarRepository> p = this.ARH.redo(this.CRR, this.CR);
                this.CRR = p.getKey();
                this.CR = p.getValue();
                refreshRentalTable();
            }catch (ExceptionCode e){
                EH.GUIHandle(e);
            } catch (EmptyStackException e){//do nothing
            }
        }
    }

    private void refreshUndoRedoMenu(){
        if(this.ARH.undoStackStatus()){
            this.undoMenu.setDisable(false);
        }else{
            this.undoMenu.setDisable(true);
        }

        if(this.ARH.redoStackStatus()){
            this.redoMenu.setDisable(false);
        }else{
            this.redoMenu.setDisable(true);
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
