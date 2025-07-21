package com.example.demo3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;

import java.util.Locale;

public class ChatbotController {

    @FXML
    private VBox chatContainer;

    @FXML
    private TextField userInput;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private void initialize() {
        // Make sure the text field takes all available width
        HBox.setHgrow(userInput, Priority.ALWAYS);
        
        // Focus on the input field when the chatbot loads
        userInput.requestFocus();
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isEmpty()) return;

        // Add user message
        addUserMessage(input);
        
        // Get and add bot response
        String response = getResponse(input.toLowerCase(Locale.ROOT));
        addBotMessage(response);

        // Clear input and scroll to bottom
        userInput.clear();
        userInput.requestFocus();
        scrollPane.setVvalue(1.0);
    }
    
    @FXML
    private void askWeather() {
        processQuickAction("What's the weather like for fishing today?");
    }
    
    @FXML
    private void askFishPrices() {
        processQuickAction("What are the current fish prices?");
    }
    
    @FXML
    private void askFishingSpots() {
        processQuickAction("What are the best fishing spots today?");
    }
    
    private void processQuickAction(String question) {
        // Add user message
        addUserMessage(question);
        
        // Get and add bot response
        String response = getResponse(question.toLowerCase(Locale.ROOT));
        addBotMessage(response);
        
        // Scroll to bottom
        scrollPane.setVvalue(1.0);
    }

    private void addUserMessage(String message) {
        Label label = new Label("You: " + message);
        label.getStyleClass().add("user-message");
        label.setWrapText(true);
        label.setMaxWidth(500);
        
        HBox container = new HBox();
        container.getStyleClass().add("user-message-container");
        container.getChildren().add(label);
        
        chatContainer.getChildren().add(container);
    }
    
    private void addBotMessage(String message) {
        Label label = new Label("Assistant: " + message);
        label.getStyleClass().add("bot-message");
        label.setWrapText(true);
        label.setMaxWidth(500);
        
        HBox container = new HBox();
        container.getStyleClass().add("bot-message-container");
        container.getChildren().add(label);
        
        chatContainer.getChildren().add(container);
    }

    private String getResponse(String input) {
        if (input.contains("weather") || input.contains("sea") || input.contains("wave")) {
            return "The sea is calm today 🌊\nTemperature: 26°C\nWind: Low\nGood day for fishing!";
        } else if (input.contains("price") || input.contains("fish price") || input.contains("market")) {
            return "Today's Fish Prices:\n🐟 Tuna: $15/kg\n🐠 Salmon: $20/kg\n🐟 Mackerel: $10/kg\n🦐 Shrimp: $25/kg";
        } else if (input.contains("spot") || input.contains("location") || input.contains("fishing spot")) {
            return "Best Fishing Spots Today:\n📍 Negombo Beach - Good for tuna\n📍 Chilaw Harbor - Excellent for mackerel\n📍 Trincomalee Bay - Great for various species";
        } else if (input.contains("tuna")) {
            return "Tuna 🐟\nPrice: $15/kg\nA popular fish, rich in protein and Omega-3.";
        } else if (input.contains("salmon")) {
            return "Salmon 🐠\nPrice: $20/kg\nKnown for its pink flesh and high nutritional value.";
        } else if (input.contains("mackerel")) {
            return "Mackerel 🐟\nPrice: $10/kg\nAffordable and rich in oil.";
        } else if (input.contains("regulation") || input.contains("law") || input.contains("rule")) {
            return "Current Fishing Regulations:\n⚖️ Minimum size for tuna: 40cm\n⚖️ Fishing license required for commercial fishing\n⚖️ Protected species must be released immediately";
        } else if (input.contains("emergency") || input.contains("help") || input.contains("safety")) {
            return "Emergency Contacts:\n🆘 Coast Guard: 1-800-COAST-GUARD\n🆘 Marine Rescue: 1-800-MARINE-HELP\n🆘 Weather Emergency: 1-800-WEATHER";
        } else {
            return "I can help with weather conditions, fish prices, fishing spots, regulations, and safety information. What would you like to know about?";
        }
    }
}

