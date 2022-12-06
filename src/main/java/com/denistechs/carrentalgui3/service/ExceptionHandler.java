package com.denistechs.carrentalgui3.service;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExceptionHandler{
    ArrayList<String> arr;

    /***
     * Constructor for the Exception Handler
     */
    public ExceptionHandler() {
        arr = new ArrayList<String>();
    }

    /***
     * Initializes the Exception Handler from a file
     * @param fileName The name of the file to be read
     */
    public void init(String fileName){
        FileInputStream fis;
        try {
            fis = new FileInputStream(fileName);
        }catch(FileNotFoundException e)
        {
            //File doesn't exist
            throw new RuntimeException("File doesn't exist. Unable to load exception handler. Program will now exit.");
        }
        Scanner sc = new Scanner(fis);
        while(sc.hasNextLine())
        {
            this.arr.add(sc.nextLine());
        }
        sc.close();
    }

    /***
     * Gets the exception message from the array with the given index
     * @param code The index of the exception message
     * @return String with the exception message
     */
    private String get(int code) {
        if(code < 0 || code >= this.arr.size()) {
            throw new RuntimeException("Invalid exception code. Program will now exit.");
        }
        return this.arr.get(code);
    }

    /***
     * Method to handle exceptions
     * @param e The exception to be handled
     * @return The String representation of the exception
     */
    public String handle(ExceptionCode e)
    {
        return this.get(e.getCode() - 1);
    }

}
