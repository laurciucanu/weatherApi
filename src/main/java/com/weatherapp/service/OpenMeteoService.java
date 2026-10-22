package com.weatherapp.service;

import com.weatherapp.dto.openmeteo.OpenMeteoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OpenMeteoService {
    private final WebClient webClient;
    private final String baseUrl;

    public OpenMeteoService(WebClient.Builder webClientBuilder, @Value("${openmeteo.url}") String baseUrl) {
        this.webClient = webClientBuilder.build();
        this.baseUrl = baseUrl;
    }

    public OpenMeteoResponse getWeatherAndForecast(double lat, double lon, int days) {
        return webClient.get()
            .uri(baseUrl + "?latitude={lat}&longitude={lon}&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m" +
                 "&daily=sunrise,sunset,temperature_2m_max,temperature_2m_min,weather_code,precipitation_sum" +
                 "&timezone=auto&forecast_days={days}", lat, lon, days)
            .retrieve()
            .bodyToMono(OpenMeteoResponse.class)
            .block();
    }
}
