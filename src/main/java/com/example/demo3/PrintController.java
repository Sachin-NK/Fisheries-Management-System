package com.example.demo3;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Print/PDF Generation functionality
 */
public class PrintController {
    
    @FXML private VBox printContainer;
    @FXML private ComboBox<String> reportTypeCombo;
    @FXML private TextField outputPathField;
    @FXML private Button browseButton;
    @FXML private Button generateButton;
    @FXML private TextArea reportPreview;
    @FXML private ProgressBar progressBar;
    @FXML private Label statusLabel;
    @FXML private CheckBox includeWeatherCheck;
    @FXML private CheckBox includeActivitiesCheck;
    @FXML private CheckBox includeSafetyCheck;
    @FXML private TextField userNameField;
    @FXML private TextField locationField;
    
    private String currentUsername = "User";
    private WeatherController weatherController;
    
    @FXML
    public void initialize() {
        setupReportTypes();
        setupDefaultValues();
        setupEventHandlers();
    }
    
    /**
     * Setup report type options
     */
    private void setupReportTypes() {
        reportTypeCombo.getItems().addAll(
            "Comprehensive Fisheries Report",
            "Weather Conditions Report", 
            "Safety Assessment Report",
            "Daily Activity Summary"
        );
        reportTypeCombo.setValue("Comprehensive Fisheries Report");
    }
    
    /**
     * Setup default values
     */
    private void setupDefaultValues() {
        // Set default output path to user's Documents folder
        String userHome = System.getProperty("user.home");
        String documentsPath = userHome + File.separator + "Documents" + File.separator + "Fisheries_Reports";
        outputPathField.setText(documentsPath);
        
        // Create directory if it doesn't exist
        File reportsDir = new File(documentsPath);
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }
        
        // Set default values
        userNameField.setText(currentUsername);
        locationField.setText("Negombo, Sri Lanka");
        
        // Check all options by default
        includeWeatherCheck.setSelected(true);
        includeActivitiesCheck.setSelected(true);
        includeSafetyCheck.setSelected(true);
        
        // Initial status
        statusLabel.setText("Ready to generate PDF reports");
        progressBar.setVisible(false);
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        browseButton.setOnAction(e -> browseOutputDirectory());
        generateButton.setOnAction(e -> generatePDFReport());
        
        reportTypeCombo.setOnAction(e -> updateReportPreview());
        
        // Update preview when checkboxes change
        includeWeatherCheck.setOnAction(e -> updateReportPreview());
        includeActivitiesCheck.setOnAction(e -> updateReportPreview());
        includeSafetyCheck.setOnAction(e -> updateReportPreview());
        
        // Initial preview
        updateReportPreview();
    }
    
    /**
     * Browse for output directory
     */
    @FXML
    private void browseOutputDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Directory for PDF Reports");
        
        // Set initial directory
        File initialDir = new File(outputPathField.getText());
        if (initialDir.exists()) {
            directoryChooser.setInitialDirectory(initialDir);
        }
        
        Stage stage = (Stage) browseButton.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);
        
        if (selectedDirectory != null) {
            outputPathField.setText(selectedDirectory.getAbsolutePath());
        }
    }
    
    /**
     * Generate PDF report based on selected options
     */
    @FXML
    private void generatePDFReport() {
        String reportType = reportTypeCombo.getValue();
        String outputPath = outputPathField.getText();
        String username = userNameField.getText().trim();
        String location = locationField.getText().trim();
        
        // Validation
        if (reportType == null || reportType.isEmpty()) {
            showAlert("Error", "Please select a report type.");
            return;
        }
        
        if (outputPath == null || outputPath.isEmpty()) {
            showAlert("Error", "Please specify an output directory.");
            return;
        }
        
        File outputDir = new File(outputPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        
        final String finalUsername = username.isEmpty() ? "User" : username;
        final String finalLocation = location.isEmpty() ? "Unknown Location" : location;
        
        // Show progress
        progressBar.setVisible(true);
        progressBar.setProgress(-1); // Indeterminate progress
        generateButton.setDisable(true);
        statusLabel.setText("Generating PDF report...");
        
        // Create background task for PDF generation
        Task<String> pdfTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return generateSelectedReport(reportType, finalUsername, finalLocation, outputPath);
            }
            
            @Override
            protected void succeeded() {
                String filePath = getValue();
                progressBar.setVisible(false);
                generateButton.setDisable(false);
                
                if (filePath != null) {
                    statusLabel.setText("✅ PDF report generated successfully!");
                    showAlert("Success", "PDF report generated successfully!\n\nFile saved to:\n" + filePath);
                } else {
                    statusLabel.setText("❌ Failed to generate PDF report");
                    showAlert("Error", "Failed to generate PDF report. Please check the console for details.");
                }
            }
            
            @Override
            protected void failed() {
                progressBar.setVisible(false);
                generateButton.setDisable(false);
                statusLabel.setText("❌ Error generating PDF report");
                showAlert("Error", "An error occurred while generating the PDF report:\n" + getException().getMessage());
            }
        };
        
        Thread pdfThread = new Thread(pdfTask);
        pdfThread.setDaemon(true);
        pdfThread.start();
    }
    
    /**
     * Generate the selected type of report
     */
    private String generateSelectedReport(String reportType, String username, String location, String outputPath) {
        try {
            // Get current weather data (simulated for now)
            WeatherData weatherData = getCurrentWeatherData();
            
            // Get recent activities (simulated)
            List<String> recentActivities = getRecentActivities();
            
            switch (reportType) {
                case "Comprehensive Fisheries Report":
                    return PDFReportService.generateFisheriesReport(username, weatherData, recentActivities, outputPath);
                    
                case "Weather Conditions Report":
                    return PDFReportService.generateWeatherReport(weatherData, location, outputPath);
                    
                case "Safety Assessment Report":
                    return PDFReportService.generateFisheriesReport(username, weatherData, recentActivities, outputPath);
                    
                case "Daily Activity Summary":
                    return PDFReportService.generateFisheriesReport(username, weatherData, recentActivities, outputPath);
                    
                default:
                    return PDFReportService.generateFisheriesReport(username, weatherData, recentActivities, outputPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get current weather data (simulated - in real app, get from WeatherController)
     */
    private WeatherData getCurrentWeatherData() {
        // In a real implementation, you would get this from the WeatherController
        // For now, we'll create sample data
        WeatherData data = new WeatherData();
        data.setCondition("Partly Cloudy");
        data.setTemperature(28.0);
        data.setSeaTemperature(26.0);
        data.setWaveHeight(1.2);
        data.setWindSpeed(15.0);
        data.setVisibility(10.0);
        data.setHumidity(78.0);
        data.setPressure(1013.0);
        data.setUvIndex(7);
        data.setTideStatus("High");
        
        return data;
    }
    
    /**
     * Get recent activities (simulated)
     */
    private List<String> getRecentActivities() {
        List<String> activities = new ArrayList<>();
        activities.add("Weather data updated at " + LocalDateTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("HH:mm")));
        activities.add("Safety assessment completed");
        activities.add("Marine conditions checked");
        activities.add("User logged into system");
        activities.add("Location coordinates updated");
        return activities;
    }
    
    /**
     * Update the report preview based on selected options
     */
    private void updateReportPreview() {
        StringBuilder preview = new StringBuilder();
        String reportType = reportTypeCombo.getValue();
        WeatherData sampleWeather = getCurrentWeatherData();
        
        preview.append("📋 DETAILED REPORT PREVIEW\n");
        preview.append("==========================\n\n");
        
        // Header Information
        preview.append("🏢 SMART FISHERIES MANAGEMENT SYSTEM\n");
        preview.append("Report Type: ").append(reportType).append("\n");
        preview.append("Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' HH:mm"))).append("\n");
        preview.append("User: ").append(userNameField.getText().isEmpty() ? "User" : userNameField.getText()).append("\n");
        preview.append("Location: ").append(locationField.getText().isEmpty() ? "Unknown" : locationField.getText()).append("\n\n");
        
        // User Information Section
        preview.append("👤 USER INFORMATION\n");
        preview.append("-------------------\n");
        preview.append("Username: ").append(userNameField.getText().isEmpty() ? "User" : userNameField.getText()).append("\n");
        preview.append("Report Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' HH:mm:ss"))).append("\n");
        preview.append("System: Smart Fisheries Management System v1.0\n\n");
        
        // Weather Conditions Section
        if (includeWeatherCheck.isSelected()) {
            preview.append("🌤️ CURRENT WEATHER CONDITIONS\n");
            preview.append("-----------------------------\n");
            preview.append("Condition: ").append(sampleWeather.getCondition()).append("\n");
            preview.append("Temperature: ").append(sampleWeather.getTemperature()).append("°C\n");
            preview.append("Sea Temperature: ").append(sampleWeather.getSeaTemperature()).append("°C\n");
            preview.append("Wave Height: ").append(sampleWeather.getWaveHeight()).append("m\n");
            preview.append("Wind Speed: ").append(sampleWeather.getWindSpeed()).append(" km/h\n");
            preview.append("Visibility: ").append(sampleWeather.getVisibility()).append(" km\n");
            preview.append("Humidity: ").append(sampleWeather.getHumidity()).append("%\n");
            preview.append("Pressure: ").append(sampleWeather.getPressure()).append(" hPa\n");
            preview.append("UV Index: ").append(sampleWeather.getUvIndex()).append("\n");
            preview.append("Tide Status: ").append(sampleWeather.getTideStatus()).append("\n\n");
            
            // Marine Conditions Assessment
            preview.append("🌊 MARINE CONDITIONS ASSESSMENT\n");
            preview.append("-------------------------------\n");
            String safetyStatus = sampleWeather.isSafeForFishing() ? "✅ SAFE FOR FISHING" : "⚠️ USE CAUTION";
            preview.append("Safety Status: ").append(safetyStatus).append("\n\n");
            
            preview.append("Parameter Analysis:\n");
            preview.append("• Wave Conditions: ").append(sampleWeather.getWaveHeight() < 1.5 ? "Good - Ideal for all boats" : 
                          sampleWeather.getWaveHeight() < 2.5 ? "Moderate - Suitable for larger boats" : "Poor - Small boats avoid").append("\n");
            preview.append("• Wind Conditions: ").append(sampleWeather.getWindSpeed() < 15 ? "Safe - Excellent conditions" : 
                          sampleWeather.getWindSpeed() < 25 ? "Moderate - Exercise caution" : "High - Consider postponing").append("\n");
            preview.append("• Visibility: ").append(sampleWeather.getVisibility() > 8 ? "Excellent - Clear navigation" : 
                          sampleWeather.getVisibility() > 5 ? "Good - Good for fishing" : "Poor - Use navigation aids").append("\n\n");
            
            // Fishing Recommendations
            preview.append("🎣 FISHING RECOMMENDATIONS\n");
            preview.append("--------------------------\n");
            if (sampleWeather.isSafeForFishing()) {
                preview.append("✅ Current conditions are favorable for fishing activities\n");
                preview.append("🎣 Best fishing times: Early morning (5:00-10:00 AM) and evening (4:00-7:00 PM)\n");
                preview.append("🌊 Wave conditions are suitable for all vessel types\n");
            } else {
                preview.append("⚠️ Current conditions require extra caution\n");
                preview.append("🚤 Recommend larger, more stable vessels only\n");
                preview.append("📡 Maintain constant communication with harbor authorities\n");
            }
            
            if (sampleWeather.getWindSpeed() > 20) {
                preview.append("💨 High winds detected - secure all equipment properly\n");
            }
            if (sampleWeather.getWaveHeight() > 2.0) {
                preview.append("🌊 High waves - avoid shallow water areas\n");
            }
            if (sampleWeather.getVisibility() < 5) {
                preview.append("🌫️ Limited visibility - use radar and GPS navigation\n");
            }
            
            preview.append("🧥 Always wear appropriate safety gear and life jackets\n");
            preview.append("📱 Carry emergency communication devices\n");
            preview.append("⛽ Ensure adequate fuel and supplies before departure\n\n");
        }
        
        // Recent Activities Section
        if (includeActivitiesCheck.isSelected()) {
            preview.append("📊 RECENT SYSTEM ACTIVITIES\n");
            preview.append("---------------------------\n");
            List<String> activities = getRecentActivities();
            for (String activity : activities) {
                preview.append("• ").append(activity).append("\n");
            }
            preview.append("\n");
        }
        
        // Safety Guidelines Section
        if (includeSafetyCheck.isSelected()) {
            preview.append("🚨 SAFETY GUIDELINES\n");
            preview.append("--------------------\n");
            preview.append("🚨 Always check weather conditions before departure\n");
            preview.append("📞 Inform harbor authorities of your fishing plans\n");
            preview.append("🧭 Carry proper navigation equipment (GPS, compass, charts)\n");
            preview.append("📡 Maintain VHF radio contact with other vessels and shore\n");
            preview.append("🆘 Know emergency procedures and contact numbers\n");
            preview.append("⚓ Anchor safely in designated areas during rough weather\n");
            
            if (!sampleWeather.isSafeForFishing()) {
                preview.append("⚠️ CURRENT CONDITIONS: Exercise extreme caution or consider postponing trip\n");
            }
            preview.append("\n");
        }
        
        // PDF Format Information
        preview.append("📄 PDF DOCUMENT FEATURES\n");
        preview.append("------------------------\n");
        preview.append("✅ Professional header with company branding\n");
        preview.append("✅ Color-coded sections and tables\n");
        preview.append("✅ Structured data presentation\n");
        preview.append("✅ Safety status indicators\n");
        preview.append("✅ Comprehensive weather analysis\n");
        preview.append("✅ Marine conditions assessment\n");
        preview.append("✅ Fishing recommendations based on conditions\n");
        preview.append("✅ Emergency contact information\n");
        preview.append("✅ Professional footer with disclaimers\n");
        preview.append("✅ Print-ready A4 format\n\n");
        
        preview.append("📁 File will be saved as: Fisheries_Report_").append(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))).append(".pdf\n");
        preview.append("📂 Location: ").append(outputPathField.getText()).append("\n");
        
        reportPreview.setText(preview.toString());
    }
    
    /**
     * Show alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Set the current username (called from MainController)
     */
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
        if (userNameField != null) {
            userNameField.setText(username);
        }
    }
    
    /**
     * Set weather controller reference for real data
     */
    public void setWeatherController(WeatherController weatherController) {
        this.weatherController = weatherController;
    }
}