package io.vertx.workshop.quote;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.workshop.common.MicroServiceVerticle;

/**
 * a verticle generating "fake" quotes based on the configuration.
 */
public class GeneratorConfigVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        final Vertx vertx = Vertx.vertx();
        vertx.fileSystem().readFile("./quote-generator/src/conf/config.json", ar -> {
            if (ar.failed()){
                ar.cause().printStackTrace();
            } else {
                System.out.println(ar.result().toJsonObject().encode());
                vertx.deployVerticle(GeneratorConfigVerticle.class.getName(), new DeploymentOptions().setConfig(ar.result().toJsonObject()));
            }
        });
    }


  /**
   * The address on which the data are sent.
   */
  public static final String ADDRESS = "market";

  /**
   * This method is called when the verticle is deployed.
   */
  @Override
  public void start() throws Exception {
    super.start();

      System.out.println(config().encode());

    // Read the configuration, and deploy a MarketDataVerticle for each company listed in the configuration.
    JsonArray quotes = config().getJsonArray("companies", new JsonArray());
    for (Object q : quotes) {
      JsonObject company = (JsonObject) q;
      // Deploy the verticle with a configuration.
      vertx.deployVerticle(MarketDataVerticle.class.getName(), new DeploymentOptions().setConfig(company));
    }

    // Deploy another verticle
    vertx.deployVerticle(RestQuoteAPIVerticle.class.getName(), new DeploymentOptions().setConfig(config()));

    // Deploy another verticle without configuration.
    final JsonObject configProvider = new JsonObject().put("symbols", config().getJsonArray("symbols"));
    vertx.deployVerticle(MarketDataProvider.class.getName(), new DeploymentOptions().setConfig(configProvider));

    // Publish the services in the discovery infrastructure.
//    publishMessageSource("market-data", ADDRESS, rec -> {
//      if (!rec.succeeded()) {
//        rec.cause().printStackTrace();
//      }
//      System.out.println("Market-Data service published : " + rec.succeeded());
//    });

//    publishHttpEndpoint("quotes", "localhost", config().getInteger("http.port", 8080), ar -> {
//      if (ar.failed()) {
//        ar.cause().printStackTrace();
//      } else {
//        System.out.println("Quotes (Rest endpoint) service published : " + ar.succeeded());
//      }
//    });
  }
}
