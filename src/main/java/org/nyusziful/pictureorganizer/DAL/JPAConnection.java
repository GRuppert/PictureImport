package org.nyusziful.pictureorganizer.DAL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class JPAConnection {
    private EntityManager entityManager;
    private EntityManagerFactory factory;
    private static JPAConnection instance;

    public enum DBMode {
        TEST, DEV, PROD
    }
    private static DBMode mode = DBMode.PROD;

    public static JPAConnection getInstance() {
        if (instance == null) {
            instance = new JPAConnection();
        }
        return instance;
    }

    private JPAConnection() {
        if (mode == DBMode.TEST) {
            // H2 in-memory DB — Hibernate generates schema from entity annotations (create-drop)
            factory = Persistence.createEntityManagerFactory("h2-picture");
        } else {
            Map<String, String> properties = new HashMap<>();
            String dbPrefix = isDev() ? "dev" : "";
            properties.put("jakarta.persistence.jdbc.url",
                    "jdbc:mysql://127.0.0.1:3306/" + dbPrefix + "pictureorganizer?useUnicode=yes&characterEncoding=UTF-8");
            factory = Persistence.createEntityManagerFactory("mysql-picture", properties);
        }
    }

    /**
     * Destroys the singleton so that a subsequent {@link #getInstance()} call
     * creates a fresh instance — useful in tests that need to switch modes between runs.
     *
     * <p>Call {@link #shutdown()} first to close any open EntityManager/factory.
     */
    public static void resetInstance() {
        instance = null;
    }

    public static boolean isDev() {
        return DBMode.DEV.equals(getMode());
    }

    public static boolean isTest() {
        return DBMode.TEST.equals(getMode());
    }

    public static DBMode getMode() {
        return mode;
    }
    public static void setMode(DBMode mode) {
        JPAConnection.mode = mode;
    }

    public EntityManager getEntityManager() {
        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = factory.createEntityManager();
        }
        EntityTransaction transaction = entityManager.getTransaction();
        if (transaction == null || !transaction.isActive())
            transaction.begin();
        return entityManager;
    }


    public void closeEntityManager() {
        if (entityManager != null)
            entityManager.close();
    }

    public static void shutdown() {
        if (instance != null) {
            if (instance.factory != null ) {
                instance.closeEntityManager();
                instance.factory.close();
            }
        }
    }

    /**
     * Might fail as calling it directly
     */
    public void rollback() {
        getEntityManager().getTransaction().rollback();
    }

    /**
     * Might fail as calling it directly
     */
    public void commit() {
        getEntityManager().getTransaction().commit();
    }

}
