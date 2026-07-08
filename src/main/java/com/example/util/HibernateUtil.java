package com.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Builds and holds the single {@link SessionFactory} for every example in this course.
 *
 * <p>Why a helper class? The {@code SessionFactory} is a heavyweight, thread-safe object
 * that is expensive to build. You create it <b>once</b> per application and reuse it.
 * Every {@code XxxDemo} class in this project calls {@link #getSessionFactory()} instead
 * of re-reading the config and rebuilding the factory.</p>
 *
 * <p><b>Spring Data JPA parallel:</b> you never write this class in a Spring Boot app.
 * Spring Boot auto-configures the equivalent {@code EntityManagerFactory} bean for you
 * from {@code application.properties}. This class makes explicit what Spring hides.</p>
 */
public final class HibernateUtil {

    // Built once, when the class is first loaded, then cached for the whole JVM run.
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateUtil() {
        // Utility class: no instances.
    }

    private static SessionFactory buildSessionFactory() {
        try {
            // configure() reads src/main/resources/hibernate.cfg.xml.
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    /** Call once at application shutdown to release the connection pool. */
    public static void shutdown() {
        if (SESSION_FACTORY != null && !SESSION_FACTORY.isClosed()) {
            SESSION_FACTORY.close();
        }
    }
}
