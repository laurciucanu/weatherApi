package com.weatherapp.controller;

import com.weatherapp.dto.openmeteo.OpenMeteoResponse;
import com.weatherapp.dto.owm.*;
import com.weatherapp.service.OpenMeteoService;
import com.weatherapp.service.OpenWeatherMapService;
import com.weatherapp.util.DistanceCalculator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class WeatherController {
    private final OpenWeatherMapService owmService;
    private final OpenMeteoService meteoService;

    public WeatherController(OpenWeatherMapService owmService, OpenMeteoService meteoService) {
        this.owmService = owmService;
        this.meteoService = meteoService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/weather")
    public String getWeather(@RequestParam String city, Model model) {
        try {
            OwmWeatherResponse weather = owmService.getCurrentWeather(city);
            List<OwmForecastResponse.ForecastItem> forecast = owmService.getForecast(city);

            model.addAttribute("weather", weather);
            model.addAttribute("forecast", forecast);
            model.addAttribute("city_name", weather.name());
            model.addAttribute("outlook", "Long-term outlook for " + city + ": Typical seasonal patterns suggest stable temperatures, but precise daily forecasts are unavailable beyond 5 days on the free tier.");

            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Unable to connect to the weather service or city not found.");
            return "index";
        }
    }

    @GetMapping("/v2")
    public String getWeatherV2(@RequestParam(required = false) String city,
                              @RequestParam(defaultValue = "5") int days,
                              Model model) {
        return processV2(city, days, model);
    }

    @PostMapping("/v2")
    public String postWeatherV2(@RequestParam String city,
                                @RequestParam(defaultValue = "5") int days,
                                Model model) {
        return processV2(city, days, model);
    }

    private String processV2(String city, int days, Model model) {
        if (city == null || city.isEmpty()) {
            return "index_v2";
        }

        int cappedDays = Math.min(days, 16);

        try {
            OwmGeoResponse coords = owmService.getCoords(city);
            if (coords == null) {
                model.addAttribute("error", "City not found. Please check the spelling.");
                return "index_v2";
            }

            OpenMeteoResponse meteoData = meteoService.getWeatherAndForecast(coords.lat(), coords.lon(), cappedDays);

            // Prepare forecast list to match the Python structure
            List<Map<String, Object>> forecastList = new ArrayList<>();
            OpenMeteoResponse.Daily daily = meteoData.daily();
            for (int i = 0; i < daily.time().size(); i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("date", daily.time().get(i));
                item.put("max", daily.temperature_2m_max().get(i));
                item.put("min", daily.temperature_2m_min().get(i));
                item.put("precip", daily.precipitation_sum().get(i));
                item.put("code", daily.weather_code().get(i));
                forecastList.add(item);
            }

            double userLat = 44.4268, userLon = 26.1025;
            double distance = DistanceCalculator.calculateDistance(userLat, userLon, coords.lat(), coords.lon());

            Map<String, Object> historical = new HashMap<>();
            historical.put("record_high", Map.of("val", "39.6°C", "date", "July 19, 2007"));
            historical.put("record_low", Map.of("val", "-29.7°C", "date", "December 28, 1996"));
            historical.put("avg_temp", "12.4°C");

            model.addAttribute("weather", meteoData.current());
            model.addAttribute("forecast", forecastList);
            model.addAttribute("city_name", coords.name());
            model.addAttribute("distance", Math.round(distance * 10.0) / 10.0);
            model.addAttribute("google_maps_url", "https://www.google.com/maps/search/?api=1&query=" + coords.name());
            model.addAttribute("historical", historical);
            model.addAttribute("sunrise", daily.sunrise().get(0));
            model.addAttribute("sunset", daily.sunset().get(0));
            model.addAttribute("days", days);
            model.addAttribute("city", city);
            model.addAttribute("current_condition_code", meteoData.current().weather_code());
            model.addAttribute("current_max", daily.temperature_2m_max().get(0));
            model.addAttribute("current_min", daily.temperature_2m_min().get(0));

            return "index_v2";
        } catch (Exception e) {
            model.addAttribute("error", "Unable to connect to weather service.");
            return "index_v2";
        }
    }
}
