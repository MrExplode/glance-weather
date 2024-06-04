package me.sunstorm.weather;

import java.util.List;

public record TemplateData(List<HourlyData> hours) {

    public void calculateScale() {
        int max = hours.stream().mapToInt(HourlyData::getTemp).max().getAsInt();
        int min = hours.stream().mapToInt(HourlyData::getTemp).min().getAsInt();
        hours.forEach(h -> h.setScale((double) (h.getTemp() - min) / (max - min)));
    }

    public void calculateDaylight() {

    }
}
