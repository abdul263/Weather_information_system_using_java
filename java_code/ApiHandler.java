package com.mycompany.mavenproject1;

import java.io.IOException;

// Interface for handling API requests
public interface ApiHandler {
    String getWeatherData(String apiUrl) throws IOException;
}
