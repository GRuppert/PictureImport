package org.nyusziful.pictureorganizer.DAL;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JPAConnection {
    private EntityManager entityManager;
    private EntityManagerFactory factory;
    private static JPAConnection instance;

    public static JPAConnection getInstance() {
        if (instance == null) {
            instance = new JPAConnection();
        }
        return instance;
    }

    private JPAConnection() {
        factory = Persistence.createEntityManagerFactory( "mysql-picture" );
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
