package com.candlelightapps.stocknroll_backend.service.SpoonacularApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApiConfig {
    static Properties properties;

    public ApiConfig() throws IOException {

        properties = new Properties();


        String configFilePath= "config/apikey.properties";

        File ConfigFile=new File(configFilePath);

        FileInputStream configFileReader = new FileInputStream(ConfigFile);

        properties.load(configFileReader);

        configFileReader.close();
    }
    public static String getApiKey(){
        return properties.getProperty("API_KEY");
    }
}
