package org.nyusziful.pictureorganizer.DB;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibConnection {
    private static Session currentSession;
    private static SessionFactory sessionFactory = null;

    private static Session openSession() {
        final SessionFactory sessionFactory = getSessionFactory();
        if (sessionFactory == null) return null;
        currentSession = sessionFactory.openSession();
        return currentSession;
    }

    public static void closeSession() {
        if (currentSession != null)
            currentSession.close();
    }

    private static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build();
            try {
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                    // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
                    // so destroy it manually.
                    System.out.println(e.getMessage());
                    StandardServiceRegistryBuilder.destroy( registry );
            }
/*
            Configuration configuration = new Configuration().configure();
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(builder.build());
*/
        }
        return sessionFactory;
    }

    public static Session getCurrentSession()
    {
        if (currentSession == null || !currentSession.isOpen()) openSession();
        return currentSession;
    }

    public static void shutdown() {
        if ( sessionFactory != null ) {
            closeSession();
            sessionFactory.close();
        }
    }
}
