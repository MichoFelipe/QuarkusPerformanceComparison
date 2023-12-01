package org.acme;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RegisterRestClient(configKey = "factorial-external-api")
@Produces(MediaType.APPLICATION_JSON)
public interface FactorialService {

    @GET
    @Path("/calculate/{input}/uuid/{uuid}")
    Uni<Response> getFactorialValueFromClient(@PathParam("input") String input, @PathParam("uuid") String uuid);
}