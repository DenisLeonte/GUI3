package com.denistechs.carrentalgui3;

import com.denistechs.carrentalgui3.fileHandlers.CarRentalRepositoryFileHandler;
import com.denistechs.carrentalgui3.fileHandlers.CarRepositoryFileHandler;
import com.denistechs.carrentalgui3.repository.CarRentalRepository;
import com.denistechs.carrentalgui3.repository.CarRepository;
import com.denistechs.carrentalgui3.service.ExceptionHandler;
import com.denistechs.carrentalgui3.service.PropertiesHandler;
import com.denistechs.carrentalgui3.service.Validator;

import static com.denistechs.carrentalgui3.fileHandlers.FilePathHandler.getFilePath;

public class RentalHypervisorGUI {
    private final CarRentalRepositoryFileHandler CRRFH;
    private final CarRepositoryFileHandler CRFH;
    private final PropertiesHandler PH;
    private final ExceptionHandler EH;
    private final Validator V;
    private CarRepository CR;
    private CarRentalRepository CRR;
    private boolean filterType = false;

    public RentalHypervisorGUI(PropertiesHandler PH, ExceptionHandler EH){
        this.CRFH = new CarRepositoryFileHandler();
        this.CRRFH = new CarRentalRepositoryFileHandler();
        this.PH = PH;
        this.EH = EH;
        this.V = new Validator();
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
