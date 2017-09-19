package dk.developer.delta.api.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.developer.database.DatabaseFront;
import dk.developer.delta.api.concepts.*;
import dk.developer.delta.api.Application;
import dk.developer.delta.api.Mailer;
import dk.developer.delta.api.Timestamper;
import dk.developer.delta.api.UserFactory;
import dk.developer.security.Credential;
import dk.developer.security.Security;
import dk.developer.security.SecurityProvider;
import dk.developer.server.Server;
import dk.developer.validation.single.Id;
import dk.developer.validation.single.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static dk.developer.delta.api.Status.*;
import static dk.developer.security.Security.Mechanism.LOGIN;
import static dk.developer.security.Security.Mechanism.NONE;

@Path("user")
public class UserService {
    private final DatabaseFront database;
    private final SecurityProvider security;
    private final UserFactory userFactory;
    private final Timestamper timestamper;
    private final Mailer mailer;

    public UserService() {
        database = Application.database();
        security = Application.security();
        userFactory = Application.userFactory();
        timestamper = Application.timestamper();
        mailer = Application.mailer();
    }

    @POST
    @Path("login")
    @Security(NONE)
    public Response login(@HeaderParam(Security.HEADER) Credential credential) {
        if ( !security.isValid(credential) )
            return Server.respond("Not a valid Facebook user").as(INVALID_CREDENTIAL);

        User user = database.load(User.class).matching("userId").with(credential.getUserId());
        boolean userAlreadyExists = user != null;
        if ( userAlreadyExists ) {
            user.setLastLogin(timestamper.timestamp());
            database.update(user);
            return Server.respond().as(EXISTING_USER_LOGGED_IN);
        }

        User newUser = userFactory.create(credential);

        UserSettings settings = createDefaultUserSettings();
        database.save(settings);

        newUser.setSettings(settings);
        newUser.setLastLogin(timestamper.timestamp());
        database.save(newUser);

        return Server.respond().as(NEW_USER_LOGGED_IN);
    }

    @GET
    @Path("/")
    @Security(LOGIN)
    public Response loadUser(@HeaderParam(Security.HEADER) Credential credential) {
        User user = database.load(User.class).matching("userId").with(credential.getUserId());
        return Server.respond(user).as(LOADED_USER);
    }

    @POST
    @Path("login/refresh")
    @Security(LOGIN)
    public Response refreshLogin(@HeaderParam(Security.HEADER) Credential credential) {
        User user = database.load(User.class).matching("userId").with(credential.getUserId());
        user.setLastLogin(timestamper.timestamp());
        database.update(user);
        return Server.respond().as(EXISTING_USER_LOGGED_IN);
    }

    private UserSettings createDefaultUserSettings() {
        Configuration configuration = database.loadAll(Configuration.class).everything().get(0);
        return new UserSettings(configuration.getDefaultOnlyOwnBenefits(), configuration.getDefaultNumberOfLocations());
    }

    @POST
    @Path("settings")
    @Security(LOGIN)
    public Response settings(@NotNull(message = "Settings were missing") @Valid UserSettingsConstructor constructor,
                             @HeaderParam(Security.HEADER) Credential credential) {
        User user = database.load(User.class).matching("userId").with(credential.getUserId());

        UserSettings settings = user.getSettings();
        settings.setOnlyOwnBenefits(constructor.getOnlyOwnBenefits());
        settings.setNumberOfLocations(constructor.getNumberOfLocations());

        boolean didUpdate = database.update(settings);
        if ( !didUpdate )
            return Server.reportError("Settings were not updated due to an error");

        return Server.respond(user).as(SETTINGS_UPDATED);
    }

    @POST
    @Path("subscribe")
    @Security(LOGIN)
    public Response subscribe(@NotNull(message = "Subscription was missing")  @Valid SubscriptionConstructor constructor,
                              @HeaderParam(Security.HEADER) Credential credential) {
        User user = database.load(User.class).matching("userId").with(credential.getUserId());
        BenefitClub benefitClub = database.load(BenefitClub.class).matching("_id").with(constructor.getBenefitClubId());

        if ( benefitClub == null )
            return Server.reportError("Benefit club does not exist");

        if ( subscriptionExists(user, benefitClub) )
            return Server.reportError("User has already subscribed to that benefit club");

        Subscription subscription = new Subscription(user.getId(), benefitClub.getId(), constructor.getMembershipNumber(), constructor.getMembershipLevel(), timestamper.timestamp());
        database.save(subscription);

        User updatedUser = database.load(User.class).matching("_id").with(user.getId());
        return Server.respond(updatedUser).as(SUBSCRIPTION_SUBMITTED);
    }

    private boolean subscriptionExists(User user, BenefitClub benefitClub) {
        List<Subscription> allSubscriptions = database.loadAll(Subscription.class).everything();
        return allSubscriptions.stream()
                .filter(subscription -> subscription.getUserId().equals(user.getId()))
                .anyMatch(subscription -> subscription.getBenefitClubId().equals(benefitClub.getId()));
    }

    @POST
    @Path("subscribe/update")
    @Security(LOGIN)
    public Response updateSubscription(@NotNull(message = "Subscription was missing")  @Valid Subscription subscription,
                              @HeaderParam(Security.HEADER) Credential credential) {
        User user = database.load(User.class).matching("userId").with(credential.getUserId());
        BenefitClub benefitClub = database.load(BenefitClub.class).matching("_id").with(subscription.getBenefitClubId());

        if ( benefitClub == null )
            return Server.reportError("Benefit club does not exist");

        if ( !user.getId().equals(subscription.getUserId()) )
            return Server.reportError("A user can only update own subscriptions");

        boolean didUpdate = database.update(subscription);
        if ( !didUpdate )
            return Server.reportError("Unable to update subscription");

        User updatedUser = database.load(User.class).matching("_id").with(user.getId());
        return Server.respond(updatedUser).as(SUBSCRIPTION_UPDATED);
    }

    @POST
    @Path("subscribe/delete")
    @Security(LOGIN)
    public Response deleteSubscription(@Id(of = Subscription.class, message = "Subscription does not exist")  String subscriptionId,
                                       @HeaderParam(Security.HEADER) Credential credential) {
        User user = database.load(User.class).matching("userId").with(credential.getUserId());
        Subscription subscription = database.load(Subscription.class).matching("_id").with(subscriptionId);

        if ( !user.getId().equals(subscription.getUserId()) )
            return Server.reportError("A user can only delete own subscriptions");

        boolean didDelete = database.delete(Subscription.class).matching("_id").with(subscriptionId);
        if ( !didDelete )
            return Server.reportError("Unable to delete subscription");

        User updatedUser = database.load(User.class).matching("_id").with(user.getId());
        return Server.respond(updatedUser).as(SUBSCRIPTION_DELETED);
    }

    @GET
    @Path("permission/status")
    @Security(NONE)
    public Response permissionStatus(@HeaderParam(Security.HEADER) Credential credential) {
        if ( !security.isValid(credential) )
            return Server.respond("Not a valid Facebook user").as(INVALID_CREDENTIAL);

        List<UserPermission> permissions = database.loadAll(UserPermission.class).everything();
        List<UserPermission> userPermissions = permissions.stream()
                .filter(userPermission -> userPermission.getFacebookUserId().equals(credential.getUserId()))
                .collect(Collectors.toList());

        boolean allAreAllowed = userPermissions.stream()
                .allMatch(UserPermission::getAllowed);

        boolean hasGivenFullPermission = userPermissions.size() == 1 && allAreAllowed;
        String status = hasGivenFullPermission ? USER_HAS_GIVEN_PERMISSION : USER_HAS_NOT_GIVEN_PERMISSION;
        return Server.respond().as(status);
    }

    @POST
    @Path("permission")
    @Security(NONE)
    public Response permission(@HeaderParam(Security.HEADER) Credential credential,
                               @NotNull(message = "Payload was missing") @Valid PermissionPayload payload) {
        if ( !security.isValid(credential) )
            return Server.respond("Not a valid Facebook user").as(INVALID_CREDENTIAL);

        List<UserPermission> permissions = database.loadAll(UserPermission.class).everything();
        UserPermission permission = permissions.stream()
                .filter(userPermission -> userPermission.getFacebookUserId().equals(credential.getUserId()))
                .filter(userPermission -> userPermission.getPermissionId().equals(payload.getPermissionId()))
                .findAny().orElse(null);

        if ( permission == null ) {
            UserPermission newPermission = new UserPermission(credential.getUserId(), payload.getPermissionId(), timestamper.timestamp(), payload.getAllowed());
            database.save(newPermission);
            return Server.respond().as(USER_HAS_UPDATED_PERMISSION);
        }

        permission.setAllowed(payload.getAllowed());
        permission.setDate(timestamper.timestamp());
        database.update(permission);
        return Server.respond().as(USER_HAS_UPDATED_PERMISSION);
    }

    @POST
    @Path("feedback")
    @Security(LOGIN)
    public Response feedback(@NotEmpty(message = "Message was empty") String message,
                             @HeaderParam(Security.HEADER) Credential credential) {
        User user = database.load(User.class).matching("userId").with(credential.getUserId());
        Feedback feedback = new Feedback(user.getId(), message);
        database.save(feedback);

        mailer.feedbackMail(message, user.getId(), user.getName());
        return Server.respond().as(FEEDBACK_SUBMITTED);
    }

    @POST
    @Path("request")
    @Security(LOGIN)
    public Response request(@NotNull(message = "Benefit club request was missing") @Valid BenefitClubRequest request,
                            @HeaderParam(Security.HEADER) Credential credential) {
        User user = database.load(User.class).matching("userId").with(credential.getUserId());
        if ( !request.getUserId().equals(user.getId()) )
            return Server.reportError("The logged in user did not issue the request");

        database.save(request);

        String message =  "Klubnavn: " + request.getBenefitClubName() + ", Organisation: " + request.getBenefitClubOrganisation() + ", Beskrivelse: " + request.getBenefitClubDescription();
        mailer.proposalMail(message, user.getId(), user.getEmail(), user.getName(), "Company");

        return Server.respond().as(REQUEST_SUBMITTED);
    }

    public static class PermissionPayload {
        @NotNull(message = "Permission id was missing")
        private Integer permissionId;

        @NotNull(message = "Allowed was missing")
        private Boolean allowed;

        @JsonCreator
        public PermissionPayload(@JsonProperty("permissionId") Integer permissionId,
                                 @JsonProperty("allowed") Boolean allowed) {
            this.permissionId = permissionId;
            this.allowed = allowed;
        }

        public Integer getPermissionId() {
            return permissionId;
        }

        public Boolean getAllowed() {
            return allowed;
        }
    }
}
