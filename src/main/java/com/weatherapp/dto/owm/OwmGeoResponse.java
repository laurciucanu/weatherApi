package com.weatherapp.dto.owm;

public record OwmGeoResponse(
    double lat,
    double lon,
    String name
) {}
