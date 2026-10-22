package com.weatherapp.service;

import com.weatherapp.dto.owm.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenWeatherMapService {
    private final WebClient webClient;
    private final String apiKey;
    private final String baseUrl;
    private final String forecastUrl;
    private final String geoUrl;

    public OpenWeatherMapService(WebClient.Builder webClientBuilder,
                                @Value("${openweather.api.key}") String apiKey,
                                @Value("${owm.base.url}") String baseUrl,
                                @Value("${owm.forecast.url}") String forecastUrl,
                                @Value("${owm.geo.url}") String geoUrl) {
        this.webClient = webClientBuilder.build();
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.forecastUrl = forecastUrl;
        this.geoUrl = geoUrl;
    }

    public OwmGeoResponse getCoords(String city) {
        List<OwmGeoResponse> responses = webClient.get()
            .uri(geoUrl + "?q={city}&limit=1&appid={key}", city, apiKey)
            .retrieve()
            .bodyToFlux(OwmGeoResponse.class)
            .collectList()
            .block();

        if (responses == null || responses.isEmpty()) return null;
        return responses.get(0);
    }

    public OwmWeatherResponse getCurrentWeather(String city) {
        return webClient.get()
            .uri(baseUrl + "?q={city}&appid={key}&units=metric", city, apiKey)
            .retrieve()
            .bodyToMono(OwmWeatherResponse.class)
            .block();
    }

    public List<OwmForecastResponse.ForecastItem> getForecast(String city) {
        OwmForecastResponse response = webClient.get()
            .uri(forecastUrl + "?q={city}&appid={key}&units=metric", city, apiKey)
            .retrieve()
            .bodyToMono(OwmForecastResponse.class)
            .block();

        if (response == null || response.list() == null) return new ArrayList<>();

        List<OwmForecastResponse.ForecastItem> dailySummaries = response.list().stream()
            .filter(item -> item.dt_txt().contains("12:00:00"))
            .collect(Collectors.toList());

        if (dailySummaries.size() < 5) {
            List<OwmForecastResponse.ForecastItem> fallback = new ArrayList<>();
            for (int i = 0; i < response.list().size() && fallback.size() < 5; i += 8) {
                fallback.add(response.list().get(i));
            }
            return fallback;
        }

        return dailySummaries.stream().limit(5).collect(Collectors.toList());
    }
}
