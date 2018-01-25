package io.vertx.workshop.share.impl;

import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ProxyHelper;
import io.vertx.workshop.common.MicroServiceVerticle;
import io.vertx.workshop.share.ShareService;

import static io.vertx.workshop.share.ShareService.ADDRESS;
import static io.vertx.workshop.share.ShareService.EVENT_ADDRESS;

public class ShareVerticle extends MicroServiceVerticle {

    public static void main(String[] args) {
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(ShareVerticle.class.getName());
    }

    @Override
    public void start() {
        super.start();

        // Create the service object
        ShareServiceImpl service = new ShareServiceImpl(vertx, discovery, config().getJsonArray("shares"));

        // Register the service proxy on the event bus
        ProxyHelper.registerService(ShareService.class, vertx, service, ADDRESS);

        // Publish it in the discovery infrastructure
        publishEventBusService("share", ADDRESS, ShareService.class, ar -> {
            if (ar.failed()) {
                ar.cause().printStackTrace();
            } else {
                System.out.println("Share service published : " + ar.succeeded());
            }
        });

        //----
        // The portfolio event service
        publishMessageSource("share-events", EVENT_ADDRESS, ar -> {
            if (ar.failed()) {
                ar.cause().printStackTrace();
            } else {
                System.out.println("Share Events service published : " + ar.succeeded());
            }
        });
        //----
    }
}
