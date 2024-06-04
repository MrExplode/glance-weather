package me.sunstorm.weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.sunstorm.weather.data.HourlyData;
import me.sunstorm.weather.data.HourlySerializer;

import java.time.format.DateTimeFormatter;

public interface Constants {
    String ENDPOINT = System.getenv("WEATHER_ENDPOINT");
    String CITY = System.getenv("CITY");
    String TIMEZONE = System.getenv("TIMEZONE");
    String LAT = System.getenv("LATITUDE");
    String LNG = System.getenv("LONGITUDE");
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("hh a");
    Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(HourlyData.class, new HourlySerializer())
            .create();
}
