package me.sunstorm.weather;

import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.templ.jte.JteTemplateEngine;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.sunstorm.weather.data.HourlyData;
import me.sunstorm.weather.data.TemplateData;

import java.nio.file.Path;
import java.util.List;

@Slf4j
public class WeatherVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        var templateHandler = createTemplateHandler();

        var httpClient = createWebClient();

        var router = Router.router(vertx);
        router.get().handler((ctx) -> {
            fetchData(httpClient).onComplete((data) -> {
                if (data.failed()) ctx.fail(data.cause());
                ctx.put("data", data.result());
                ctx.next();
            });
        });
        router.get().handler(templateHandler);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080)
                .onComplete((http) -> {
            if (http.succeeded()) {
                log.info("Weather extension started");
                startPromise.complete();
            } else {
                log.info("Failed to start weather extension");
                startPromise.fail(http.cause());
            }
        });
    }

    private Handler<RoutingContext> createTemplateHandler() {
        var jte = TemplateEngine.createPrecompiled(Path.of("jte-classes"), ContentType.Html);
        var engine = JteTemplateEngine.create(jte);
        return (context) -> {
            engine.render(new JsonObject(context.data()), "extension.jte", res -> {
                if (res.succeeded()) {
                    if (!context.request().isEnded()) {
                        context.request().resume();
                    }
                    context.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html").end(res.result());
                } else {
                    if (!context.request().isEnded()) {
                        context.request().resume();
                    }
                    context.fail(res.cause());
                }
            });
        };
    }

    private WebClient createWebClient() {
        var opt = new WebClientOptions().setUserAgent("");
        return WebClient.create(vertx, opt);
    }

    @SneakyThrows
    private Future<TemplateData> fetchData(WebClient client) {
        return client.getAbs(Constants.ENDPOINT + "/" + Constants.CITY)
                .as(BodyCodec.string())
                .send()
                .map((res) -> {
                    log.debug("weather service response: {}", res.body());
                    var data = JsonParser.parseString(res.body());
                    var hours = Constants.GSON.fromJson(data.getAsJsonObject().get("next_hours"), new TypeToken<List<HourlyData>>() {})
                            .stream()
                            .limit(12)
                            .toList();

                    var result = new TemplateData(hours);
                    result.calculateScale();
                    result.calculateDaylight();
                    return result;
                });
    }
}
