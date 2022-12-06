package com.denistechs.carrentalgui3.domain;

public class CarRental extends Entity<Integer>{
    Car rentedCar;
    int numberOfDays;
    String rentalName;

    public CarRental(){}

    /***
     * Default constructor for CarRental
     * @param id The id of the rental
     * @param c The car to be rented
     * @param numberOfDays The number of days the car is rented
     * @param rentalName The name of the customer
     */
    public CarRental(Integer id,Car c, int numberOfDays,String rentalName) {
        super(id);
        this.rentedCar = c;
        this.numberOfDays = numberOfDays;
        this.rentalName = rentalName;
    }

    /***
     * Returns the String representation of a CarRental object
     * @return String representation of a CarRental object
     */
    @Override
    public String toString() {
        //Rounding to 2 decimal places
        double roundDown = Math.round(this.numberOfDays*this.rentedCar.getCostPerDay()*100)/100;
        return String.valueOf(this.id) + ", Rental, " + this.rentedCar.getManufacturer() + " " + this.rentedCar.getModel() +
                ", total price: " + String.valueOf(roundDown) + "$, rented by " + this.rentalName + ", " + String.valueOf(this.numberOfDays) + " days left";
    }

    /***
     * CSV representation of the CarRental object
     * @return String representation
     */
    public String toCSV(){
        return String.valueOf(this.id)+","+String.valueOf(this.rentedCar.getID())+","+String.valueOf(this.numberOfDays)+","
                + this.rentalName;
    }

    /***
     * Checks if two CarRental entities are equal by their ID
     * @param o The object to be compared
     * @return True if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarRental carRental = (CarRental) o;
        return this.id == carRental.id;
    }

    /***
     * Returns the rented Car
     * @return The rented Car
     */
    public Car getRentedCar() {
        return rentedCar;
    }

    /***
     * Sets the rented Car
     * @param rentedCar The rented Car
     */
    public void setRentedCar(Car rentedCar) {
        this.rentedCar = rentedCar;
    }

    /***
     * Returns the number of days the car is rented
     * @return The number of days the car is rented
     */
    public int getNumberOfDays() {
        return numberOfDays;
    }

    /***
     * Sets the number of days the car is rented
     * @param numberOfDays The number of days the car is rented
     */
    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    /***
     * Returns the name of the customer
     * @return The name of the customer
     */
    public String getRentalName() {
        return rentalName;
    }

    /***
     * Sets the name of the customer
     * @param rentalName The name of the customer
     */
    public void setRentalName(String rentalName) {
        this.rentalName = rentalName;
    }
}
