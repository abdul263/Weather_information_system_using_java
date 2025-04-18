package com.mycompany.mavenproject1;

import javax.swing.*;
import java.io.IOException;
import java.net.URLEncoder;

// Main class for the Weather Information System
public class WeatherApp {
    private final WeatherApiHandler weatherApiHandler;
    private final WeatherParser weatherParser;

    public WeatherApp() {
        this.weatherApiHandler = new WeatherApiHandler();
        this.weatherParser = new WeatherParser();
    }

    // Start the WeatherApp
    public void start() {
        WeatherGUI.createAndShowGUI(new WeatherGUI.WeatherHandler() {
            // Show weather information for the default location
            @Override
            public void showWeather(JTextArea resultTextArea) {
                try {
                    // Construct API URLs for current weather and forecast
                    String currentWeatherApiUrl = String.format("%s/current.json?key=%s&q=%s", WeatherApiHandler.BASE_URL, WeatherApiHandler.API_KEY, WeatherAppConstants.DEFAULT_LOCATION);
                    String forecastApiUrl = String.format("%s/forecast.json?key=%s&q=%s&days=3", WeatherApiHandler.BASE_URL, WeatherApiHandler.API_KEY, WeatherAppConstants.DEFAULT_LOCATION);

                    // Fetch weather data and display in the GUI
                    String currentWeatherJson = weatherApiHandler.getWeatherData(currentWeatherApiUrl);
                    String forecastJson = weatherApiHandler.getWeatherData(forecastApiUrl);

                    String weatherInfo = "Current Location: " + WeatherAppConstants.DEFAULT_LOCATION + "\n" +
                            "Weather Information:\n" +
                            weatherParser.parseCurrentWeatherInfo(currentWeatherJson) +
                            "\nForecast Information:\n" +
                            weatherParser.parseForecastInfo(forecastJson);

                    resultTextArea.setText(weatherInfo);
                } catch (IOException ex) {
                    handleWeatherError(resultTextArea);
                }
            }

            // Check weather information for a specified city
            @Override
            public void checkWeather(String city, JTextArea resultTextArea, JLabel currentLocationLabel) {
                try {
                    WeatherAppConstants.DEFAULT_LOCATION = city;  // Update default location
                    currentLocationLabel.setText("Current Location: " + WeatherAppConstants.DEFAULT_LOCATION);

                    String encodedCity = URLEncoder.encode(city, "UTF-8");
                    String currentWeatherApiUrl = String.format("%s/current.json?key=%s&q=%s", WeatherApiHandler.BASE_URL, WeatherApiHandler.API_KEY, encodedCity);
                    String forecastApiUrl = String.format("%s/forecast.json?key=%s&q=%s&days=3", WeatherApiHandler.BASE_URL, WeatherApiHandler.API_KEY, encodedCity);

                    // Fetch weather data for the specified city and display in the GUI
                    String currentWeatherJson = weatherApiHandler.getWeatherData(currentWeatherApiUrl);
                    String forecastJson = weatherApiHandler.getWeatherData(forecastApiUrl);

                    String weatherInfo = "Current Location: " + WeatherAppConstants.DEFAULT_LOCATION + "\n" +
                            "Weather Information:\n" +
                            weatherParser.parseCurrentWeatherInfo(currentWeatherJson) +
                            "\nForecast Information:\n" +
                            weatherParser.parseForecastInfo(forecastJson);

                    resultTextArea.setText(weatherInfo);
                } catch (IOException ex) {
                    handleWeatherError(resultTextArea);
                }
            }

            // Handle errors during weather data retrieval
            private void handleWeatherError(JTextArea resultTextArea) {
                resultTextArea.setText("Failed to retrieve weather data. Please try again.");
            }
        });
    }
}
