package org.acme;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainVerticle extends AbstractVerticle {

  Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    // Create a Router
    Router router = Router.router(vertx);

    // Mount the handler for all incoming requests at every path and HTTP method
    router.route("/factorialVertx/list").handler(context -> {

      // CALL EXTERNAL APIs
      var idProcess = UUID.randomUUID();
      WebClient client = WebClient.create(vertx);

      int NUMBER_EXTERNAL_APIS = 3;
      List<Future<String>> futureList = new ArrayList();
      for(int i=1;i<=NUMBER_EXTERNAL_APIS;i++) {
        int valueToCalculate = getRandomNumber(2,10);
        LOGGER.info(String.format("VERTX, START idProcess [%s] with valueToCalculate1 [%s]", idProcess, valueToCalculate));

        Future<String> response = client.get(8080,"localhost","/factorialExternalApi/calculate/" + valueToCalculate+"/uuid/"+idProcess).expect(ResponsePredicate.SC_OK).send()
          .map(HttpResponse::bodyAsString).recover(error -> {
            LOGGER.error("External API, Error response: "+error.getMessage());
            return Future.succeededFuture();
          });
        futureList.add(response);
      }

      List<String> res = new ArrayList();
      Future.join(futureList)
        .onSuccess(result -> {
          result.list().forEach(x -> {
            if(x != null) {
              res.add(x.toString());
            }
          });
          context.json(new JsonArray(res.toString()));
        })
        .onFailure(error -> {
            //LOGGER.error("We should not fail");
            LOGGER.error("An error exist joining the results "+error);
        });

    });

    // Create the HTTP server
    vertx.createHttpServer()
      // Handle every request using the router
      .requestHandler(router)
      // Start listening
      .listen(8888)
      // Print the port
      .onSuccess(server -> LOGGER.info("HTTP server started on port " + server.actualPort()));
  }

  private int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }

}
