package com.weatherapp.dto.openmeteo;

import java.util.List;

public record OpenMeteoResponse(
    Current current,
    Daily daily
) {
    public record Current(
        double temperature_2m,
        double relative_humidity_2m,
        double apparent_temperature,
        int weather_code,
        double wind_speed_10m
    ) {}

    public record Daily(
        List<String> time,
        List<Double> temperature_2m_max,
        List<Double> temperature_2m_min,
        List<Integer> weather_code,
        List<Double> precipitation_sum,
        List<String> sunrise,
        List<String> sunset
    ) {}
}
