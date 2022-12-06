package com.denistechs.carrentalgui3.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.denistechs.carrentalgui3.domain.Car;
import jakarta.xml.bind.annotation.XmlRootElement;


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
}
