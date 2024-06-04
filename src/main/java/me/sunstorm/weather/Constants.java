package me.sunstorm.weather;

import java.time.format.DateTimeFormatter;

public interface Constants {
    String CITY = System.getenv("CITY");
    String TIMEZONE = System.getenv("TIMEZONE");
    String LAT = System.getenv("LATITUDE");
    String LNG = System.getenv("LONGITUDE");
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("hh a");
}
