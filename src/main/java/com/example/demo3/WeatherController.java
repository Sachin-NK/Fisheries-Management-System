package com.example.demo3;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WeatherController {

    // Location and coordinates fields
    @FXML
    private TextField searchLocationField;
    @FXML
    private TextField latitudeField;
    @FXML
    private TextField longitudeField;
    @FXML
    private HBox cardContainer;
    @FXML
    private LineChart<String, Number> weatherChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    // Current weather card elements
    @FXML
    private Label currentConditionLabel;
    @FXML
    private Label currentTempLabel;
    @FXML
    private Label windSpeedLabel;
    @FXML
    private Label waveHeightLabel;
    @FXML
    private Label visibilityLabel;
    @FXML
    private Label seaTempLabel;

    // Safety status card elements
    @FXML
    private Label safetyIconLabel;
    @FXML
    private Label safetyStatusLabel;
    @FXML
    private Label waveConditionStatus;
    @FXML
    private Label windSpeedStatus;
    @FXML
    private Label visibilityStatus;

    // Bottom metrics elements
    @FXML
    private Label tideStatusLabel;
    @FXML
    private Label uvIndexLabel;
    @FXML
    private Label currentHumidityLabel;
    @FXML
    private Label pressureLabel;

    // Real-time update components
    private Timeline realTimeUpdater;
    private Timeline alertChecker;
    private Random random = new Random();
    private Label statusLabel;
    private VBox alertContainer;

    // Weather thresholds for alerts
    private static final double WAVE_ALERT_THRESHOLD = 3.0; // meters
    private static final double RAIN_ALERT_THRESHOLD = 10.0; // mm
    private static final double WIND_WAVE_ALERT_THRESHOLD = 2.5; // meters

    // Current weather data for real-time updates
    private double currentWaveHeight = 0.0;
    private double currentRainfall = 0.0;
    private double currentWindWave = 0.0;
    private String currentCondition = "Clear";

    // Sample weather conditions for simulation
    private final String[] weatherConditions = {
            "Clear", "Partly Cloudy", "Cloudy", "Light Rain", "Heavy Rain",
            "Thunderstorm", "Windy", "Calm", "Rough Seas"
    };

    private final String[] weatherAlerts = {
            "⚠️ High waves detected - Exercise caution",
            "🌧️ Heavy rainfall expected - Check equipment",
            "💨 Strong winds forecasted - Consider postponing trips",
            "⛈️ Thunderstorm approaching - Return to harbor",
            "🌊 Rough sea conditions - Small boats stay ashore",
            "☀️ Excellent fishing conditions - Perfect weather",
            "🌤️ Mild weather conditions - Good for fishing"
    };

    @FXML
    public void initialize() {
        latitudeField.setText("6.05");
        longitudeField.setText("80.21");

        // Create status label for real-time updates
        createStatusLabel();

        // Get initial weather data
        getWeatherData();

        // Start real-time updates
        startRealTimeUpdates();
    }

    @FXML
    public void selectFishingArea(javafx.event.ActionEvent event) {
        javafx.scene.control.Button button = (javafx.scene.control.Button) event.getSource();
        String areaName = button.getText();

        // Set coordinates based on fishing area
        switch (areaName) {
            case "Negombo":
                latitudeField.setText("7.21");
                longitudeField.setText("79.84");
                if (searchLocationField != null)
                    searchLocationField.setText("Negombo, Sri Lanka");
                break;
            case "Chilaw":
                latitudeField.setText("7.58");
                longitudeField.setText("79.80");
                if (searchLocationField != null)
                    searchLocationField.setText("Chilaw, Sri Lanka");
                break;
            case "Trincomalee":
                latitudeField.setText("8.59");
                longitudeField.setText("81.21");
                if (searchLocationField != null)
                    searchLocationField.setText("Trincomalee, Sri Lanka");
                break;
            case "Galle":
                latitudeField.setText("6.03");
                longitudeField.setText("80.22");
                if (searchLocationField != null)
                    searchLocationField.setText("Galle, Sri Lanka");
                break;
            case "Mirissa":
                latitudeField.setText("5.95");
                longitudeField.setText("80.46");
                if (searchLocationField != null)
                    searchLocationField.setText("Mirissa, Sri Lanka");
                break;
            case "Kalpitiya":
                latitudeField.setText("8.23");
                longitudeField.setText("79.77");
                if (searchLocationField != null)
                    searchLocationField.setText("Kalpitiya, Sri Lanka");
                break;
        }

        // Update weather data for the selected area
        getWeatherData();
    }

    @FXML
    public void getWeatherData() {
        String lat = latitudeField.getText();
        String lon = longitudeField.getText();

        try {
            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(lon);

            // Marine and Rain API URLs
            String marineUrl = String.format(
                    "https://marine-api.open-meteo.com/v1/marine?latitude=%.2f&longitude=%.2f&daily=wave_height_max,wind_wave_height_max&timezone=auto",
                    latitude, longitude);
            String rainUrl = String.format(
                    "https://api.open-meteo.com/v1/forecast?latitude=%.2f&longitude=%.2f&daily=precipitation_sum&timezone=auto",
                    latitude, longitude);

            // Fetch data
            JSONObject marineJson = fetchJsonFromUrl(marineUrl);
            JSONObject rainJson = fetchJsonFromUrl(rainUrl);

            parseWeatherData(marineJson, rainJson);
        } catch (Exception e) {
            showError("❌ API or input error:\n" + e.getMessage());
        }
    }

    private JSONObject fetchJsonFromUrl(String urlStr) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        return new JSONObject(response.toString());
    }

    private void parseWeatherData(JSONObject marine, JSONObject rain) {
        JSONArray dates = marine.getJSONObject("daily").getJSONArray("time");
        JSONArray waves = marine.getJSONObject("daily").getJSONArray("wave_height_max");
        JSONArray windWaves = marine.getJSONObject("daily").getJSONArray("wind_wave_height_max");

        JSONArray rainDates = rain.getJSONObject("daily").getJSONArray("time");
        JSONArray rainVals = rain.getJSONObject("daily").getJSONArray("precipitation_sum");

        Map<String, Double> rainMap = new HashMap<>();
        for (int i = 0; i < rainDates.length(); i++) {
            rainMap.put(rainDates.getString(i), rainVals.getDouble(i));
        }

        System.out.println("Wave Days: " + dates.length() + ", Rain Days: " + rainDates.length());

        cardContainer.getChildren().clear();
        weatherChart.getData().clear();

        Label title = new Label("📊 Weather Summary");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        XYChart.Series<String, Number> waveSeries = new XYChart.Series<>();
        waveSeries.setName("Wave Height (m)");

        XYChart.Series<String, Number> rainSeries = new XYChart.Series<>();
        rainSeries.setName("Rainfall (mm)");

        for (int i = 0; i < dates.length(); i++) {
            String date = dates.getString(i);
            double wave = waves.getDouble(i);
            double windWave = windWaves.getDouble(i);
            double rainfall = rainMap.getOrDefault(date, 0.0);

            Label label = new Label(String.format("📅 %s\n🌊 Wave: %.1f m\n💨 Wind Wave: %.1f m\n🌧 Rain: %.1f mm",
                    date, wave, windWave, rainfall));
            label.getStyleClass().add("weather-card");
            cardContainer.getChildren().add(label);

            waveSeries.getData().add(new XYChart.Data<>(date, wave));
            rainSeries.getData().add(new XYChart.Data<>(date, rainfall));
        }

        weatherChart.setTitle("Wave & Rain Forecast");
        weatherChart.getData().addAll(waveSeries, rainSeries);
    }

    private void showError(String msg) {
        // Just log the error instead of showing popup
        System.err.println("Weather API Error: " + msg);
    }

    /**
     * Create status label for real-time updates
     */
    private void createStatusLabel() {
        statusLabel = new Label("🔄 Real-time weather monitoring active");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #10b981; -fx-font-weight: bold;");

        // Add status label to the top of the card container
        Platform.runLater(() -> {
            if (cardContainer.getParent() instanceof VBox) {
                VBox parent = (VBox) cardContainer.getParent();
                if (!parent.getChildren().contains(statusLabel)) {
                    parent.getChildren().add(0, statusLabel);
                }
            }
        });
    }

    /**
     * Start real-time weather updates and alerts
     */
    private void startRealTimeUpdates() {
        // Update weather data every 5 minutes (300 seconds)
        realTimeUpdater = new Timeline(
                new KeyFrame(Duration.seconds(300), e -> {
                    Platform.runLater(() -> {
                        updateStatusLabel("🔄 Updating weather data...");
                        getWeatherData();
                        simulateWeatherChanges();
                    });
                }));
        realTimeUpdater.setCycleCount(Timeline.INDEFINITE);
        realTimeUpdater.play();

        // Check for weather alerts every 30 seconds
        alertChecker = new Timeline(
                new KeyFrame(Duration.seconds(30), e -> checkWeatherAlerts()));
        alertChecker.setCycleCount(Timeline.INDEFINITE);
        alertChecker.play();

        // Simulate real-time weather condition changes every 2 minutes
        Timeline conditionUpdater = new Timeline(
                new KeyFrame(Duration.seconds(120), e -> simulateWeatherChanges()));
        conditionUpdater.setCycleCount(Timeline.INDEFINITE);
        conditionUpdater.play();
    }

    /**
     * Stop real-time updates (useful when leaving weather page)
     */
    public void stopRealTimeUpdates() {
        if (realTimeUpdater != null) {
            realTimeUpdater.stop();
        }
        if (alertChecker != null) {
            alertChecker.stop();
        }
    }

    /**
     * Simulate weather condition changes for demonstration
     */
    private void simulateWeatherChanges() {
        Platform.runLater(() -> {
            // Simulate small changes in weather conditions
            currentWaveHeight += (random.nextDouble() - 0.5) * 0.5; // ±0.25m change
            currentRainfall += (random.nextDouble() - 0.5) * 2.0; // ±1mm change
            currentWindWave += (random.nextDouble() - 0.5) * 0.3; // ±0.15m change

            // Keep values within realistic ranges
            currentWaveHeight = Math.max(0.1, Math.min(5.0, currentWaveHeight));
            currentRainfall = Math.max(0.0, Math.min(50.0, currentRainfall));
            currentWindWave = Math.max(0.1, Math.min(4.0, currentWindWave));

            // Update current condition
            currentCondition = weatherConditions[random.nextInt(weatherConditions.length)];

            // Update the overview cards with new data
            updateWeatherOverviewCards();

            // Update status with current conditions
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            updateStatusLabel(String.format("🌊 Wave: %.1fm | 🌧️ Rain: %.1fmm | %s | Updated: %s",
                    currentWaveHeight, currentRainfall, currentCondition, timestamp));
        });
    }

    /**
     * Update the weather overview cards with current data
     */
    private void updateWeatherOverviewCards() {
        // Update Current Weather Card
        if (currentConditionLabel != null) {
            currentConditionLabel.setText(currentCondition);
        }
        if (currentTempLabel != null) {
            // Simulate temperature based on condition
            int temp = 25 + random.nextInt(10); // 25-35°C
            currentTempLabel.setText(temp + "°C");
        }
        if (windSpeedLabel != null) {
            int windSpeed = 8 + random.nextInt(20); // 8-28 km/h
            windSpeedLabel.setText(windSpeed + " km/h");
        }
        if (waveHeightLabel != null) {
            waveHeightLabel.setText(String.format("%.1fm", currentWaveHeight));
        }
        if (visibilityLabel != null) {
            int visibility = 5 + random.nextInt(10); // 5-15 km
            visibilityLabel.setText(visibility + " km");
        }
        if (seaTempLabel != null) {
            int seaTemp = 24 + random.nextInt(6); // 24-30°C
            seaTempLabel.setText(seaTemp + "°C");
        }

        // Update Safety Status Card
        boolean isSafe = isSafeForFishing();
        int alertLevel = getWeatherAlertLevel();

        if (safetyIconLabel != null) {
            safetyIconLabel.setText(isSafe ? "✅" : (alertLevel == 2 ? "🚨" : "⚠️"));
        }
        if (safetyStatusLabel != null) {
            safetyStatusLabel
                    .setText(isSafe ? "Safe for Fishing" : (alertLevel == 2 ? "Dangerous Conditions" : "Use Caution"));
        }
        if (waveConditionStatus != null) {
            waveConditionStatus
                    .setText(currentWaveHeight < 1.5 ? "Good" : currentWaveHeight < 2.5 ? "Moderate" : "Poor");
        }
        if (windSpeedStatus != null) {
            int windSpeed = 8 + random.nextInt(20);
            windSpeedStatus.setText(windSpeed < 15 ? "Safe" : windSpeed < 25 ? "Moderate" : "High");
        }
        if (visibilityStatus != null) {
            int visibility = 5 + random.nextInt(10);
            visibilityStatus.setText(visibility > 8 ? "Excellent" : visibility > 5 ? "Good" : "Poor");
        }

        // Update Bottom Metrics
        if (tideStatusLabel != null) {
            String[] tideStates = { "High", "Low", "Rising", "Falling" };
            tideStatusLabel.setText(tideStates[random.nextInt(tideStates.length)]);
        }
        if (uvIndexLabel != null) {
            int uvIndex = 3 + random.nextInt(8); // 3-10
            uvIndexLabel.setText(String.valueOf(uvIndex));
        }
        if (currentHumidityLabel != null) {
            int humidity = 60 + random.nextInt(30); // 60-90%
            currentHumidityLabel.setText(humidity + "%");
        }
        if (pressureLabel != null) {
            int pressure = 1005 + random.nextInt(20); // 1005-1025 hPa
            pressureLabel.setText(String.valueOf(pressure));
        }
    }

    /**
     * Check for weather alerts based on current conditions (no popups - just
     * logging)
     */
    private void checkWeatherAlerts() {
        Platform.runLater(() -> {
            boolean hasAlert = false;
            String alertMessage = "";

            // Check wave height alert
            if (currentWaveHeight > WAVE_ALERT_THRESHOLD) {
                hasAlert = true;
                alertMessage = "⚠️ HIGH WAVES: " + String.format("%.1fm", currentWaveHeight)
                        + " - Exercise extreme caution!";
                System.out.println("Weather Alert: " + alertMessage);
            }

            // Check rainfall alert
            if (currentRainfall > RAIN_ALERT_THRESHOLD) {
                hasAlert = true;
                alertMessage = "🌧️ HEAVY RAIN: " + String.format("%.1fmm", currentRainfall)
                        + " - Check equipment and visibility!";
                System.out.println("Weather Alert: " + alertMessage);
            }

            // Check wind wave alert
            if (currentWindWave > WIND_WAVE_ALERT_THRESHOLD) {
                hasAlert = true;
                alertMessage = "💨 STRONG WINDS: " + String.format("%.1fm", currentWindWave)
                        + " wind waves - Consider postponing trips!";
                System.out.println("Weather Alert: " + alertMessage);
            }

            // Log positive conditions occasionally
            if (!hasAlert && random.nextDouble() < 0.1) { // 10% chance
                String positiveAlert = weatherAlerts[random.nextInt(weatherAlerts.length)];
                if (positiveAlert.contains("☀️") || positiveAlert.contains("🌤️")) {
                    System.out.println("Weather Update: " + positiveAlert);
                }
            }
        });
    }

    /**
     * Update status label with current information
     */
    private void updateStatusLabel(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    /**
     * Get current weather summary for external use
     */
    public String getCurrentWeatherSummary() {
        return String.format("Current Conditions: %s | Wave: %.1fm | Rain: %.1fmm | Wind Wave: %.1fm",
                currentCondition, currentWaveHeight, currentRainfall, currentWindWave);
    }

    /**
     * Check if current conditions are safe for fishing
     */
    public boolean isSafeForFishing() {
        return currentWaveHeight < WAVE_ALERT_THRESHOLD &&
                currentRainfall < RAIN_ALERT_THRESHOLD &&
                currentWindWave < WIND_WAVE_ALERT_THRESHOLD;
    }

    /**
     * Get weather alert level (0=safe, 1=caution, 2=danger)
     */
    public int getWeatherAlertLevel() {
        if (currentWaveHeight > WAVE_ALERT_THRESHOLD ||
                currentRainfall > RAIN_ALERT_THRESHOLD ||
                currentWindWave > WIND_WAVE_ALERT_THRESHOLD) {
            return 2; // Danger
        } else if (currentWaveHeight > WAVE_ALERT_THRESHOLD * 0.7 ||
                currentRainfall > RAIN_ALERT_THRESHOLD * 0.7 ||
                currentWindWave > WIND_WAVE_ALERT_THRESHOLD * 0.7) {
            return 1; // Caution
        }
        return 0; // Safe
    }
}
