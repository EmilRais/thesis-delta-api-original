package dk.developer.delta.api;

import dk.developer.server.Server;

public class Status extends Server.Status {
    public static final String NEW_USER_LOGGED_IN = "NewUserLoggedIn";
    public static final String EXISTING_USER_LOGGED_IN = "ExistingUserLoggedIn";

    public static final String USER_HAS_GIVEN_PERMISSION = "UserHasGivenPermission";
    public static final String USER_HAS_NOT_GIVEN_PERMISSION = "UserHasNotGivenPermission";
    public static final String USER_HAS_UPDATED_PERMISSION = "UserHasUpdatedPermission";

    public static final String LOADED_USER = "LoadedUser";
    public static final String LOADED_EVERYTHING = "LoadedEverything";
    public static final String LOADED_CONFIGURATION = "LoadedConfiguration";

    public static final String LOGGED_BENEFIT_PAIR_VISIT = "LoggedBenefitPairVisit";

    public static final String SUBSCRIPTION_SUBMITTED = "SubscriptionSubmitted";
    public static final String SUBSCRIPTION_UPDATED = "SubscriptionUpdated";
    public static final String SUBSCRIPTION_DELETED = "SubscriptionDeleted";

    public static final String FEEDBACK_SUBMITTED = "FeedbackSubmitted";
    public static final String REQUEST_SUBMITTED = "RequestSubmitted";

    public static final String SETTINGS_UPDATED = "SettingsUpdated";

    public static final String PLAIN_SEARCH = "PlainSearch";
    public static final String TEXT_SERACH = "TextSearch";
    public static final String CATEGORY_SEARCH = "CategorySearch";
    public static final String BENEFIT_CLUB_SEARCH = "BenefitClubSearch";

    private Status() {}
}
