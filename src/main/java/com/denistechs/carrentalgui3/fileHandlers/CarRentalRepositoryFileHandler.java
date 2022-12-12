package com.denistechs.carrentalgui3.fileHandlers;

import com.denistechs.carrentalgui3.domain.Car;
import com.denistechs.carrentalgui3.domain.CarRental;
import com.denistechs.carrentalgui3.repository.CarRentalRepository;
import com.denistechs.carrentalgui3.repository.CarRepository;
import com.denistechs.carrentalgui3.service.ExceptionCode;
import com.denistechs.carrentalgui3.service.Validator;
import com.google.gson.Gson;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class CarRentalRepositoryFileHandler {
    public CarRentalRepositoryFileHandler() {}

    public void saveCarRentalRepository(CarRentalRepository CRR, String fileName, String fileExtension)
    {
        switch(fileExtension) {
            case "txt" -> this.saveToTxt(CRR, fileName);
            case "bin" -> this.saveToBin(CRR, fileName);
            case "JSON" -> this.saveToJSON(CRR, fileName);
            case "XML" -> this.saveToXML(CRR, fileName);
        }
    }

    private void saveToTxt(CarRentalRepository CRR, String fileName)
    {
        try {
            FileWriter fw = new FileWriter(fileName);
            Iterable<CarRental> i = CRR.getValues();
            for (CarRental cr : i) {
                fw.write(cr.toCSV() + "\n");
            }
            fw.close();
        }catch (IOException ex)
        {
            //File doesn't exist
            throw new RuntimeException("CarRentalRepositoryFileHandler.saveToTxt: File not found");
        }
    }

    private void saveToBin(CarRentalRepository CRR, String fileName)
    {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(CRR);
            oos.flush();
            oos.close();
        }catch (IOException ex)
        {
            //File doesn't exist
            throw new RuntimeException("CarRepositoryFileHandler.saveToBin: File not found");
        }
    }

    private void saveToJSON(CarRentalRepository CRR, String fileName){
        try{
            FileWriter fw = new FileWriter(fileName);
            String json = new Gson().toJson(CRR);
            fw.write(json);
            fw.close();
        }catch (IOException ex)
        {
            //File doesn't exist
            ex.printStackTrace();
        }
    }

    private void saveToXML(CarRentalRepository CRR, String fileName){
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(CarRentalRepository.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

            marshaller.marshal(CRR,new File(fileName));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveToSQL(CarRentalRepository CRR, String DB_URL, String USER, String PASS)
    {
        try{
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement statement = conn.createStatement();
            //Delete everything
            statement.executeUpdate("DROP TABLE Rental;");
            statement.executeUpdate("CREATE TABLE Rental(" +
                    "ID int PRIMARY KEY ," +
                    "Days int," +
                    "CarID int," +
                    "Name VARCHAR(50));");
            Iterable<CarRental> i = CRR.getValues();
            for(CarRental c : i)
            {
                String query = "INSERT INTO Rental VALUES(" + String.valueOf(c.getID()) + ","+String.valueOf(c.getNumberOfDays())+","+String.valueOf(c.getRentedCar().getID())+
                        ",'"+c.getRentalName()+"');";
                statement.executeUpdate(query);
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public CarRentalRepository loadCarRentalRepository(String fileName, String fileExtension, CarRepository CR)
    {
        CarRentalRepository CRR = new CarRentalRepository();
        switch(fileExtension)
        {
            case "txt" -> CRR = this.loadFromTxt(fileName, CR);
            case "bin" -> CRR = this.loadFromBin(fileName);
            case "JSON" -> CRR = this.loadFromJSON(fileName);
            case "XML" -> CRR = this.loadFromXML(fileName);
        }
        return CRR;
    }

    private CarRentalRepository loadFromTxt(String fileName, CarRepository CR){
        CarRentalRepository CRR = new CarRentalRepository();
        FileInputStream fis;
        try {
            fis = new FileInputStream(fileName);
        }catch(IOException e) {
            //File doesn't exist
            System.out.println("File doesn't exist. Unable to load Car Rental repository");
            return CRR;
        }
        Scanner sc = new Scanner(fis);
        while(sc.hasNextLine())
        {
            //The CSV part
            String[] aux = sc.nextLine().split(",",0);
            int id = Integer.parseInt(aux[0]);
            int cid = Integer.parseInt(aux[1]);
            int noOfDays = Integer.parseInt(aux[2]);
            String name = aux[3];
            try {
                //Trying to add rental with Car information from the Car Repository
                CarRental c = new CarRental(id,(Car)CR.get(cid),noOfDays,name);
                Validator.validateCarRental(c);
                CRR.add(id,c);
            }catch (ExceptionCode e) {
                //Duped ID
                //Duped Car
                System.out.println("Error with Car Rental repository initialization. There may be car rental duplicates or ID duplicates");
            }catch (RuntimeException re)
            {
                System.out.println(re.getMessage());
            }
        }
        sc.close();
        return CRR;
    }

    private CarRentalRepository loadFromBin(String fileName){
        CarRentalRepository CRR = new CarRentalRepository();
        FileInputStream fis;
        ObjectInputStream ois;
        try {
            fis = new FileInputStream(fileName);
            ois = new ObjectInputStream(fis);
            CRR = (CarRentalRepository) ois.readObject();
            ois.close();
        }catch(IOException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        return CRR;
    }

    private CarRentalRepository loadFromJSON(String fileName){
        CarRentalRepository CRR = new CarRentalRepository();
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(fileName);
            Scanner sc = new Scanner(fis);
            if(sc.hasNextLine())
            {
                String json = sc.nextLine();
                Gson gson = new Gson();
                CRR = gson.fromJson(json,CarRentalRepository.class);
            }
        }catch (FileNotFoundException e)
        {
            throw new RuntimeException("CarRentalRepositoryFileHandler.loadFromJSON: File not found");
        }
        return CRR;
    }

    private CarRentalRepository loadFromXML(String fileName) {
        CarRentalRepository CRR = new CarRentalRepository();
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(CarRentalRepository.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            CRR =(CarRentalRepository) unmarshaller.unmarshal(new File(fileName));
        }catch(Exception e) {
            e.printStackTrace();
        }
        return CRR;
    }

    public CarRentalRepository loadFromSQL(String DB_URL, String USER, String PASS, CarRepository CR)
    {
        CarRentalRepository CRR = new CarRentalRepository();
        try{
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Rental");
            while(resultSet.next())
            {
                int ID = resultSet.getInt("ID");
                int Days = resultSet.getInt("Days");
                int CarID = resultSet.getInt("CarID");
                String Name = resultSet.getString("Name");
                Car c = CR.get(CarID);
                CarRental cr = new CarRental(ID, c, Days, Name);
                Validator.validateCarRental(cr);
                CRR.add(ID, cr);
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
        return CRR;
    }
}
