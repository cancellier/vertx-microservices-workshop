package io.vertx.workshop.quote;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.workshop.common.MicroServiceVerticle;

/**
 * a verticle generating "fake" quotes based on the configuration.
 */
public class GeneratorConfigVerticle extends MicroServiceVerticle {

    /**
     * The address on which the data are sent.
     */
    public static final String ADDRESS = "market";

    /**
     * This method is called when the verticle is deployed.
     */
    @Override
    public void start() {
        super.start();

        // Read the configuration, and deploy a MarketDataVerticle for each company listed in the configuration.
        JsonArray symbols = config().getJsonArray("symbols");
        vertx.deployVerticle(MarketDataProvider.class.getName(), new DeploymentOptions().setConfig(new JsonObject().put("symbols", symbols)));


        // Deploy another verticle without configuration.
        vertx.deployVerticle(RestQuoteAPIVerticle.class.getName(), new DeploymentOptions().setConfig(config()));

        // Publish the services in the discovery infrastructure.
        publishMessageSource("market-data", ADDRESS, rec -> {
            if (!rec.succeeded()) {
                rec.cause().printStackTrace();
            }
            System.out.println("Market-Data service published : " + rec.succeeded());
        });

        publishHttpEndpoint("quotes", "localhost", config().getInteger("http.port", 8080), ar -> {
            if (ar.failed()) {
                ar.cause().printStackTrace();
            } else {
                System.out.println("Quotes (Rest endpoint) service published : " + ar.succeeded());
            }
        });
    }
}
