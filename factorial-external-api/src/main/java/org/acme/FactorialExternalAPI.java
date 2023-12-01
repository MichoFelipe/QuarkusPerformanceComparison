package org.acme;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/factorialExternalApi")
@Produces(MediaType.APPLICATION_JSON)
public class FactorialExternalAPI {

    @Inject
    FactorialService factorialService;
 
    @GET
    @Path("/calculate/{input}/uuid/{uuid}")
    public Uni<Response> getFactorialValue(@PathParam("input") int input, @PathParam("uuid") String uuid){
     return factorialService.getFactorialValue(input, uuid);
    }
}