package io.vertx.workshop.quote;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This verticle exposes a HTTP endpoint to retrieve the current / last values of the maker data (quotes).
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class RestQuoteAPIVerticle extends AbstractVerticle {

  private Map<String, JsonObject> quotes = new HashMap<>();

  @Override
  public void start() throws Exception {
    vertx.eventBus().<JsonObject>consumer(GeneratorConfigVerticle.ADDRESS)
        .handler(this::onMarketData);


    vertx.createHttpServer()
        .requestHandler(request -> {
          HttpServerResponse response = request.response()
              .putHeader("content-type", "application/json");

            String name = request.getParam("name");
            if(name != null && !name.isEmpty()){
                JsonObject quote = quotes.get(name);
                if(quote == null){
                    response.setStatusCode(404).end();
                } else {
                    response.end(Json.encodePrettily(quote));
                }
            } else {
                response
                        .end(Json.encodePrettily(quotes));
            }

        })
        .listen(config().getInteger("http.port"), ar -> {
          if (ar.succeeded()) {
            System.out.println("Server started on port : " + config().getInteger("http.port"));
          } else {
            System.out.println("Cannot start the server: " + ar.cause());
          }
        });
  }

    private void onMarketData(Message<JsonObject> message) {
        JsonObject body = message.body();
        String key = getKey(body);
        quotes.put(key, body);
    }

    private String getKey(JsonObject body) {
        return body.getString("name");
    }
}
