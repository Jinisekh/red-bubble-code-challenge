package com.jini.redbubble.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Utils {

    public static JsonNode readBasePriceFile(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(filePath);
            return objectMapper.readTree(file);
        }catch (IOException e){
            System.out.println("Oops was unable to read "+ filePath + ". Please make sure the file path and the content are correct.");
            return null;
        }catch (Exception e){
            System.out.println("Caught");
            return null;
        }
    }
}
