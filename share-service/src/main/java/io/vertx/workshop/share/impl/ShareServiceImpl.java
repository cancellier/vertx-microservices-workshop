package io.vertx.workshop.share.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.workshop.share.Share;
import io.vertx.workshop.share.ShareService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class ShareServiceImpl implements ShareService {

    private final Vertx vertx;
    private final Map<String, Share> shares;
    private final ServiceDiscovery discovery;

    public ShareServiceImpl(Vertx vertx, ServiceDiscovery discovery, JsonArray shares) {
        this.vertx = vertx;
        this.discovery = discovery;
        this.shares = shares.stream()
                .map(o -> new Share((JsonObject) o))
                .peek(o -> System.out.println(o))
                .collect(toMap(Share::getCode, identity()));
    }

    @Override
    public void getShare(Handler<AsyncResult<Share>> resultHandler) {

    }
}
