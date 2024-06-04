package me.sunstorm.weather.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class HourlySerializer implements JsonDeserializer<HourlyData> {
    @Override
    public HourlyData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var obj = json.getAsJsonObject();
        var time = obj.get("time").getAsInt();
        var sky = obj.get("sky").getAsString();
        var skyTitle = obj.get("sky_title").getAsString();
        var temp = obj.get("temp").getAsInt();
        var windSpeed = obj.get("wind_speed").getAsInt();
        var windDirection = obj.get("wind_direction").getAsInt();
        var rain = obj.get("rain").getAsInt();
        var rainPercentage = obj.get("rain_percentage").getAsInt();
        return new HourlyData(time, sky, skyTitle, temp, windSpeed, windDirection, rain, rainPercentage);
    }
}
