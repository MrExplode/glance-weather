package me.sunstorm.weather;

import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.rendering.template.TemplateUtil;
import lombok.SneakyThrows;
import me.sunstorm.weather.data.HourlyData;
import me.sunstorm.weather.data.TemplateData;

import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class WeatherExtension {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void start() {
        var codeResolver = new ResourceCodeResolver("", WeatherExtension.class.getClassLoader());
        var templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);

        Javalin.create(config -> config.fileRenderer(new JavalinJte(templateEngine))).get("/", ctx -> {
            var data = fetchData();
            ctx.header("Widget-Title", "WEATHER");
            ctx.header("Widget-Content-Type", "html");
            ctx.render("extension.jte", TemplateUtil.model("data", data));
        }).start("0.0.0.0", 8080);
    }

    @SneakyThrows
    private TemplateData fetchData() {
        var req = HttpRequest.newBuilder()
                .uri(new URI(Constants.ENDPOINT + "/" + Constants.CITY))
                .header("User-Agent", "")
                .GET()
                .build();
        var res = httpClient.send(req, HttpResponse.BodyHandlers.ofInputStream());

        var data = JsonParser.parseReader(new InputStreamReader(res.body()));
        var hours = Constants.GSON.fromJson(data.getAsJsonObject().get("next_hours"), new TypeToken<List<HourlyData>>() {})
                .stream()
                .limit(12)
                .toList();

        var result = new TemplateData(hours);
        result.calculateScale();
        result.calculateDaylight();
        return result;
    }
}
