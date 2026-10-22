package com.weatherapp.util;

import java.util.HashMap;
import java.util.Map;

public class WeatherCodeMapper {
    private static final Map<Integer, String> CODES = new HashMap<>();

    static {
        CODES.put(0, "Clear sky");
        CODES.put(1, "Mainly clear");
        CODES.put(2, "Partly cloudy");
        CODES.put(3, "Overcast");
        CODES.put(45, "Fog");
        CODES.put(48, "Depositing rime fog");
        CODES.put(51, "Light drizzle");
        CODES.put(61, "Slight rain");
        CODES.put(71, "Slight snow fall");
        CODES.put(80, "Slight rain showers");
        CODES.put(95, "Thunderstorm");
    }

    public static String getCondition(int code) {
        return CODES.getOrDefault(code, "Unknown");
    }
}
