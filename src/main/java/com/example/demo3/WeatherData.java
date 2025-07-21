package com.example.demo3;

/**
 * Data class to hold weather information for PDF generation
 */
public class WeatherData {
    private String condition;
    private double temperature;
    private double seaTemperature;
    private double waveHeight;
    private double windSpeed;
    private double visibility;
    private double humidity;
    private double pressure;
    private int uvIndex;
    private String tideStatus;
    private boolean safeForFishing;
    
    // Constructor
    public WeatherData() {
        // Default values
        this.condition = "Clear";
        this.temperature = 28.0;
        this.seaTemperature = 26.0;
        this.waveHeight = 1.2;
        this.windSpeed = 15.0;
        this.visibility = 10.0;
        this.humidity = 78.0;
        this.pressure = 1013.0;
        this.uvIndex = 7;
        this.tideStatus = "High";
        this.safeForFishing = true;
    }
    
    // Constructor with parameters
    public WeatherData(String condition, double temperature, double seaTemperature, 
                      double waveHeight, double windSpeed, double visibility, 
                      double humidity, double pressure, int uvIndex, String tideStatus) {
        this.condition = condition;
        this.temperature = temperature;
        this.seaTemperature = seaTemperature;
        this.waveHeight = waveHeight;
        this.windSpeed = windSpeed;
        this.visibility = visibility;
        this.humidity = humidity;
        this.pressure = pressure;
        this.uvIndex = uvIndex;
        this.tideStatus = tideStatus;
        this.safeForFishing = calculateSafety();
    }
    
    // Calculate if conditions are safe for fishing
    private boolean calculateSafety() {
        return waveHeight < 3.0 && windSpeed < 25.0 && visibility > 3.0;
    }
    
    // Getters and Setters
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    public double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    
    public double getSeaTemperature() {
        return seaTemperature;
    }
    
    public void setSeaTemperature(double seaTemperature) {
        this.seaTemperature = seaTemperature;
    }
    
    public double getWaveHeight() {
        return waveHeight;
    }
    
    public void setWaveHeight(double waveHeight) {
        this.waveHeight = waveHeight;
        this.safeForFishing = calculateSafety();
    }
    
    public double getWindSpeed() {
        return windSpeed;
    }
    
    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
        this.safeForFishing = calculateSafety();
    }
    
    public double getVisibility() {
        return visibility;
    }
    
    public void setVisibility(double visibility) {
        this.visibility = visibility;
        this.safeForFishing = calculateSafety();
    }
    
    public double getHumidity() {
        return humidity;
    }
    
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
    
    public double getPressure() {
        return pressure;
    }
    
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
    
    public int getUvIndex() {
        return uvIndex;
    }
    
    public void setUvIndex(int uvIndex) {
        this.uvIndex = uvIndex;
    }
    
    public String getTideStatus() {
        return tideStatus;
    }
    
    public void setTideStatus(String tideStatus) {
        this.tideStatus = tideStatus;
    }
    
    public boolean isSafeForFishing() {
        return safeForFishing;
    }
    
    public void setSafeForFishing(boolean safeForFishing) {
        this.safeForFishing = safeForFishing;
    }
    
    @Override
    public String toString() {
        return String.format("WeatherData{condition='%s', temp=%.1f°C, waves=%.1fm, wind=%.1fkm/h, safe=%s}", 
                           condition, temperature, waveHeight, windSpeed, safeForFishing);
    }
}