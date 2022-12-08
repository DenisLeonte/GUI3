package com.denistechs.carrentalgui3.util;

import com.denistechs.carrentalgui3.repository.CarRentalRepository;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;

public class CarRentalRepositoryAdapter {
    /*public CarRentalRepository read(JsonReader reader) throws IOException {
        CarRentalRepository CRR = new CarRentalRepository();
        reader.beginObject();
        String fieldName = null;

        while(reader.hasNext()){
            JsonToken token = reader.peek();
            if(token.equals(JsonToken.NAME)){
                fieldName = reader.nextName();
            }

            if("repo".equals(fieldName)){
                reader.beginArray();
            }
        }
    }*/
}
