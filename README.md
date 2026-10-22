# Weather App

A modern weather dashboard application built with Java 17, Spring Boot 3, and Thymeleaf. The application provides current weather data, 5-16 day forecasts, and city-specific metrics using multiple weather APIs.

## 🚀 Features

- **Real-time Weather**: Current temperature, humidity, wind speed, and apparent temperature.
- **Extended Forecast**: Customizable forecast range (5 to 16 days).
- **City Intelligence**: 
    - Integration with OpenWeatherMap for geocoding (coordinates lookup).
    - Integration with Open-Meteo for precise meteorological data.
    - Distance calculation from a reference location.
- **Historical Data**: Display of record highs and lows for cities.
- **Modern UI**: 
    - Dashboard-style layout (`/v2` endpoint).
    - Dark mode support.
    - PWA ready (Manifest and Service Worker included).
- **Responsiveness**: Fully adaptive design for mobile and desktop devices.

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 3.2.5
- **Frontend**: Thymeleaf, CSS3 (Custom Variables), JavaScript (ES6)
- **APIs**: OpenWeatherMap (Geo API), Open-Meteo (Weather API)
- **Build Tool**: Maven

## ⚙️ Installation & Setup

### Prerequisites
- **JDK 17** or higher installed.
- **Apache Maven** installed.
- An **OpenWeatherMap API Key** (Required for city geocoding).

### 1. Clone the Repository
```bash
git clone <repository-url>
cd weatherApp
```

### 2. Configure API Keys
You can provide your OpenWeatherMap API key in two ways:

**Option A: Environment Variable (Recommended)**
```powershell
# Windows PowerShell
$env:OPENWEATHER_API_KEY = 'your_api_key_here'

# Linux/macOS
export OPENWEATHER_API_KEY='your_api_key_here'
```

**Option B: application.properties**
Add the following line to `src/main/resources/application.properties`:
```properties
openweather.api.key=your_api_key_here
```

### 3. Run the Application
Execute the following command from the project root:
```bash
mvn spring-boot:run
```

The application will be available at: `http://localhost:5000`

## 📖 API Endpoints

| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/` | GET | Main landing page. |
| `/weather` | POST | Standard weather view (Current + 5-day forecast). |
| `/v2` | GET/POST | Advanced Dashboard view with extended forecast and historical data. |

## 📁 Project Structure

- `src/main/java/com/weatherapp/controller`: Handles HTTP requests and orchestrates data flow.
- `src/main/java/com/weatherapp/service`: Business logic for interacting with external Weather APIs.
- `src/main/java/com/weatherapp/dto`: Data Transfer Objects for API responses.
- `src/main/java/com/weatherapp/util`: Utility classes (Distance calculation, Weather code mapping).
- `src/main/resources/templates`: Thymeleaf HTML templates.
- `src/main/resources/static`: Static assets (CSS, JS, Images, Manifest).
