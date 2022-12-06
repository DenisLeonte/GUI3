package com.denistechs.carrentalgui3.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesHandler {
    Properties properties;

    public PropertiesHandler()
    {
        properties = new Properties();
    }

    public void init(String fileName)
    {
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(fileName);
            properties.load(fis);
            fis.close();
        }catch(IOException ex)
        {
            throw new RuntimeException("No properties file detected. Program will now quit");
        }
        this.inputCheck();
    }

    public void inputCheck()
    {
        int inputCount = 0; // 1 is correct value, anything else is wrong
        if(this.getProperty("textInput").equals("true"))
            inputCount++;
        if(this.getProperty("binaryInput").equals("true"))
            inputCount++;
        if(this.getProperty("JSONInput").equals("true"))
            inputCount++;
        if(this.getProperty("SQLInput").equals("true"))
            inputCount++;
        if(this.getProperty("XMLInput").equals("true"))
            inputCount++;

        if(inputCount != 1)
            throw new RuntimeException("Properties file is not configured correctly, configure input settings. Program will now quit");
    }

    public String getProperty(String key)
    {
        return properties.getProperty(key);
    }
}
