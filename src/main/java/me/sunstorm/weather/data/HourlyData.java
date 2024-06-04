package me.sunstorm.weather.data;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@RequiredArgsConstructor
public class HourlyData {
    private final int rawTime;
    private final String sky;
    private final String skyTitle;
    private final int temp;
    private final int windSpeed;
    private final int windDirection;
    private final int rain;
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
