package io.vertx.workshop.share;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Collection;


/**
 * A service managing a portfolio.
 * <p>
 * This service is an event bus service (a.k.a service proxies, or async RPC). The client and server are generated at
 * compile time.
 * <p>
 * All method are asynchronous and so ends with a {@link Handler} parameter.
 */
@VertxGen
@ProxyGen
public interface ShareService {

  /**
   * The address on which the service is published.
   */
  String ADDRESS = "service.share";

  /**
   * The address on which the successful action are sent.
   */
  String EVENT_ADDRESS = "share";

  /**
   * Gets the shares.
   *
   * @param resultHandler the result handler called when the portfolio has been retrieved. The async result indicates
   *                      whether the call was successful or not.
   */
  void getShare(Handler<AsyncResult<Share>> resultHandler);


}
