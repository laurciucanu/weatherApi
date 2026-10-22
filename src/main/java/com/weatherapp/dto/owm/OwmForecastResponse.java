package com.weatherapp.dto.owm;

import java.util.List;

public record OwmForecastResponse(
    List<ForecastItem> list
) {
    public record ForecastItem(
        long dt,
        String dt_txt,
        Main main,
        Weather[] weather,
        Wind wind
    ) {
        public record Main(double temp, double feels_like, int humidity, double temp_min, double temp_max) {}
        public record Weather(String main, String description, String icon) {}
        public record Wind(double speed) {}
    }
}
