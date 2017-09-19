package dk.developer.delta.api.concepts;

import dk.developer.security.Security;
import dk.developer.server.Server;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static dk.developer.security.Security.Mechanism.NONE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("validate")
public class ValidationTestService {
    @POST
    @Path("/user")
    @Security(NONE)
    @Consumes(APPLICATION_JSON)
    public Response user(@Valid User user) {
        return Server.respond().as("Valid");
    }

    @POST
    @Path("/user/settings")
    @Security(NONE)
    @Consumes(APPLICATION_JSON)
    public Response userSettings(@Valid UserSettings user) {
        return Server.respond().as("Valid");
    }

    @POST
    @Path("/user/settings/constructor")
    @Security(NONE)
    @Consumes(APPLICATION_JSON)
    public Response userSettingsConstructor(@Valid UserSettingsConstructor user) {
        return Server.respond().as("Valid");
    }

    @POST
    @Path("/user/subscription")
    @Security(NONE)
    @Consumes(APPLICATION_JSON)
    public Response subscription(@Valid Subscription subscription) {
        return Server.respond().as("Valid");
    }

    @POST
    @Path("/user/subscription/constructor")
    @Security(NONE)
    @Consumes(APPLICATION_JSON)
    public Response subscriptionConstructor(@Valid SubscriptionConstructor subscription) {
        return Server.respond().as("Valid");
    }

    @POST
    @Path("/user/permission")
    @Security(NONE)
    @Consumes(APPLICATION_JSON)
    public Response userPermission(@Valid UserPermission permission) {
        return Server.respond().as("Valid");
    }

    @POST
    @Path("/benefit/club")
    @Security(NONE)
    @Consumes(APPLICATION_JSON)
    public Response benefitClub(@Valid BenefitClub benefitClub) {
        return Server.respond().as("Valid");
    }

    @POST
    @Path("/membership/level")
    @Security(NONE)
    @Consumes(APPLICATION_JSON)
    public Response membershipLevel(@Valid MembershipLevel membershipLevel) {
        return Server.respond().as("Valid");
    }

    @POST
    @Path("/benefit")
    @Security(NONE)
    @Consumes(APPLICATION_JSON)
    public Response benefit(@Valid Benefit benefit) {
        return Server.respond().as("Valid");
    }

    @POST
    @Path("/feedback")
    @Security(NONE)
    @Consumes(APPLICATION_JSON)
    public Response feedback(@Valid Feedback feedback) {
        return Server.respond().as("Valid");
    }

    @POST
    @Path("/request")
    @Security(NONE)
    @Consumes(APPLICATION_JSON)
    public Response request(@Valid BenefitClubRequest request) {
        return Server.respond().as("Valid");
    }

    @POST
    @Path("/configuration")
    @Security(NONE)
    @Consumes(APPLICATION_JSON)
    public Response configuration(@Valid Configuration configuration) {
        return Server.respond().as("Valid");
    }

    @POST
    @Path("/log/event")
    @Security(NONE)
    @Consumes(APPLICATION_JSON)
    public Response logEvent(@Valid LogEvent event) {
        return Server.respond().as("Valid");
    }

    @POST
    @Path("/category")
    @Security(NONE)
    @Consumes(APPLICATION_JSON)
    public Response category(@Valid Category category) {
        return Server.respond().as("Valid");
    }
}
