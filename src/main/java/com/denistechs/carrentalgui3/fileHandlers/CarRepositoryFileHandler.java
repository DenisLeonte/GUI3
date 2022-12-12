package com.denistechs.carrentalgui3.fileHandlers;

import com.denistechs.carrentalgui3.domain.Car;
import com.denistechs.carrentalgui3.repository.CarRepository;
import com.denistechs.carrentalgui3.service.ExceptionCode;
import com.denistechs.carrentalgui3.service.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class CarRepositoryFileHandler {
    public CarRepositoryFileHandler() {}

    public CarRepository loadCarRepository(String fileName, String fileExtension){
        CarRepository CR = new CarRepository();
        switch(fileExtension)
        {
            case "txt" ->  CR = this.loadFromTxt(fileName);
            case "bin" -> CR = this.loadFromBin(fileName);
            case "JSON" -> CR = this.loadFromJSON(fileName);
            case "XML" -> CR = this.loadFromXML(fileName);
        }
        return CR;
    }

    private CarRepository loadFromTxt(String fileName){
        CarRepository CR = new CarRepository();

        FileInputStream fis;
        try {
            fis = new FileInputStream(fileName);
        }catch(FileNotFoundException e)
        {
            //No file found
            throw new RuntimeException("CarRepositoryFileHandler.loadFromTxt: File not found");
        }
        Scanner sc = new Scanner(fis);
        while(sc.hasNextLine())
        {
            //The CSV part
            String[] aux = sc.nextLine().split(",",0);
            int id = Integer.parseInt(aux[0]);
            String man = aux[1];
            String mod = aux[2];
            double ppd = Double.parseDouble(aux[3]);
            boolean t = Integer.parseInt(aux[4]) != 1;
            Car c = new Car(mod,man,ppd,id,t);
            try {
                //Code 1, 2
                Validator.validateCar(c);
                CR.add(id,c);
            }catch (ExceptionCode e)
            {
                System.out.println("Error with repository initialization, check the file for duplicates in ID");
            }catch (RuntimeException re)
            {
                System.out.println(re.getMessage());
            }
        }
        sc.close();
        return CR;
    }

    private CarRepository loadFromBin(String fileName){
        CarRepository CR = new CarRepository();
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try{
            fis = new FileInputStream(fileName);
            ois = new ObjectInputStream(fis);
            CR = (CarRepository) ois.readObject();
            ois.close();
        }catch(IOException | ClassNotFoundException e) {
            throw new RuntimeException("CarRepositoryFileHandler.loadFromBin: File not found");
        }
        return CR;
    }

    private CarRepository loadFromJSON(String fileName) {
        CarRepository CR = new CarRepository();
        try{
            ObjectMapper mapper = new ObjectMapper();
            CR = mapper.readValue(new File(fileName),CarRepository.class);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return CR;
    }

    private CarRepository loadFromXML(String fileName){
        CarRepository CR = new CarRepository();
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(CarRepository.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            CR = (CarRepository) unmarshaller.unmarshal(new File(fileName));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return CR;
    }

    public CarRepository loadFromSQL(String DB_URL, String USER, String PASS)
    {
        CarRepository CR = new CarRepository();
        try{
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Car");
            while(resultSet.next())
            {
                boolean taken = resultSet.getInt("isTaken")==1?true:false;
                Car c = new Car(resultSet.getString("Model"),resultSet.getString("Manufacturer"), resultSet.getFloat("PricePerDay"), resultSet.getInt("ID"),taken);
                Validator.validateCar(c);
                CR.add((int)c.getID(),c);
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        } catch (ExceptionCode e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e)
        {
            e.printStackTrace();
        }
        return CR;
    }

    public void saveCarRepository(CarRepository CR, String fileName, String fileExtension){
        switch(fileExtension)
        {
            case "txt" -> this.saveToTxt(CR,fileName);
            case "bin" -> this.saveToBin(CR,fileName);
            case "JSON" -> this.saveToJSON(CR,fileName);
            case "XML" -> this.saveToXML(CR,fileName);
        }
    }

    private void saveToTxt(CarRepository CR, String fileName){
        try {
            FileWriter fw = new FileWriter(fileName);
            Iterable<Car> i = CR.getValues();
            for (Car car : i) {
                fw.write(car.toCSV() + "\n");
            }
            fw.close();
        }catch (IOException ex)
        {
            //File doesn't exist
            throw new RuntimeException("CarRepositoryFileHandler.saveToTxt: File not found");
        }
    }

    private void saveToBin(CarRepository CR, String fileName){
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(CR);
            oos.flush();
            oos.close();
        }catch (IOException ex)
        {
            //File doesn't exist
            throw new RuntimeException("CarRepositoryFileHandler.saveToBin: File not found");
        }
    }

    private void saveToJSON(CarRepository CR, String fileName){
        try{
            FileWriter fw = new FileWriter(fileName);
            String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(CR);
            fw.write(json);
            fw.close();
        }catch (IOException ex)
        {
            //File doesn't exist
            ex.printStackTrace();
        }
    }

    private void saveToXML(CarRepository CR, String fileName){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CarRepository.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

            marshaller.marshal(CR,new File(fileName));

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void saveToSQL(CarRepository CR, String DB_URL, String USER, String PASS)
    {
        try{
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement statement = conn.createStatement();
            //Delete everything
            statement.executeUpdate("DROP TABLE Car;");
            statement.executeUpdate("CREATE TABLE Car(" +
                    "ID int PRIMARY KEY ," +
                    "Manufacturer VARCHAR(50)," +
                    "Model VARCHAR(50)," +
                    "PricePerDay FLOAT," +
                    "isTaken INT)");
            Iterable<Car> i = CR.getValues();
            for(Car c : i)
            {
                double aux = c.getCostPerDay()*100;
                aux = Math.round(aux);
                aux /= 100;
                String query = "INSERT INTO Car VALUES("+ c.getID().toString()+",'"+c.getManufacturer()+"','"+c.getModel()+
                "',"+String.valueOf(aux)+",";
                if(c.getTaken())
                {
                    query+="1);";
                }
                else{
                    query+="0);";
                }
                statement.executeUpdate(query);
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
