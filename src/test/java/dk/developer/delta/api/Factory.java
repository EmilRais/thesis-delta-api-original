package dk.developer.delta.api;

import dk.developer.delta.api.concepts.*;
import dk.developer.security.Credential;

import static dk.developer.utility.Convenience.set;

public class Factory {
    public static class Users {
        public static User thomas() {
            UserSettings settings = Settings.someSettings();
            return new User("thom123", "Thomas Moore", "Thomas", "Moore", "thomas@mail.com", Gender.MALE, "Aalborg, Danmark", "da_DK", "{ \"min\": 21 }", "2016-01-12 18:00:00.000", settings, set());
        }

        public static User peter() {
            UserSettings settings = Settings.someSettings();
            return new User("pete123", "Peter Thomsen", "Peter", "Thomsen", "peter@mail.com", Gender.MALE, "Aalborg, Danmark", "da_DK", "{ \"min\": 21 }", "2016-01-12 18:00:00.000", settings, set());
        }

        public static User joseph() {
            UserSettings settings = Settings.someSettings();
            return new User("jose123", "Joseph Minskj", "Joseph", "Minskj", "joseph@mail.com", Gender.MALE, "Aalborg, Danmark", "da_DK", "{ \"min\": 21 }", "2016-01-12 18:00:00.000", settings, set());
        }
    }

    public static class Settings {
        public static UserSettings someSettings() {
            return new UserSettings(true, 20);
        }
    }

    public static class Subscriptions {
        public static Subscription matasSubscription() {
            return new Subscription("NOT-GENERATED-YET", "NOT-GENERATED-YET", "1234", "1", "2016-01-12 18:00:00.000");
        }
    }

    public static class Permissions {
        public static UserPermission thomasPermission() {
            return new UserPermission("thom123", 1, "2016-01-12 16:00:00.000", true);
        }

        public static UserPermission josephPermission() {
            return new UserPermission("jose123", 1, "2016-01-12 16:00:00.000", false);
        }
    }

    public static class Credentials {
        public static Credential thomas() {
            return new Credential("thom123", "token1234");
        }

        public static Credential joseph() {
            return new Credential("jose123", "token1234");
        }
    }

    public static class BenefitClubs {
        public static BenefitClub matas() {
            return new BenefitClub("Club Matas", "Hos Club Matas får du gode tilbud", "Vis dit medlemskort", "club-matas-image", "Matas A/S", 107);
        }

        public static BenefitClub adidas() {
            return new BenefitClub("Adidas Program", "Adidas Program hjælper dig med at leve livet", null, "adidas-program-image", "Adidas A/S", 34);
        }
    }

    public static class MembershipLevels {
        public static MembershipLevel matasPremium() {
            return new MembershipLevel("NOT-GENERATED-YET", "Premium", "Support the homeless", 1, "matas-premium-image");
        }
    }

    public static class Benefits {
        public static Benefit cheapPerfumes() {
            return new Benefit("NOT-GENERATED-YET", "NOT-GENERATED-YET", "Billige parfumer", "Køb for over 100 kroner");
        }
    }

    public static class BenefitLocations {
        public static BenefitLocation matasLocation() {
            return new BenefitLocation("LocationId", "Matas Aarhus", 12.34, 56.78, null, false, "Ryesgade 13", "Gågaden", "Aarhus C", "8000", "+45 12 34 56 78");
        }
    }

    public static class BenefitPairs {
        public static BenefitTriple matasPerfumePair() {
            return new BenefitTriple("matas-perfume-pair", "NOT-GENERATED-YET", "NOT-GENERATED-YET", "NOT-GENERATED-YET", 0.86);
        }
    }

    public static class Feedbacks {
        public static Feedback positiveFeedback() {
            return new Feedback("NOT-GENERATED-YET", "I am very happy about your app");
        }
    }

    public static class Requests {
        public static BenefitClubRequest legoRequest() {
            return new BenefitClubRequest("NOT-GENERATED-YET", "Leg med Lego", "Lego A/S", "Her leger man med Lego");
        }
    }

    public static class Configurations {
        public static Configuration someConfiguration() {
            return new Configuration("1.0", false, 20, "http://DB1SERVER.cloudapp.net:9200/appbenefits/_search", "Accepterer du?");
        }
    }

    public static class LogEvents {
        public static LogEvent textSearch() {
            return new LogEvent("NOT-GENERATED-YET", LogEventType.SEARCH_TEXT, "Klub", 0);
        }

        public static LogEvent categorySearch() {
            return new LogEvent("NOT-GENERATED-YET", LogEventType.SEARCH_CATEGORY, "mood id: 5", 167);
        }
    }

    public static class Categories {
        public static IndustryCategory industryShop() {
            return new IndustryCategory("Butik");
        }

        public static InterestCategory interestGolf() {
            return new InterestCategory("Golf");
        }

        public static MoodCategory moodRomance() {
            return new MoodCategory("Romantik");
        }

        public static ProductCategory productToys() {
            return new ProductCategory("Toys");
        }

        public static WeatherCategory weatherRain() {
            return new WeatherCategory("Rain");
        }

        public static Category shop() {
            return new Category("NOT-GENERATED-YET", "Butik", CategoryType.INDUSTRY);
        }

        public static Category golf() {
            return new Category("NOT-GENERATED-YET", "Golf", CategoryType.INTEREST);
        }

        public static Category romance() {
            return new Category("NOT-GENERATED-YET", "Romantik", CategoryType.MOOD);
        }

        public static Category toys() {
            return new Category("NOT-GENERATED-YET", "Toys", CategoryType.PRODUCT);
        }

        public static Category rain() {
            return new Category("NOT-GENERATED-YET", "Rain", CategoryType.WEATHER);
        }
    }
}