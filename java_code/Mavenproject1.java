package com.mycompany.mavenproject1;

import javax.swing.*;

public class Mavenproject1 {
    public static void main(String[] args) {
        // Run the WeatherApp on the Swing event dispatch thread
        SwingUtilities.invokeLater(() -> {
            WeatherApp weatherApp = new WeatherApp();
            weatherApp.start();
        });
    }
}
