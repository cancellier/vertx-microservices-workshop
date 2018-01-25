package io.vertx.workshop.quote;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

import java.math.BigDecimal;
import java.util.Random;
import java.util.TreeSet;

public class MarketDataProvider extends AbstractVerticle {

    final TreeSet<String> symbols = new TreeSet<>();

    public void get() {

        WebClient.create(vertx).get(443, "api.coinmarketcap.com", "/v1/ticker")
                .addQueryParam("convert", "EUR")
                .addQueryParam("limit", "0")
                .ssl(true)
                .send(ar -> {
                    if (!ar.succeeded()) {
                        ar.cause().printStackTrace();
                        return;
                    }

                    final JsonArray objects = ar.result().bodyAsJsonArray();
                    objects.stream()
                            .map(o -> (JsonObject) o)
                            .filter(o -> symbols.contains(o.getString("symbol")))
                            .filter(o -> !"batcoin".equals(o.getString("id")))
                            .forEach(this::publish);
                });


    }

    private void publish(JsonObject ticker) {


        JsonObject message = new JsonObject()
                .put("exchange", "coinmarketcap")
                .put("symbol", ticker.getString("symbol"))
                .put("name", ticker.getString("name"))
                .put("bid", new BigDecimal(ticker.getString("price_eur", "0")).doubleValue())
                .put("ask", new BigDecimal(ticker.getString("price_eur", "0")).doubleValue())
                .put("volume", new BigDecimal(ticker.getString("24h_volume_eur", "0")).doubleValue())
                .put("open", 0)
                .put("shares", new BigDecimal(ticker.getString("total_supply", "0")).doubleValue());

        vertx.eventBus().publish(GeneratorConfigVerticle.ADDRESS, message);

    }

    @Override
    public void start() throws Exception {
        super.start();

        config().getJsonArray("symbols")
                .stream()
                .map(o -> (String) o)
                .forEach(symbols::add);

        vertx.setPeriodic(60 * 1000L, l -> get());
        vertx.setTimer(5000L, l -> get());
    }
}
