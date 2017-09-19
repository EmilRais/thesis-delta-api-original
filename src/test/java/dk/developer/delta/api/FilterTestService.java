package dk.developer.delta.api;

import dk.developer.security.Security;
import dk.developer.server.Server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static dk.developer.security.Security.Mechanism.LOGIN;

@Path("filter")
public class FilterTestService {
    @GET
    @Path("/login")
    @Security(LOGIN)
    public Response login() throws Exception {
        return Server.respond("Was allowed to pass").as("Test");
    }
}
