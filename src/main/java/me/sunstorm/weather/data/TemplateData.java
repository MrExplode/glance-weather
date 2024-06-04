package me.sunstorm.weather.data;

import lombok.extern.slf4j.Slf4j;
import me.sunstorm.weather.Constants;
import org.shredzone.commons.suncalc.SunTimes;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public record TemplateData(List<HourlyData> hours) {

    public void calculateScale() {
        int max = hours.stream().mapToInt(HourlyData::getTemp).max().getAsInt();
        int min = hours.stream().mapToInt(HourlyData::getTemp).min().getAsInt();
        hours.forEach(h -> h.setScale((double) (h.getTemp() - min) / (max - min)));
    }

    public void calculateDaylight() {
        var lat = Double.parseDouble(Constants.LAT);
        var lng = Double.parseDouble(Constants.LNG);
        var timezone = ZoneId.of(Constants.TIMEZONE);
        var times = SunTimes.compute()
                .on(ZonedDateTime.now(timezone))
                .at(lat, lng)
                .execute();
        var sunrise = times.getRise();
        var sunset = times.getSet();

        hours.forEach(h -> {
            var time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(h.getRawTime()), timezone);
            h.setTime(time);
            h.setDaylight(time.getHour() >= sunrise.getHour() && time.getHour() <= sunset.getHour());
            h.setSunrise(time.getHour() == sunrise.getHour());
            h.setSunset(time.getHour() == sunset.getHour());
        });
    }

    public HourlyData current() {
        return hours.getFirst();
    }
}
