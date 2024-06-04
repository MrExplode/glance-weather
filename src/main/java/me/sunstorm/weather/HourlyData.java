package me.sunstorm.weather;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@RequiredArgsConstructor
public class HourlyData {
    @SerializedName("time")
    private final int rawTime;
    private final String sky;
    @SerializedName("sky_title")
    private final String skyTitle;
    private final int temp;
    @SerializedName("wind_speed")
    private final int windSpeed;
    @SerializedName("wind_direction")
    private final int windDirection;
    private final int rain;
    @SerializedName("rain_percentage")
    private final int rainPercentage;

    @Setter
    private double scale;
    @Setter
    private boolean sunrise = false;
    @Setter
    private boolean daylight = false;
    @Setter
    private boolean sunset = false;
    @Setter
    private ZonedDateTime time;
}
