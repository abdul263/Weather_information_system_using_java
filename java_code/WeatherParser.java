package com.mycompany.mavenproject1;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The WeatherParser class is responsible for parsing JSON responses from the weather API.
 * It extracts relevant weather information, including current weather conditions, forecast
 * details for multiple days, and hourly forecast information.
 */
public class WeatherParser {
    private static final Logger LOGGER = Logger.getLogger(WeatherParser.class.getName());

    /**
     * A nested class representing different types of weather information.
     * This class includes fields for day information, astronomy information, and hourly information.
     * It overrides the toString method to concatenate the information for easy display.
     */
    public static class WeatherInfo {
        private String date;
        private String dayInfo;
        private String astroInfo;
        private String hourlyInfo;

        /**
         * Gets the date of the weather information.
         *
         * @return The date of the weather information.
         */
        public String getDate() {
            return date;
        }

        /**
         * Sets the date of the weather information.
         *
         * @param date The date to set.
         */
        public void setDate(String date) {
            this.date = date;
        }

        /**
         * Returns a string representation of the weather information.
         *
         * @return A string representation of the weather information.
         */
        @Override
        public String toString() {
            return "Date: " + date + "\nDay Information:\n" + dayInfo + "\nAstronomy Information:\n" + astroInfo + "\nHourly Information:\n" + hourlyInfo;
        }
    }

    /**
     * Parses the current weather information from the JSON response.
     * Extracts relevant details such as condition text, temperature, and wind speed.
     *
     * @param jsonResponse The JSON response from the weather API.
     * @return A formatted string containing the parsed current weather information.
     */

    public String parseCurrentWeatherInfo(String jsonResponse) {
        StringBuilder result = new StringBuilder();

        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject weatherData = jsonParser.parse(jsonResponse).getAsJsonObject();

            JsonObject current = weatherData.getAsJsonObject("current");
            String conditionText = current.getAsJsonObject("condition").get("text").getAsString();
            double temperatureC = current.get("temp_c").getAsDouble();
            double temperatureF = current.get("temp_f").getAsDouble();
            double windSpeedKph = current.get("wind_kph").getAsDouble();
            double windSpeedMph = current.get("wind_mph").getAsDouble();

            result.append("Current Weather: ").append(conditionText).append("\n");
            result.append("Temperature: ").append(temperatureC).append("°C / ").append(temperatureF).append("°F\n");
            result.append("Wind Speed: ").append(windSpeedKph).append(" kph / ").append(windSpeedMph).append(" mph\n");

            result.append("\n");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error occurred during parsing current weather data", e);
            result.append("Error occurred during parsing current weather data. Exception: ").append(e.getMessage()).append("\n");
        }

        return result.toString();
    }
    
    /**
     * Parses the forecast information from the JSON response.
     * Extracts details for multiple days, including day information, astronomy details, and hourly information.
     *
     * @param jsonResponse The JSON response from the weather API.
     * @return A formatted string containing the parsed forecast information.
     */
   
    public String parseForecastInfo(String jsonResponse) {
        StringBuilder result = new StringBuilder();

        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject forecastData = jsonParser.parse(jsonResponse).getAsJsonObject();

            if (forecastData.has("forecast") && forecastData.getAsJsonObject("forecast").has("forecastday")) {
                JsonArray forecastDays = forecastData.getAsJsonObject("forecast").getAsJsonArray("forecastday");

                if (forecastDays.size() > 0) {
                    List<WeatherInfo> forecastInfoList = new ArrayList<>();

                    for (JsonElement element : forecastDays) {
                        JsonObject forecastDay = element.getAsJsonObject();

                        WeatherInfo weatherInfo = new WeatherInfo();
                        weatherInfo.setDate(forecastDay.get("date").getAsString());

                        JsonObject day = forecastDay.getAsJsonObject("day");
                        if (day != null) {
                            weatherInfo.dayInfo = parseDayInfo(day);
                        }

                        JsonObject astro = forecastDay.getAsJsonObject("astro");
                        if (astro != null) {
                            weatherInfo.astroInfo = parseAstroInfo(astro);
                        }

                        JsonArray hourlyArray = forecastDay.getAsJsonArray("hour");
                        if (hourlyArray != null) {
                            weatherInfo.hourlyInfo = parseHourlyInfo(hourlyArray);
                        }

                        forecastInfoList.add(weatherInfo);
                    }

                    for (WeatherInfo weatherInfo : forecastInfoList) {
                        result.append(weatherInfo.toString()).append("\n----------------------------\n");
                    }
                } else {
                    result.append("No forecast information available.\n");
                }
            } else {
                result.append("No forecastday information available in the response.\n");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error occurred during parsing forecast data", e);
            result.append("Error occurred during parsing forecast data. Exception: ").append(e.getMessage()).append("\n");
        }

        return result.toString();
    }
    
    /**
     * Parses the day information from the JSON object.
     * Extracts details such as maximum and minimum temperatures.
     *
     * @param day The JSON object containing day information.
     * @return A formatted string containing the parsed day information.
     */

    private String parseDayInfo(JsonObject day) {
        StringBuilder result = new StringBuilder();

        JsonElement maxTempCElement = day.get("maxtemp_c");
        JsonElement minTempCElement = day.get("mintemp_c");

        if (maxTempCElement != null && minTempCElement != null) {
            double maxTempC = maxTempCElement.getAsDouble();
            double minTempC = minTempCElement.getAsDouble();

            result.append("Max Temperature: ").append(maxTempC).append("°C\n");
            result.append("Min Temperature: ").append(minTempC).append("°C\n");
        } else {
            result.append("Max/Min Temperature information not available.\n");
        }

        return result.toString();
    }
    
    /**
     * Parses the astronomy information from the JSON object.
     * Extracts details such as sunrise and sunset times.
     *
     * @param astro The JSON object containing astronomy information.
     * @return A formatted string containing the parsed astronomy information.
     */

    private String parseAstroInfo(JsonObject astro) {
        StringBuilder result = new StringBuilder();

        result.append("Sunrise: ").append(astro.get("sunrise").getAsString()).append("\n");
        result.append("Sunset: ").append(astro.get("sunset").getAsString()).append("\n");

        return result.toString();
    }
    
    /**
     * Parses the hourly information from the JSON array.
     * Extracts details such as time, temperature, condition, and wind speed for each hour.
     *
     * @param hourlyArray The JSON array containing hourly information.
     * @return A formatted string containing the parsed hourly information.
     */

    private String parseHourlyInfo(JsonArray hourlyArray) {
        StringBuilder result = new StringBuilder();

        for (JsonElement element : hourlyArray) {
            JsonObject hour = element.getAsJsonObject();
            String time = hour.get("time").getAsString();
            double temperatureC = hour.get("temp_c").getAsDouble();
            String conditionText = hour.getAsJsonObject("condition").get("text").getAsString();
            double windSpeedKph = hour.get("wind_kph").getAsDouble();

            String formattedLine = String.format("Time: %-25sTemperature: %-30sCondition: %-40sWind Speed: %-35s",
                    time, temperatureC + "°C", conditionText, windSpeedKph + " kph");

            result.append(formattedLine).append("\n");
        }

        return result.toString();
    }
}
