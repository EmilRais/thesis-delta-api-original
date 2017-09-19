package dk.developer.delta.api.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.DatabaseFront;
import dk.developer.delta.api.Application;
import dk.developer.delta.api.Status;
import dk.developer.delta.api.concepts.*;
import dk.developer.security.Credential;
import dk.developer.security.Security;
import dk.developer.server.Server;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static dk.developer.delta.api.concepts.LogEventType.NAVIGATED_TO_BENEFIT_PAIR;
import static dk.developer.security.Security.Mechanism.LOGIN;
import static dk.developer.security.Security.Mechanism.NONE;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Path("benefit")
public class BenefitService {
    private final DatabaseFront database;

    public BenefitService() {
        database = Application.database();
    }

    @GET
    @Path("all")
    @Security(LOGIN)
    public Response loadEverything() {
        List<BenefitClub> benefitClubs = database.loadAll(BenefitClub.class).everything();
        List<MembershipLevel> membershipLevels = database.loadAll(MembershipLevel.class).everything();
        List<Benefit> benefits = database.loadAll(Benefit.class).everything();
        List<Category> categories = loadCategories();
        Container container = new Container(benefitClubs, membershipLevels, benefits, categories);
        return Server.respond(container).as(Status.LOADED_EVERYTHING);
    }

    @GET
    @Path("configuration")
    @Security(NONE)
    public Response loadConfiguration() {
        Configuration configuration = database.loadAll(Configuration.class).everything().get(0);
        return Server.respond(configuration).as(Status.LOADED_CONFIGURATION);
    }

    @POST
    @Path("visit/benefit/pair")
    @Security(LOGIN)
    public Response logBenefitVisit(@HeaderParam(Security.HEADER) Credential credential,
                                    @NotNull(message = "Payload was missing") VisitPayload payload) {

        BenefitClub benefitClub = database.load(BenefitClub.class).matching("_id").with(payload.benefitClubId);
        if ( benefitClub == null )
            return Server.reportError("Benefit club did not exist");

        User user = database.load(User.class).matching("userId").with(credential.getUserId());
        String message = format("(BenefitClub: %s, BenefitLocation: %s)", payload.getBenefitClubId(), payload.getBenefitLocationId());
        database.save(new LogEvent(user.getId(), NAVIGATED_TO_BENEFIT_PAIR, message, null));
        return Server.respond().as(Status.LOGGED_BENEFIT_PAIR_VISIT);
    }

    private List<Category> loadCategories() {
        List<CategoryConvertible> convertibles = new ArrayList<>();
        convertibles.addAll(database.loadAll(IndustryCategory.class).everything());
        convertibles.addAll(database.loadAll(InterestCategory.class).everything());
        convertibles.addAll(database.loadAll(MoodCategory.class).everything());
        convertibles.addAll(database.loadAll(ProductCategory.class).everything());
        convertibles.addAll(database.loadAll(WeatherCategory.class).everything());

        return convertibles.stream()
                .map(CategoryConvertible::toCategory)
                .collect(toList());
    }

    public static class VisitPayload {
        private final String benefitLocationId;
        private final String benefitClubId;

        @JsonCreator
        public VisitPayload(@JsonProperty("benefitLocationId") String benefitLocationId,
                            @JsonProperty("benefitClubId") String benefitClubId) {
            this.benefitLocationId = benefitLocationId;
            this.benefitClubId = benefitClubId;
        }

        public String getBenefitLocationId() {
            return benefitLocationId;
        }

        public String getBenefitClubId() {
            return benefitClubId;
        }
    }

    public static class Container {
        private final List<BenefitClub> benefitClubs;
        private final List<MembershipLevel> membershipLevels;
        private final List<Benefit> benefits;
        private final List<Category> categories;

        @JsonCreator
        public Container(@JsonProperty("benefitClubs") List<BenefitClub> benefitClubs,
                         @JsonProperty("membershipLevels") List<MembershipLevel> membershipLevels,
                         @JsonProperty("benefits") List<Benefit> benefits,
                         @JsonProperty("categories") List<Category> categories) {
            this.benefitClubs = benefitClubs;
            this.membershipLevels = membershipLevels;
            this.benefits = benefits;
            this.categories = categories;
        }

        public List<BenefitClub> getBenefitClubs() {
            return benefitClubs;
        }

        public List<MembershipLevel> getMembershipLevels() {
            return membershipLevels;
        }

        public List<Benefit> getBenefits() {
            return benefits;
        }

        public List<Category> getCategories() {
            return categories;
        }
    }
}
