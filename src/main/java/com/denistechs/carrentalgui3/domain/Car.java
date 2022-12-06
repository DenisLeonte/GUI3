package com.denistechs.carrentalgui3.domain;

import com.denistechs.carrentalgui3.service.ExceptionCode;

public class Car<Integer> extends Entity<Integer>{
    private String model, manufacturer;
    private double costPerDay;
    private Boolean isTaken;

    /***
     * Default constructor for Car
     */
    public Car() {
    }

    //All data checks must be done OUTSIDE the constructors, data should be checked only at setters

    /***
     * Constructor for Car
     * @param model The model of the car
     * @param manufacturer The manufacturer of the car
     * @param costPerDay The cost per day of the car
     * @param id The id of the car
     * @param isTaken The availability of the car
     */
    public Car(String model, String manufacturer, double costPerDay, Integer id, Boolean isTaken) {
        super(id);
        this.model = model;
        this.manufacturer = manufacturer;
        this.costPerDay = Math.round(costPerDay*1000)/1000;
        this.isTaken = isTaken;
    }

    /***
     * Checks if two Car entities are equal by their ID
     * @param o The object to be compared
     * @return True if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id;
    }

    /***
     * Returns the String representation of a Car object
     * @return String representation of a Car object
     */
    @Override
    public String toString() {
        String aux;
        try {
            aux = this.isTaken ? "occupied" : "free";
        }catch(NullPointerException ex)
        {
            return "";
        }
        return String.valueOf(this.id) + ". " + this.manufacturer + " " + this.model +
                ", " + this.costPerDay + "$ a day, " + aux;
    }

    /***
     * Returns the CSV format of the Car object
     * @return String representation of a Car object in CSV format
     */
    public String toCSV()
    {
        String aux = new String();
        aux += String.valueOf(this.id) + ",";
        aux += this.manufacturer + "," + this.model + ",";
        aux += String.valueOf(this.costPerDay) + ",";
        try {
            aux += this.isTaken ? String.valueOf(0) : String.valueOf(1);
        }catch(NullPointerException ex)
        {
            return "";
        }
        return aux;
    }

    /***
     * Getter for the model of the car
     * @return The model of the car
     */
    public String getModel() {
        return model;
    }

    /***
     * Setter for the model of the car
     * @param model The model of the car
     */
    public void setModel(String model){
        this.model = model;
    }

    /***
     * Getter for the manufacturer of the car
     * @return The manufacturer of the car
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /***
     * Setter for the manufacturer of the car
     * @param manufacturer The manufacturer of the car
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /***
     * Getter for the cost per day of the car
     * @return The cost per day of the car
     */
    public double getCostPerDay() {
        return costPerDay;
    }

    /***
     * Setter for the cost per day of the car
     * @param costPerDay The cost per day of the car
     * @throws ExceptionCode code 7
     */
    public void setCostPerDay(double costPerDay){
        this.costPerDay = costPerDay;
    }

    /***
     * Getter for the availability of the car
     * @return The availability of the car
     */
    public Boolean getTaken() {
        return isTaken;
    }

    /***
     * Setter for the availability of the car
     * @param taken The availability of the car
     */
    public void setTaken(Boolean taken) {
        isTaken = taken;
    }
}
