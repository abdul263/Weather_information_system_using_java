package com.mycompany.mavenproject1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

// Handles API requests for weather data
public class WeatherApiHandler implements ApiHandler {
    public static final String API_KEY = "52ca0b29cf85492499e42049231511"; 
    public static final String BASE_URL = "http://api.weatherapi.com/v1";

    @Override
    public String getWeatherData(String apiUrl) throws IOException {
        // Connect to the API and retrieve data
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Read the response
        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }

        return response.toString();
    }
}
