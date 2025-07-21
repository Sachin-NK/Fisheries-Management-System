package com.example.demo3;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service class for generating PDF reports for the Fisheries Management System
 * Compatible with iText 5.5.13.3
 */
public class PDFReportService {
    
    private static final BaseColor HEADER_COLOR = new BaseColor(59, 130, 246); // Blue
    private static final BaseColor ACCENT_COLOR = new BaseColor(16, 185, 129); // Green
    private static final BaseColor TEXT_COLOR = new BaseColor(31, 41, 55); // Dark gray
    private static final BaseColor WHITE_COLOR = new BaseColor(255, 255, 255); // White
    private static final BaseColor LIGHT_GRAY = new BaseColor(248, 250, 252); // Light gray
    
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, WHITE_COLOR);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, HEADER_COLOR);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, TEXT_COLOR);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, TEXT_COLOR);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.GRAY);
    
    /**
     * Generate a comprehensive fisheries report PDF
     */
    public static String generateFisheriesReport(String username, WeatherData weatherData, 
                                               List<String> recentActivities, String outputPath) {
        try {
            String fileName = "Fisheries_Report_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            String fullPath = outputPath + File.separator + fileName;
            
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fullPath));
            document.open();
            
            // Add header
            addReportHeader(document, "Smart Fisheries Management System - Comprehensive Report");
            
            // Add user information
            addUserSection(document, username);
            
            // Add weather information
            addWeatherSection(document, weatherData);
            
            // Add recent activities
            addActivitiesSection(document, recentActivities);
            
            // Add safety recommendations
            addSafetySection(document, weatherData);
            
            // Add footer
            addReportFooter(document);
            
            document.close();
            return fullPath;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Generate a weather-specific report PDF
     */
    public static String generateWeatherReport(WeatherData weatherData, String location, String outputPath) {
        try {
            String fileName = "Weather_Report_" + location.replace(" ", "_") + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            String fullPath = outputPath + File.separator + fileName;
            
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fullPath));
            document.open();
            
            // Add header
            addReportHeader(document, "Marine Weather Report - " + location);
            
            // Add detailed weather information
            addDetailedWeatherSection(document, weatherData, location);
            
            // Add marine conditions
            addMarineConditionsSection(document, weatherData);
            
            // Add fishing recommendations
            addFishingRecommendationsSection(document, weatherData);
            
            // Add footer
            addReportFooter(document);
            
            document.close();
            return fullPath;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Add report header with logo and title
     */
    private static void addReportHeader(Document document, String title) throws DocumentException {
        // Create header table
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1, 4});
        
        // Logo cell (using emoji as placeholder)
        PdfPCell logoCell = new PdfPCell();
        logoCell.addElement(new Paragraph("🐟", new Font(Font.FontFamily.HELVETICA, 40, Font.NORMAL)));
        logoCell.setBackgroundColor(HEADER_COLOR);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoCell.setPadding(10);
        
        // Title cell
        PdfPCell titleCell = new PdfPCell();
        titleCell.addElement(new Paragraph(title, TITLE_FONT));
        titleCell.addElement(new Paragraph("Generated on: " + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' HH:mm")), 
            new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, WHITE_COLOR)));
        titleCell.setBackgroundColor(HEADER_COLOR);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titleCell.setPadding(10);
        
        headerTable.addCell(logoCell);
        headerTable.addCell(titleCell);
        
        document.add(headerTable);
        document.add(new Paragraph(" ")); // Add space
    }
    
    /**
     * Add user information section
     */
    private static void addUserSection(Document document, String username) throws DocumentException {
        document.add(new Paragraph("User Information", HEADER_FONT));
        document.add(new Paragraph(" ")); // Add space
        
        PdfPTable userTable = new PdfPTable(2);
        userTable.setWidthPercentage(100);
        userTable.setWidths(new float[]{1, 2});
        
        addTableRow(userTable, "Username:", username);
        addTableRow(userTable, "Report Generated:", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' HH:mm:ss")));
        addTableRow(userTable, "System:", "Smart Fisheries Management System v1.0");
        
        document.add(userTable);
        document.add(new Paragraph(" ")); // Add space
    }
    
    /**
     * Add weather information section
     */
    private static void addWeatherSection(Document document, WeatherData weatherData) throws DocumentException {
        document.add(new Paragraph("Current Weather Conditions", HEADER_FONT));
        document.add(new Paragraph(" ")); // Add space
        
        PdfPTable weatherTable = new PdfPTable(4);
        weatherTable.setWidthPercentage(100);
        weatherTable.setWidths(new float[]{1, 1, 1, 1});
        
        // Header row
        weatherTable.addCell(createHeaderCell("Condition"));
        weatherTable.addCell(createHeaderCell("Temperature"));
        weatherTable.addCell(createHeaderCell("Wave Height"));
        weatherTable.addCell(createHeaderCell("Wind Speed"));
        
        // Data row
        weatherTable.addCell(createDataCell(weatherData.getCondition()));
        weatherTable.addCell(createDataCell(weatherData.getTemperature() + "°C"));
        weatherTable.addCell(createDataCell(weatherData.getWaveHeight() + "m"));
        weatherTable.addCell(createDataCell(weatherData.getWindSpeed() + " km/h"));
        
        document.add(weatherTable);
        document.add(new Paragraph(" ")); // Add space
    }
    
    /**
     * Add detailed weather section for weather-specific reports
     */
    private static void addDetailedWeatherSection(Document document, WeatherData weatherData, String location) throws DocumentException {
        document.add(new Paragraph("Detailed Weather Analysis", HEADER_FONT));
        document.add(new Paragraph(" ")); // Add space
        
        PdfPTable detailTable = new PdfPTable(2);
        detailTable.setWidthPercentage(100);
        detailTable.setWidths(new float[]{1, 2});
        
        addTableRow(detailTable, "Location:", location);
        addTableRow(detailTable, "Current Condition:", weatherData.getCondition());
        addTableRow(detailTable, "Temperature:", weatherData.getTemperature() + "°C");
        addTableRow(detailTable, "Sea Temperature:", weatherData.getSeaTemperature() + "°C");
        addTableRow(detailTable, "Wave Height:", weatherData.getWaveHeight() + "m");
        addTableRow(detailTable, "Wind Speed:", weatherData.getWindSpeed() + " km/h");
        addTableRow(detailTable, "Visibility:", weatherData.getVisibility() + " km");
        addTableRow(detailTable, "Humidity:", weatherData.getHumidity() + "%");
        addTableRow(detailTable, "Pressure:", weatherData.getPressure() + " hPa");
        addTableRow(detailTable, "UV Index:", String.valueOf(weatherData.getUvIndex()));
        addTableRow(detailTable, "Tide Status:", weatherData.getTideStatus());
        
        document.add(detailTable);
        document.add(new Paragraph(" ")); // Add space
    }
    
    /**
     * Add marine conditions section
     */
    private static void addMarineConditionsSection(Document document, WeatherData weatherData) throws DocumentException {
        document.add(new Paragraph("Marine Conditions Assessment", HEADER_FONT));
        document.add(new Paragraph(" ")); // Add space
        
        // Safety status
        String safetyStatus = weatherData.isSafeForFishing() ? "✅ SAFE FOR FISHING" : "⚠️ USE CAUTION";
        BaseColor statusColor = weatherData.isSafeForFishing() ? ACCENT_COLOR : new BaseColor(239, 68, 68);
        
        document.add(new Paragraph(safetyStatus, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, statusColor)));
        document.add(new Paragraph(" ")); // Add space
        
        // Conditions breakdown
        PdfPTable conditionsTable = new PdfPTable(3);
        conditionsTable.setWidthPercentage(100);
        conditionsTable.setWidths(new float[]{1, 1, 2});
        
        conditionsTable.addCell(createHeaderCell("Parameter"));
        conditionsTable.addCell(createHeaderCell("Status"));
        conditionsTable.addCell(createHeaderCell("Recommendation"));
        
        // Wave conditions
        String waveStatus = weatherData.getWaveHeight() < 1.5 ? "Good" : 
                           weatherData.getWaveHeight() < 2.5 ? "Moderate" : "Poor";
        String waveRec = weatherData.getWaveHeight() < 1.5 ? "Ideal for all boats" : 
                        weatherData.getWaveHeight() < 2.5 ? "Suitable for larger boats" : "Small boats avoid";
        conditionsTable.addCell(createDataCell("Wave Conditions"));
        conditionsTable.addCell(createDataCell(waveStatus));
        conditionsTable.addCell(createDataCell(waveRec));
        
        // Wind conditions
        String windStatus = weatherData.getWindSpeed() < 15 ? "Safe" : 
                           weatherData.getWindSpeed() < 25 ? "Moderate" : "High";
        String windRec = weatherData.getWindSpeed() < 15 ? "Excellent conditions" : 
                        weatherData.getWindSpeed() < 25 ? "Exercise caution" : "Consider postponing";
        conditionsTable.addCell(createDataCell("Wind Conditions"));
        conditionsTable.addCell(createDataCell(windStatus));
        conditionsTable.addCell(createDataCell(windRec));
        
        // Visibility
        String visStatus = weatherData.getVisibility() > 8 ? "Excellent" : 
                          weatherData.getVisibility() > 5 ? "Good" : "Poor";
        String visRec = weatherData.getVisibility() > 8 ? "Clear navigation" : 
                       weatherData.getVisibility() > 5 ? "Good for fishing" : "Use navigation aids";
        conditionsTable.addCell(createDataCell("Visibility"));
        conditionsTable.addCell(createDataCell(visStatus));
        conditionsTable.addCell(createDataCell(visRec));
        
        document.add(conditionsTable);
        document.add(new Paragraph(" ")); // Add space
    }
    
    /**
     * Add fishing recommendations section
     */
    private static void addFishingRecommendationsSection(Document document, WeatherData weatherData) throws DocumentException {
        document.add(new Paragraph("Fishing Recommendations", HEADER_FONT));
        document.add(new Paragraph(" ")); // Add space
        
        com.itextpdf.text.List recommendations = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
        
        if (weatherData.isSafeForFishing()) {
            recommendations.add(new ListItem("✅ Current conditions are favorable for fishing activities", NORMAL_FONT));
            recommendations.add(new ListItem("🎣 Best fishing times: Early morning (5:00-10:00 AM) and evening (4:00-7:00 PM)", NORMAL_FONT));
            recommendations.add(new ListItem("🌊 Wave conditions are suitable for all vessel types", NORMAL_FONT));
        } else {
            recommendations.add(new ListItem("⚠️ Current conditions require extra caution", NORMAL_FONT));
            recommendations.add(new ListItem("🚤 Recommend larger, more stable vessels only", NORMAL_FONT));
            recommendations.add(new ListItem("📡 Maintain constant communication with harbor authorities", NORMAL_FONT));
        }
        
        // Weather-specific recommendations
        if (weatherData.getWindSpeed() > 20) {
            recommendations.add(new ListItem("💨 High winds detected - secure all equipment properly", NORMAL_FONT));
        }
        if (weatherData.getWaveHeight() > 2.0) {
            recommendations.add(new ListItem("🌊 High waves - avoid shallow water areas", NORMAL_FONT));
        }
        if (weatherData.getVisibility() < 5) {
            recommendations.add(new ListItem("🌫️ Limited visibility - use radar and GPS navigation", NORMAL_FONT));
        }
        
        // General recommendations
        recommendations.add(new ListItem("🧥 Always wear appropriate safety gear and life jackets", NORMAL_FONT));
        recommendations.add(new ListItem("📱 Carry emergency communication devices", NORMAL_FONT));
        recommendations.add(new ListItem("⛽ Ensure adequate fuel and supplies before departure", NORMAL_FONT));
        
        document.add(recommendations);
        document.add(new Paragraph(" ")); // Add space
    }
    
    /**
     * Add recent activities section
     */
    private static void addActivitiesSection(Document document, List<String> activities) throws DocumentException {
        document.add(new Paragraph("Recent System Activities", HEADER_FONT));
        document.add(new Paragraph(" ")); // Add space
        
        if (activities == null || activities.isEmpty()) {
            document.add(new Paragraph("No recent activities recorded.", 
                new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC, BaseColor.GRAY)));
        } else {
            com.itextpdf.text.List activityList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
            for (String activity : activities) {
                activityList.add(new ListItem(activity, NORMAL_FONT));
            }
            document.add(activityList);
        }
        
        document.add(new Paragraph(" ")); // Add space
    }
    
    /**
     * Add safety section
     */
    private static void addSafetySection(Document document, WeatherData weatherData) throws DocumentException {
        document.add(new Paragraph("Safety Guidelines", HEADER_FONT));
        document.add(new Paragraph(" ")); // Add space
        
        com.itextpdf.text.List safetyList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
        safetyList.add(new ListItem("🚨 Always check weather conditions before departure", NORMAL_FONT));
        safetyList.add(new ListItem("📞 Inform harbor authorities of your fishing plans", NORMAL_FONT));
        safetyList.add(new ListItem("🧭 Carry proper navigation equipment (GPS, compass, charts)", NORMAL_FONT));
        safetyList.add(new ListItem("📡 Maintain VHF radio contact with other vessels and shore", NORMAL_FONT));
        safetyList.add(new ListItem("🆘 Know emergency procedures and contact numbers", NORMAL_FONT));
        safetyList.add(new ListItem("⚓ Anchor safely in designated areas during rough weather", NORMAL_FONT));
        
        if (!weatherData.isSafeForFishing()) {
            safetyList.add(new ListItem("⚠️ CURRENT CONDITIONS: Exercise extreme caution or consider postponing trip", 
                new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(239, 68, 68))));
        }
        
        document.add(safetyList);
        document.add(new Paragraph(" ")); // Add space
    }
    
    /**
     * Add report footer
     */
    private static void addReportFooter(Document document) throws DocumentException {
        document.add(new Paragraph(" ")); // Add space
        
        PdfPTable footerTable = new PdfPTable(1);
        footerTable.setWidthPercentage(100);
        
        PdfPCell footerCell = new PdfPCell();
        footerCell.addElement(new Paragraph("Smart Fisheries Management System", 
            new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.GRAY)));
        footerCell.addElement(new Paragraph("This report is generated automatically based on current data and should be used as a guide only.", 
            new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY)));
        footerCell.setBackgroundColor(LIGHT_GRAY);
        footerCell.setBorderColor(BaseColor.LIGHT_GRAY);
        footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerCell.setPadding(10);
        
        footerTable.addCell(footerCell);
        document.add(footerTable);
    }
    
    /**
     * Helper method to add table rows
     */
    private static void addTableRow(PdfPTable table, String label, String value) {
        table.addCell(createLabelCell(label));
        table.addCell(createDataCell(value));
    }
    
    /**
     * Create header cell
     */
    private static PdfPCell createHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, WHITE_COLOR)));
        cell.setBackgroundColor(HEADER_COLOR);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        return cell;
    }
    
    /**
     * Create label cell
     */
    private static PdfPCell createLabelCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, BOLD_FONT));
        cell.setBackgroundColor(LIGHT_GRAY);
        cell.setPadding(8);
        return cell;
    }
    
    /**
     * Create data cell
     */
    private static PdfPCell createDataCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, NORMAL_FONT));
        cell.setPadding(8);
        return cell;
    }
}