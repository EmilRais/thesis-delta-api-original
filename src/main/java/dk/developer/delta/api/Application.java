package dk.developer.delta.api;

import dk.developer.facebook.Facebook;

public class Application extends dk.developer.server.Application {
    private static UserFactory userFactory;
    private static Timestamper timestamper;
    private static Mailer mailer;

    public static Facebook facebook() {
        return Facebook.facebook("431442888312921", "3838abccc24c2c136579123d");
    }

    public static UserFactory userFactory() {
        return userFactory;
    }

    public static UserFactory userFactory(UserFactory userFactory) {
        Application.userFactory = userFactory;
        return userFactory;
    }

    public static Timestamper timestamper() {
        return timestamper;
    }

    public static Timestamper timestamper(Timestamper timestamper) {
        Application.timestamper = timestamper;
        return timestamper;
    }

    public static Mailer mailer() { return mailer; }

    public static Mailer mailer(Mailer mailer) {
        Application.mailer = mailer;
        return mailer;
    }
}