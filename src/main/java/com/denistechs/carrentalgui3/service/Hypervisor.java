package com.denistechs.carrentalgui3.service;

import com.denistechs.carrentalgui3.domain.Car;
import com.denistechs.carrentalgui3.domain.CarRental;
import com.denistechs.carrentalgui3.fileHandlers.CarRentalRepositoryFileHandler;
import com.denistechs.carrentalgui3.fileHandlers.CarRepositoryFileHandler;
import com.denistechs.carrentalgui3.repository.CarRentalRepository;
import com.denistechs.carrentalgui3.repository.CarRepository;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Hypervisor {

    private final ExceptionHandler EH;
    private final PropertiesHandler PH;
    private CarRepository CR;
    private CarRentalRepository CRR;

    private final CarRepositoryFileHandler CRFH;
    private final CarRentalRepositoryFileHandler CRRFH;
    /***
     * Constructor for the Hypervisor
     * @param e The Exception Handler
     * @param ph The Properties Handler
     */
    public Hypervisor(ExceptionHandler e, PropertiesHandler ph) {
        this.EH = e;
        this.PH = ph;
        this.CR = new CarRepository();
        this.CRR = new CarRentalRepository();
        this.CRFH = new CarRepositoryFileHandler();
        this.CRRFH = new CarRentalRepositoryFileHandler();
    }

    //Exception handling
    /***
     * Method to handle exceptions
     * @param ex The exception to be handled
     * @return The String representation of the exception
     */
    public String handleException(ExceptionCode ex) {
        return this.EH.handle(ex);
    }

    //Car part
    /***
     * Method to get the String representation of a car by the given index
     * @param index The index of the car
     * @return The String representation of the car
     * @throws ExceptionCode code 4
     */
    public String getCarString(int index) throws ExceptionCode {
        //Code 4
        return this.CR.get(index).toString();
    }

    /***
     * Adds a Car object to the CarRepository
     *
     * @param man The manufacturer of the car
     * @param mod The model of the car
     * @param ppd The price per day of the car
     * @throws ExceptionCode code 1, 2 ,7
     */
    public void addCar(String man, String mod, double ppd) throws ExceptionCode {
        int id = this.CR.getSize() + 1;
        Car c = new Car(mod,man,ppd,id,false);
        //Code 1, 2
        Validator.validateCar(c);
        this.CR.add(id, c);
    }

    /***
     * Modify a car object in the CarRepository
     * @param oldIndex Index of the car to be modified
     * @param man The new manufacturer of the car
     * @param mod The new model of the car
     * @param ppd The new price per day of the car
     * @throws ExceptionCode code 1, 3, 4, 7
     */
    public void modifyCar(int oldIndex, String man, String mod, double ppd) throws ExceptionCode {
        //Code 4
        Car oldCar = (Car)this.CR.get(oldIndex);
        Car newCar = new Car(mod,man,ppd,(Integer)oldCar.getID(), oldCar.getTaken());
        //Code 1, 3
        Integer ol = oldIndex;
        Validator.validateCar(newCar);
        this.CR.modify(ol,oldCar,newCar);
    }

    /***
     * "Upscales" the entire CarRepository by one. So now that the car with the ID 3 has ID 4 and so on
     * @param index Index from which the upscale should start
     * @throws ExceptionCode code 1, 3, 4
     */
    private void upscaleCarIndex(int index) throws ExceptionCode {
        for(int i = index; i <= this.CR.getSize() + 1; i++)
        {
            //Code 4
            Car c = (Car)this.CR.get(i);
            c.setID(i-1);
            //Code 1, 3
            Validator.validateCar(c);
            this.CR.modify(i,CR.get(i), c);
        }
    }

    /***
     * Removes a car from the CarRepository
     * @param index Index of the car to be removed
     * @throws ExceptionCode code 1, 3, 4
     */
    public void removeCar(int index) throws ExceptionCode {
        //index = carID - 1
        //Code 1, 3, 4
        this.CR.remove(index, CR.get(index));
        //Code 1, 3, 4
        this.upscaleCarIndex(index + 1);
    }

    public String getAvailableCars()
    {
        Predicate<Car> getAvailable = c -> c.getTaken() != true;
        Stream.Builder<Car> carSB = Stream.builder();
        Iterable<Car> i = this.CR.getValues();
        for(Car c : i)
        {
            carSB.accept(c);
        }
        Stream<Car> carS = carSB.build();
        Iterator<Car> j = carS.filter(getAvailable).collect(Collectors.toList()).iterator();
        StringBuilder sb = new StringBuilder();
        while(j.hasNext())
        {
            sb.append(j.next().toString()).append("\n");
        }
        return sb.toString();
    }

    public String sortCarsByPPD(){
        Stream.Builder<Car> carSB = Stream.builder();
        Iterable<Car> i = this.CR.getValues();
        for(Car c : i)
        {
            carSB.accept(c);
        }
        Stream<Car> carS = carSB.build();
        Iterator<Car> j = carS.sorted((c1,c2) -> {if(c1.getCostPerDay()>c2.getCostPerDay())return -1;
        else if (c1.getCostPerDay() == c2.getCostPerDay()) return 0;
        else return 1;}).collect(Collectors.toList()).iterator();
        StringBuilder sb = new StringBuilder();
        while(j.hasNext())
        {
            sb.append(j.next().toString()).append("\n");
        }
        return sb.toString();
    }

    public String filterPrice(double lower, double upper){
        Stream.Builder<Car> carSB = Stream.builder();
        Iterable<Car> i = this.CR.getValues();
        for(Car c : i) {
            carSB.accept(c);
        }
        Stream<Car> carStream = carSB.build();

        Iterator<Car> k = carStream.filter((c) -> c.getCostPerDay() >= lower && c.getCostPerDay() <= upper).collect(Collectors.toList()).iterator();
        StringBuilder sb = new StringBuilder();
        while(k.hasNext())
        {
            sb.append(k.next().toString()).append('\n');
        }
        return sb.toString();
    }


    public Iterable<Car> getCarIterable()
    {
        return this.CR.getValues();
    }

    //Car Rental Part

    /***
     *  Returns a String with the list of the car available, if there are no car rentals, it returns "No cars available"
     * @return String object
     */
    public String listAvailableCars() {
        StringBuilder sb = new StringBuilder();
        Function<Car, String> getString = c -> c.toString() + "\n";
        Predicate<Car> taken = c -> c.getTaken();
        Iterable<Car> i = this.CR.getValues();
        for(Car c:i) {
            if (!taken.test(c)) sb.append(getString.apply(c));
        }
        String s = sb.toString();
        if(s.isBlank()) s = "No cars available";
        return s;
    }

    /***
     * Returns a String with the list of the car rentals, if there are no car rentals, it returns a void String
     * @return String object
     */
    public String listRentals() {
        StringBuilder sb = new StringBuilder();
        Function<CarRental, String> getString = c -> c.toString() + "\n";

        Iterable<CarRental> i = this.CRR.getValues();
        for(CarRental cr:i){
            sb.append(getString.apply(cr));
        }
        return sb.toString();
    }

    /***
     * Adds a rental to the CarRentalRepository with the given parameters.
     * @param cid Car ID
     * @param days Number of days
     * @param name Name of the customer
     * @return Returns the ID of the Rental
     * @throws ExceptionCode code 1, 2, 3, 4, 8
     */
    public int addRental(int cid, int days, String name) throws ExceptionCode {
        //Get the car if it exists
        //Code 4
        Car c = (Car)this.CR.get(cid);
        if(c.getTaken()) throw new ExceptionCode(8);
        //Bug here
        int id = this.CRR.getGreatestID()+1;
        CarRental cr = new CarRental(id,c,days,name);
        Validator.validateCarRental(cr);
        //Adding the rental to the repository
        //Code 1, 2
        this.CRR.add(id, cr);
        //Setting the car as taken
        Car aux = c;
        aux.setTaken(true);
        //Code 1, 3
        Validator.validateCar(aux);
        this.CR.modify((Integer)c.getID(),c,aux);
        return id;
    }

    /***
     * Gets the amount to pay for a rental
     * @param id ID of the rental
     * @return float with the amount to pay
     * @throws ExceptionCode code 4
     */
    public String getAmountToPay(int id) throws ExceptionCode {
        //Code 4
        CarRental c = (CarRental)this.CRR.get(id);
        //Some rounding, to limit it to 2 decimal places
        double roundDown = Math.round(c.getNumberOfDays()*c.getRentedCar().getCostPerDay()*100)/100;
        String aux = "You have to pay: " + String.valueOf(roundDown) + "$";
        return aux;
    }

    /***
     * Gets the String representation of a rental with the given ID
     * @param id ID of the rental
     * @return String representation of the rental
     * @throws ExceptionCode Code 4
     */
    public String getRentalString(int id) throws ExceptionCode {
        //Code 4
        CarRental c = (CarRental)this.CRR.get(id);
        return c.toString();
    }

    /***
     * Modifies the rental with the oldIndex with the new parameters.
     * @param oldIndex Index of the rental to be modified
     * @param newCar New car
     * @param newDays New number of days
     * @param newName New name of the customer
     * @throws ExceptionCode code 1, 3, 4
     */
    public void modifyRental(int oldIndex, int newCar, int newDays, String newName) throws ExceptionCode {
        //Get the new car
        //Code 4
        Car c = (Car)this.CR.get(newCar);

        if(c.getTaken()) throw new ExceptionCode(8);
        //Get the old rental
        //Code 3
        CarRental oldRental = (CarRental)this.CRR.get(oldIndex);

        //Get the old car and set it as free
        Car oldCar = oldRental.getRentedCar();
        oldCar.setTaken(false);

        c.setTaken(true);
        CarRental newRental = new CarRental(oldIndex,c, newDays,newName);

        Validator.validateCarRental(newRental);
        //Code 1, 3
        this.CRR.modify(oldIndex, oldRental, newRental);
    }

    /***
     * "Upscales" the entire CarRentalRepository by one. So now that the rental with the ID 3 has ID 4 and so on
     * @param index Index from which the upscale should start
     * @throws ExceptionCode code 3, 4
     */
    private void upscaleCarRentalIndex(int index) throws ExceptionCode {
        for(int i = index; i <= this.CRR.getSize(); i++)
        {
            //Code 4
            CarRental c = (CarRental) this.CRR.get(i);
            c.setID(i-1);
            //Code 1, 3
            this.CRR.modify(i, this.CRR.get(i), c);
        }
    }

    /***
     * Removes the rental with the given ID from the CarRentalRepository
     * @param index Index of the rental to be removed
     * @throws ExceptionCode code 1, 3, 4
     */
    public void removeCarRental(int index) throws ExceptionCode {
        //Code 1, 3, 4
        CarRental cr = (CarRental) this.CRR.get(index);
        Car oc = cr.getRentedCar();
        Car c = cr.getRentedCar();
        c.setTaken(false);
        this.CRR.remove(index, cr);
        this.CR.modify((Integer)oc.getID(),oc,c);
        //Code 3, 4
        this.upscaleCarRentalIndex(index);
    }

    //CarRental Filters
    public String getRentalsToBeReturned(int days) {
        Stream.Builder<CarRental> carRentalSb = Stream.builder();
        Iterable<CarRental> i = this.CRR.getValues();
        for(CarRental c : i)
        {
            carRentalSb.accept(c);
        }
        Stream<CarRental> carRentalS = carRentalSb.build();
        Iterator<CarRental> k = carRentalS.filter((c1) -> c1.getNumberOfDays() <= days).collect(Collectors.toList()).iterator();
        StringBuilder sb = new StringBuilder();
        while(k.hasNext()) {
            sb.append(k.next().toString()).append('\n');
        }
        return sb.toString();
    }


    //File IO
    public void stop() throws ExceptionCode {
        if(this.PH.getProperty("textOutput").equals("true")) {
            this.CRFH.saveCarRepository(this.CR, this.PH.getProperty("textCarsPath"), "txt");
            this.CRRFH.saveCarRentalRepository(this.CRR,this.PH.getProperty("textRentalsPath"), "txt");
        }
        if(this.PH.getProperty("binaryOutput").equals("true")) {
            this.CRFH.saveCarRepository(this.CR, this.PH.getProperty("binaryCarsPath"), "bin");
            this.CRRFH.saveCarRentalRepository(this.CRR,this.PH.getProperty("binaryRentalsPath"), "bin");
        }
        if(this.PH.getProperty("JSONOutput").equals("true")) {
            this.CRFH.saveCarRepository(this.CR, this.PH.getProperty("JSONCarsPath"), "JSON");
            this.CRRFH.saveCarRentalRepository(this.CRR,this.PH.getProperty("JSONRentalsPath"), "JSON");
        }
        if(this.PH.getProperty("XMLOutput").equals("true")) {
            this.CRFH.saveCarRepository(this.CR, this.PH.getProperty("XMLCarsPath"), "XML");
            this.CRRFH.saveCarRentalRepository(this.CRR,this.PH.getProperty("XMLRentalsPath"), "XML");
        }
        if(this.PH.getProperty("SQLOutput").equals("true")) {
            this.CRFH.saveToSQL(this.CR,this.PH.getProperty("SQLURL"),this.PH.getProperty("SQLUSER"),this.PH.getProperty("SQLPASS"));
            this.CRRFH.saveToSQL(this.CRR,this.PH.getProperty("SQLURL"),this.PH.getProperty("SQLUSER"),this.PH.getProperty("SQLPASS"));
        }
    }

    public void init()throws ExceptionCode{
        if(this.PH.getProperty("textInput").equals("true")) {
            this.CR = this.CRFH.loadCarRepository(this.PH.getProperty("textCarsPath"), "txt");
            this.CRR = this.CRRFH.loadCarRentalRepository(this.PH.getProperty("textRentalsPath"), "txt", this.CR);
        }
        if(this.PH.getProperty("binaryInput").equals("true")) {
            this.CR = this.CRFH.loadCarRepository(this.PH.getProperty("binaryCarsPath"), "bin");
            this.CRR = this.CRRFH.loadCarRentalRepository(this.PH.getProperty("binaryRentalsPath"), "bin", this.CR);
        }
        if(this.PH.getProperty("JSONInput").equals("true")) {
            this.CR = this.CRFH.loadCarRepository(this.PH.getProperty("JSONCarsPath"), "JSON");
            this.CRR = this.CRRFH.loadCarRentalRepository(this.PH.getProperty("JSONRentalsPath"), "JSON", this.CR);
        }
        if(this.PH.getProperty("XMLInput").equals("true")) {
            this.CR = this.CRFH.loadCarRepository(this.PH.getProperty("XMLCarsPath"),"XML");
            this.CRR = this.CRRFH.loadCarRentalRepository(this.PH.getProperty("XMLRentalsPath"),"XML", this.CR);
        }
        if(this.PH.getProperty("SQLInput").equals("true")){
            this.CR = this.CRFH.loadFromSQL(this.PH.getProperty("SQLURL"),this.PH.getProperty("SQLUSER"),this.PH.getProperty("SQLPASS"));
            this.CRR = this.CRRFH.loadFromSQL(this.PH.getProperty("SQLURL"),this.PH.getProperty("SQLUSER"),this.PH.getProperty("SQLPASS"), this.CR);
        }

    }

}
