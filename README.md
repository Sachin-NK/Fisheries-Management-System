# 🐟 Smart Fisheries Management System

A modern JavaFX desktop application designed for fisheries management in Sri Lanka, featuring real-time weather monitoring, community chat, and comprehensive fishing data management.

## 📋 Table of Contents

- [Features](#features)
- [Screenshots](#screenshots)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [API Integration](#api-integration)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

### 🌊 Weather Monitoring
- **Real-time Marine Weather Data** - Live weather conditions for Sri Lankan coastal areas
- **Popular Fishing Areas** - Quick access to weather for Negombo, Chilaw, Trincomalee, Galle, Mirissa, and Kalpitiya
- **Safety Assessment** - Automated fishing safety recommendations based on weather conditions
- **Interactive Charts** - Wave height trends and weather forecasting
- **Location Search** - Search by city, fishing area, or harbor with coordinate input

### 💬 Community Features
- **Real-time Chat** - Community discussion platform for fishermen
- **Chatbot** - AI-powered assistance for fishing information
- **User Avatars** - Personalized user profiles with color-coded avatars
- **Message History** - Persistent chat history with timestamps

### 📊 Dashboard & Analytics
- **Modern Dashboard** - Clean, professional interface with key metrics
- **Price Tracking** - Fish price monitoring and trends
- **Boat Management** - Fleet tracking and management
- **Trip Planning** - Fishing trip organization and scheduling
- **Fishermen Directory** - Community member management

### 🎨 Modern UI/UX
- **Responsive Design** - Adaptive layout for different screen sizes
- **Dark Theme Support** - Professional blue-themed interface
- **Smooth Animations** - Fluid transitions and hover effects
- **Glass-morphism Effects** - Modern visual styling
- **Accessibility** - Keyboard navigation and screen reader support

## 🖼️ Screenshots

### Weather Dashboard
The weather page provides comprehensive marine weather information with:
- Location search and coordinate input
- Popular fishing areas quick selection
- Current weather conditions with blue gradient design
- Safety status with detailed recommendations
- Bottom metrics showing tide, UV index, humidity, and pressure

### Community Chat
Real-time community discussion platform featuring:
- Modern chat interface with user avatars
- Floating chatbot for instant assistance
- Message history with timestamps
- User-friendly input controls

## 🔧 Prerequisites

Before running the application, ensure you have:

- **Java 11 or higher** - [Download Java](https://www.oracle.com/java/technologies/downloads/)
- **JavaFX SDK** - [Download JavaFX](https://openjfx.io/)
- **Maven 3.6+** - [Download Maven](https://maven.apache.org/download.cgi)
- **Internet Connection** - Required for weather API calls

## 🚀 Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/smart-fisheries-management.git
   cd smart-fisheries-management
   ```

2. **Navigate to the project directory**
   ```bash
   cd JavaFx_Desktop_Application-main
   ```

3. **Install dependencies**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn javafx:run
   ```

   Or alternatively:
   ```bash
   java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -cp target/classes com.example.demo3.HelloApplication
   ```

## 📖 Usage

### Getting Started

1. **Launch the Application**
   - Run the application using Maven or your IDE
   - The main dashboard will appear with navigation sidebar

2. **Weather Monitoring**
   - Click on "Weather" in the sidebar
   - Select a fishing area or enter custom coordinates
   - View current conditions and safety recommendations
   - Scroll down to see detailed charts and forecasts

3. **Community Chat**
   - Access the floating chatbot icon (bottom-left corner)
   - Join community discussions
   - Get AI-powered fishing assistance

4. **Dashboard Navigation**
   - Use the sidebar to navigate between different sections
   - View key metrics on the main dashboard
   - Access user profile and settings

### Key Functionalities

#### Weather Features
- **Location Search**: Enter city names or fishing areas
- **Coordinate Input**: Precise latitude/longitude positioning
- **Quick Areas**: One-click access to popular fishing locations
- **Safety Status**: Real-time safety assessments
- **Weather Alerts**: Automatic notifications for dangerous conditions

#### Chat Features
- **Real-time Messaging**: Instant communication with community
- **Floating Chatbot**: Always-accessible AI assistance
- **User Profiles**: Personalized avatars and usernames
- **Message History**: Persistent conversation records

## 📁 Project Structure

```
JavaFx_Desktop_Application-main/
├── src/
│   └── main/
│       ├── java/com/example/demo3/
│       │   ├── HelloApplication.java          # Main application entry
│       │   ├── MainController.java            # Main window controller
│       │   ├── WeatherController.java         # Weather page logic
│       │   ├── CommunityController.java       # Chat functionality
│       │   ├── ChatWindow.java                # Chat window management
│       │   ├── TransitionManager.java         # UI animations
│       │   └── Message.java                   # Message data model
│       └── resources/com/example/demo3/
│           ├── main.fxml                      # Main layout
│           ├── weathepage.fxml               # Weather page layout
│           ├── community.fxml                # Chat interface
│           ├── chatbot.fxml                  # Chatbot interface
│           ├── chat-window.fxml              # Chat window
│           ├── style.css                     # Main stylesheet
│           ├── weather.css                   # Weather page styles
│           └── chat-window.css               # Chat window styles                                   # Maven configuration
└── README.md                                 # This file
```

## 🛠️ Technologies Used

### Core Technologies
- **JavaFX 17+** - Modern desktop application framework
- **Java 11+** - Programming language
- **Maven** - Dependency management and build tool
- **CSS3** - Modern styling with animations and effects

### External APIs
- **Open-Meteo Marine API** - Real-time marine weather data
- **Open-Meteo Weather API** - General weather information

### Libraries & Dependencies
- **JSON Processing** - For API response handling
- **JavaFX Controls** - UI components and layouts
- **JavaFX FXML** - Declarative UI design

### Design Features
- **CSS Grid & Flexbox** - Responsive layouts
- **CSS Animations** - Smooth transitions and hover effects
- **Glass-morphism** - Modern visual effects
- **Gradient Backgrounds** - Professional color schemes

## 🌐 API Integration

### Weather Data Sources

The application integrates with multiple weather APIs:

#### Marine Weather API
```
https://marine-api.open-meteo.com/v1/marine
Parameters: latitude, longitude, daily data (wave_height_max, wind_wave_height_max)
```

#### General Weather API
```
https://api.open-meteo.com/v1/forecast
Parameters: latitude, longitude, daily precipitation data
```

### Supported Locations

Pre-configured fishing areas in Sri Lanka:
- **Negombo** (7.21°N, 79.84°E)
- **Chilaw** (7.58°N, 79.80°E)
- **Trincomalee** (8.59°N, 81.21°E)
- **Galle** (6.03°N, 80.22°E)
- **Mirissa** (5.95°N, 80.46°E)
- **Kalpitiya** (8.23°N, 79.77°E)

## 🎨 UI/UX Features

### Modern Design System
- **Color Palette**: Professional blue theme (#3b82f6, #1e40af)
- **Typography**: Segoe UI and Roboto font families
- **Spacing**: Consistent 8px grid system
- **Shadows**: Subtle drop shadows for depth

### Interactive Elements
- **Hover Effects**: Smooth color transitions
- **Button States**: Visual feedback for all interactions
- **Form Validation**: Real-time input validation
- **Loading States**: Progress indicators for API calls

### Responsive Layout
- **Flexible Grids**: Adaptive card layouts
- **Scrollable Content**: Proper overflow handling
- **Resizable Windows**: Maintains proportions across sizes

## 🤝 Contributing

We welcome contributions to improve the Smart Fisheries Management System!

### How to Contribute

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Make your changes**
4. **Commit your changes**
   ```bash
   git commit -m 'Add some amazing feature'
   ```
5. **Push to the branch**
   ```bash
   git push origin feature/amazing-feature
   ```
6. **Open a Pull Request**

### Development Guidelines

- Follow Java coding conventions
- Write clear, descriptive commit messages
- Add comments for complex logic
- Test your changes thoroughly
- Update documentation as needed

*This application aims to improve fishing safety and community collaboration through modern technology.*