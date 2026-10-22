package com.weatherapp.dto.owm;

public record OwmWeatherResponse(
    String name,
    Main main,
    Weather[] weather,
    Wind wind,
    Clouds clouds,
    Sys sys
) {
    public record Main(double temp, double feels_like, int humidity, double temp_min, double temp_max) {}
    public record Weather(String main, String description, String icon) {}
    public record Wind(double speed) {}
    public record Clouds(int all) {}
    public record Sys(String country) {}
}
