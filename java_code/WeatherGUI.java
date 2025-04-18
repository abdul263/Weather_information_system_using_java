package com.mycompany.mavenproject1;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The WeatherGUI class is responsible for creating and displaying the graphical user interface
 * of the Weather Information System application. It provides a user-friendly interface for users
 * to view weather information for a default location or check weather information for a specific city.
 */
public class WeatherGUI {
    private static final Logger LOGGER = Logger.getLogger(WeatherGUI.class.getName());
    private static final String API_KEY = "52ca0b29cf85492499e42049231511";
    private static final String BASE_URL = "http://api.weatherapi.com/v1";

    // The default location for weather information
    private static String currentLocation = "Bangalore";

    /**
     * Creates and displays the main GUI for the Weather Information System.
     * This GUI includes components for displaying current weather information, allowing users
     * to check weather for other locations, and providing a responsive user interface.
     *
     * @param weatherHandler An implementation of the WeatherHandler interface for handling weather-related actions.
     */
    public static void createAndShowGUI(WeatherHandler weatherHandler) {
        JFrame frame = new JFrame("Weather Information System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(173, 216, 230)); // Light Blue background

        // Title
        JLabel titleLabel = new JLabel("Weather Information System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204)); // Dark Blue text color
        frame.add(titleLabel, BorderLayout.NORTH);

        // Panel for displaying current location and weather details
        JPanel currentLocationPanel = new JPanel(new BorderLayout());
        currentLocationPanel.setBackground(new Color(240, 248, 255)); // Alice Blue background

        JLabel currentLocationLabel = new JLabel("Current Location: " + currentLocation, SwingConstants.CENTER);
        currentLocationLabel.setFont(new Font("Arial", Font.BOLD, 18));
        currentLocationPanel.add(currentLocationLabel, BorderLayout.NORTH);

        JTextArea resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        currentLocationPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(currentLocationPanel, BorderLayout.CENTER);

        // Panel for checking other locations
        JPanel checkOtherLocationPanel = new JPanel();
        checkOtherLocationPanel.setLayout(new BoxLayout(checkOtherLocationPanel, BoxLayout.Y_AXIS));
        checkOtherLocationPanel.setBackground(new Color(240, 248, 255)); // Alice Blue background

        JLabel checkLocationLabel = new JLabel("Check Other Locations");
        checkLocationLabel.setFont(new Font("Arial", Font.BOLD, 16));
        checkOtherLocationPanel.add(checkLocationLabel);

        JTextField cityTextField = new JTextField(15);
        cityTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        checkOtherLocationPanel.add(cityTextField);

        JButton checkWeatherButton = new JButton("Check Weather");
        checkWeatherButton.setFont(new Font("Arial", Font.BOLD, 16));
        checkWeatherButton.setBackground(new Color(0, 102, 204));
        checkWeatherButton.setForeground(Color.WHITE);
        checkWeatherButton.addActionListener(e -> weatherHandler.checkWeather(cityTextField.getText(), resultTextArea, currentLocationLabel));
        checkOtherLocationPanel.add(checkWeatherButton);

        frame.add(checkOtherLocationPanel, BorderLayout.SOUTH);

        // Show weather for default location (Bangalore)
        weatherHandler.showWeather(resultTextArea);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Displays the weather information for the default location.
     * This method fetches the weather data for the default location, parses it, and updates
     * the GUI components with the relevant information.
     *
     * @param resultTextArea The text area to display the weather information.
     * @param weatherApiHandler The API handler for fetching weather data.
     * @param weatherParser The parser for extracting information from the weather data.
     */
    public static void showWeatherForCurrentLocation(
        JTextArea resultTextArea, WeatherApiHandler weatherApiHandler, WeatherParser weatherParser) {
    try {
        String currentWeatherApiUrl = String.format("%s/current.json?key=%s&q=%s", BASE_URL, API_KEY, currentLocation);
        String forecastApiUrl = String.format("%s/forecast.json?key=%s&q=%s&days=3", BASE_URL, API_KEY, currentLocation);

        String currentWeatherJson = weatherApiHandler.getWeatherData(currentWeatherApiUrl);
        String forecastJson = weatherApiHandler.getWeatherData(forecastApiUrl);

        String currentWeatherInfo = "Current Location: " + currentLocation + "\n" +
                "Weather Information:\n" +
                weatherParser.parseCurrentWeatherInfo(currentWeatherJson);

        String forecastInfo = "\nForecast Information:\n" +
                weatherParser.parseForecastInfo(forecastJson);

        resultTextArea.setText(currentWeatherInfo + forecastInfo);
    } catch (IOException ex) {
        handleWeatherError(resultTextArea);
    }
    }

    /**
     * Checks and displays weather information for the specified city.
     * This method allows users to input a city, fetch weather data for that city, parse the data,
     * and update the GUI components with the relevant information.
     *
     * @param city The city for which weather information is to be checked.
     * @param resultTextArea The text area to display the weather information.
     * @param currentLocationLabel The label displaying the current location.
     * @param weatherApiHandler The API handler for fetching weather data.
     * @param weatherParser The parser for extracting information from the weather data.
     */
    public static void checkWeatherForCity(
            String city, JTextArea resultTextArea, JLabel currentLocationLabel,
            WeatherApiHandler weatherApiHandler, WeatherParser weatherParser) {
        try {
            currentLocation = city;  // Update current location
            currentLocationLabel.setText("Current Location: " + currentLocation);

            String encodedCity = URLEncoder.encode(city, "UTF-8");
            String currentWeatherApiUrl = String.format("%s/current.json?key=%s&q=%s", BASE_URL, API_KEY, encodedCity);
            String forecastApiUrl = String.format("%s/forecast.json?key=%s&q=%s&days=3", BASE_URL, API_KEY, encodedCity);

            String currentWeatherJson = weatherApiHandler.getWeatherData(currentWeatherApiUrl);
            String forecastJson = weatherApiHandler.getWeatherData(forecastApiUrl);

            String weatherInfo = "Current Location: " + currentLocation + "\n" +
                    "Weather Information:\n" +
                    weatherParser.parseCurrentWeatherInfo(currentWeatherJson) +
                    "\nForecast Information:\n" +
                    weatherParser.parseForecastInfo(forecastJson);

            resultTextArea.setText(weatherInfo);
        } catch (IOException ex) {
            handleWeatherError(resultTextArea);
        }
    }

    /**
     * Handles errors that occur during weather data retrieval.
     * This method displays an error message in the text area when an error occurs
     * while fetching weather data from the API.
     *
     * @param resultTextArea The text area to display the error message.
     */
    private static void handleWeatherError(JTextArea resultTextArea) {
        resultTextArea.setText("Failed to retrieve weather data. Please try again.");
    }

    /**
     * The WeatherHandler interface defines methods for handling weather-related actions in the GUI.
     * Implementations of this interface can customize the behavior of the WeatherGUI class by providing
     * their own logic for showing weather and checking weather for specific locations.
     */
    public interface WeatherHandler {
        /**
         * Displays the weather information for the default location.
         *
         * @param resultTextArea The text area to display the weather information.
         */
        void showWeather(JTextArea resultTextArea);

        /**
         * Checks and displays weather information for the specified city.
         *
         * @param city The city for which weather information is to be checked.
         * @param resultTextArea The text area to display the weather information.
         * @param currentLocationLabel The label displaying the current location.
         */
        void checkWeather(String city, JTextArea resultTextArea, JLabel currentLocationLabel);
    }
}
