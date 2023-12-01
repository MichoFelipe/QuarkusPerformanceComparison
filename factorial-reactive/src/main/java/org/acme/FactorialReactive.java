package org.acme;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/factorialApi")
public class FactorialReactive {

    Logger LOGGER = LoggerFactory.getLogger(FactorialReactive.class);

    private final WebClient client;
    private static AtomicInteger atomicThreadCounter = new AtomicInteger();

    @Inject
    @RestClient
    FactorialService factorialService;
    
    public FactorialReactive(Vertx vertx) {
        this.client = WebClient.create(vertx,
                new WebClientOptions().setDefaultHost("localhost").setDefaultPort(8080).setSsl(false)
                        .setTrustAll(true));
    }

    @GET
    @Path("/vertxWebClient")
    public Uni<Response> getFactorialUsingVerxtWebClient() {
     var idProcess = UUID.randomUUID();
        LOGGER.info("VERTX, START idProcess [{}] with ATOMIC_VALUE  [{}]", idProcess, atomicThreadCounter.getAndIncrement());
        
        // GENERATE A RANDOM NUMBER BETWEEN 2 TO 10
        int valueToCalculate = getRandomNumber(2,10);

        return client.get("/factorialExternalApi/calculate/" + valueToCalculate+"/uuid/"+idProcess)
                .send()
    .onFailure()
    .invoke(ex -> LOGGER.error("Couldn't execute EXTERNAL Factorial API", ex))
                .map(resp -> {
                    if (resp.statusCode() == 200) {
                     LOGGER.info("VERTX, END idProcess [{}] with ATOMIC_VALUE  [{}]", idProcess, atomicThreadCounter.getAndDecrement());
                        return Response.ok(resp.bodyAsJsonObject()).build();
                    } else {
                     String errorMessage = "Error connecting with factorial external API";
                     LOGGER.error(errorMessage);
                     return Response.status(resp.statusCode()).entity(errorMessage).build();
                    }
                });
    }
    
    @GET
    @Path("/restClient")
    public Uni<Response> getFactorialUsingRestClient() {
     var idProcess = UUID.randomUUID();
     try {
      LOGGER.info("UNI, START idProcess [{}] with ATOMIC_VALUE  [{}]", idProcess, atomicThreadCounter.getAndIncrement());
         int valueToCalculate = getRandomNumber(2,10);
         Uni<Response> resp = factorialService.getFactorialValueFromClient(String.valueOf(valueToCalculate), String.valueOf(idProcess));
         return resp;
     } finally {
      LOGGER.info("UNI, END idProcess [{}] with ATOMIC_VALUE  [{}]", idProcess, atomicThreadCounter.getAndDecrement());
     }
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }    
 
}
