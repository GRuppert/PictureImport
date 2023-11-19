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

    private static boolean test = false;

    public static JPAConnection getInstance() {
        if (instance == null) {
            instance = new JPAConnection();
        }
        return instance;
    }

    private JPAConnection() {
        Map properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url", "jdbc:mysql://127.0.0.1:3306/" + (isTest() ? "test" : "") + "pictureorganizer?useUnicode=yes&amp&characterEncoding=UTF-8");
        factory = Persistence.createEntityManagerFactory( "mysql-picture", properties);
    }

    public static boolean isTest() {
        return test;
    }

    public static void setTest(boolean test) {
        JPAConnection.test = test;
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
