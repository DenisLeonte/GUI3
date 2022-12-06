package com.denistechs.carrentalgui3.service;

import com.denistechs.carrentalgui3.domain.Car;
import com.denistechs.carrentalgui3.domain.CarRental;

public class Validator {
    public Validator(){}

    public static void validateCar(Car car)
    {
        boolean errorTriggered = false;
        String errorMessage = new String();
        if(!Character.isUpperCase(car.getManufacturer().charAt(0)))
        {
            errorTriggered = true;
            errorMessage += "Manufacturer name must start with a capital letter\n";
        }
        if(!Character.isUpperCase(car.getModel().charAt(0)))
        {
            errorTriggered = true;
            errorMessage += "Model name must start with a capital letter\n";
        }
        if(car.getCostPerDay() < 0)
        {
            errorTriggered = true;
            errorMessage += "Cost per day must be a positive number\n";
        }
        if((Integer)car.getID() < 0)
        {
            errorTriggered = true;
            errorMessage += "ID must be a positive number\n";
        }
        if(car.getTaken() == null)
        {
            errorTriggered = true;
            errorMessage += "Taken must be a boolean\n";
        }
        if(errorTriggered)
        {
            throw new RuntimeException(errorMessage);
        }
    }

    public static void validateCarRental(CarRental carRental)
    {
        boolean errorTriggered = false;
        String errorMessage = new String();
        if(carRental.getNumberOfDays() < 0)
        {
            errorTriggered = true;
            errorMessage += "Days rented must be a positive number\n";
        }
        if((Integer)carRental.getID() < 0)
        {
            errorTriggered = true;
            errorMessage += "ID must be a positive number\n";
        }
        if(errorTriggered)
        {
            throw new RuntimeException(errorMessage);
        }
    }
}
