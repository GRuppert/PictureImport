package org.nyusziful.pictureorganizer.DAL;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibConnection {
    private Session currentSession;
    private SessionFactory sessionFactory = null;
    private static HibConnection instance = null;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static HibConnection getInstance() {
        if (instance == null) {
            instance = new HibConnection();
        }
        return instance;
    }

    private HibConnection() {
        if (getSessionFactory() == null) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from persistence.xml
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

        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "org.npcc.ccms.model" });
        sessionFactory.setHibernateProperties(hibernateProperties());

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        return properties;
    }



*/
        }
    }

    public void closeSession() {
        if (currentSession != null)
            currentSession.close();
    }

    public Session getCurrentSession()
    {
        Session currentSession = sessionFactory.getCurrentSession();
        if (currentSession.getTransaction() == null || !currentSession.getTransaction().isActive())
            currentSession.beginTransaction();
        return sessionFactory.getCurrentSession();
    }

    public void shutdown() {
        if ( getSessionFactory() != null ) {
            closeSession();
            getSessionFactory().close();
        }
    }

    /**
     * Might fail as calling it directly
     */
    public void rollback() {
        sessionFactory.getCurrentSession().getTransaction().rollback();
    }

    /**
     * Might fail as calling it directly
     */
    public void commit() {
        sessionFactory.getCurrentSession().getTransaction().commit();
    }
}
