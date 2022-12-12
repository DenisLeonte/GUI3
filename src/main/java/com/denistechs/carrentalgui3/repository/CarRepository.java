package com.denistechs.carrentalgui3.repository;

import com.denistechs.carrentalgui3.domain.Car;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.HashMap;
import java.util.Map;


@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarRepository extends MemoryRepository<Integer, Car>{
    /***
     * Default constructor for the Car Repository
     */
    public CarRepository() {

    }

    /***
     * Returns the size of the Car Repository
     * @return int with the size
     */
    public int getSize() {
        return this.repo.size();
    }

    /***
     * Returns the list with all the Cars that are available
     * @return String
     */
    public String listAvailableCars() {
        //Arguably StringBuilder is better than a String in this situation
        StringBuilder sb = new StringBuilder();
        Iterable<Car> i = this.repo.values();
        for(Car c : i)
        {
            if(!c.getTaken())
            {
                //Equivalent to s += c.toString() + "\n";
                sb.append(c.toString()).append("\n");
            }
        }
        return sb.toString();
    }

    public HashMap<String, Integer> getAllManufacturers(){
        Map<String, Integer> m = new HashMap<>();
        for(Car c : this.repo.values())
        {
            if(m.containsKey(c.getManufacturer()))
            {
                m.put(c.getManufacturer(), m.get(c.getManufacturer()) + 1);
            }
            else
            {
                m.put(c.getManufacturer(), 1);
            }
        }
        return (HashMap<String, Integer>) m;
    }

    public double getMinPrice(){
        double min = Double.MAX_VALUE;
        for(Car c : this.repo.values())
        {
            if(c.getCostPerDay() <= min)
            {
                min = c.getCostPerDay();
            }
        }
        return min;
    }

    public double getMaxPrice(){
        double max = Double.MIN_VALUE;
        for(Car c : this.repo.values())
        {
            if(c.getCostPerDay() >= max)
            {
                max = c.getCostPerDay();
            }
        }
        return max;
    }

}
