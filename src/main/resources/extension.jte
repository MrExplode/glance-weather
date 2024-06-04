@import gg.jte.support.ForSupport
@import me.sunstorm.weather.Constants
@import me.sunstorm.weather.data.TemplateData

@param TemplateData data

<div class="size-h2 color-highlight text-center" style="text-transform: capitalize;">${data.current().getSkyTitle()}</div>
<div class="size-h4 text-center">${data.current().getTemp()}°C</div>

<div class="weather-columns flex margin-top-15 justify-center">
@for(var entry : ForSupport.of(data.hours()))
    <div class="weather-column${entry.isFirst() ? " weather-column-current" : ""}">
        @if(entry.get().getRainPercentage() >= 30)
            <div class="weather-column-rain"></div>
        @endif
        @if(entry.get().isDaylight())
            <div class="weather-column-daylight${entry.get().isSunrise() ? " weather-column-daylight-sunrise" : (entry.get().isSunset() ? " weather-column-daylight-sunset" : "")}"></div>
        @endif
        <div class="weather-column-value${entry.get().getTemp() <= 0 ? " weather-column-negative" : ""}">${Math.abs(entry.get().getTemp())}</div>
        <div class="weather-bar" style="--weather-bar-height: ${String.format("%.2f", entry.get().getScale())}"></div>
        <div class="weather-column-time">${entry.get().getTime().format(Constants.FORMATTER)}</div>
    </div>
@endfor
</div>

