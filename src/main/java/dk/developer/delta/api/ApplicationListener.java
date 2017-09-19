package dk.developer.delta.api;

import dk.developer.database.HibernateDatabase;
import dk.developer.security.FacebookSecurityProvider;
import dk.developer.security.SecurityProvider;
import org.hibernate.SessionFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static dk.developer.facebook.Permission.*;

public class ApplicationListener implements ServletContextListener {
    private SessionFactory factory;

    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Initialising application");

        attachDatabase();
        attachSecurityProvider();
        attachUserFactory();
        attachTimestamper();
        attachMailer();

        System.out.println("Done initialising application");
    }

    private void attachDatabase() {
        System.out.println("Connecting to production database");
        factory = new HibernateDatabase.SessionFactoryBuilder("production.cfg.xml").create();
        Application.database(new HibernateDatabase(factory));

        System.out.println("Now using hibernate production database");
    }

    private void attachSecurityProvider() {
        System.out.println("Attaching security provider");

        SecurityProvider securityProvider = new FacebookSecurityProvider(Application.facebook(), PUBLIC_PROFILE, EMAIL);
        Application.security(securityProvider);

        System.out.println("Now using Facebook security provider");
    }

    private void attachUserFactory() {
        System.out.println("Attaching user factory");

        UserFactory factory = FacebookUserFactory.create(Application.facebook());
        Application.userFactory(factory);

        System.out.println("Now using Facebook user factory");
    }

    private void attachTimestamper() {
        System.out.println("Attaching time stamper");

        Timestamper timestamper = new CurrentTimeTimestamper();
        Application.timestamper(timestamper);

        System.out.println("Now using current time timestamper");
    }

    private void attachMailer() {
        System.out.println("Attaching mailer");

        MandrillMailer mailer = new MandrillMailer("support@mail.com");
        Application.mailer(mailer);

        System.out.println("Now using Mandrill mailer");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Destroying application");

        Application.database(null);
        factory.close();

        Application.security(null);
        Application.userFactory(null);
        Application.timestamper(null);
        Application.mailer(null);

        System.out.println("Application succesfully destroyed");
    }
}