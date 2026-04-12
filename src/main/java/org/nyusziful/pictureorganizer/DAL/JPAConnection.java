package org.nyusziful.pictureorganizer.DAL;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.sql.Connection;
import java.sql.DriverManager;
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

    private static final String H2_JDBC_URL =
            "jdbc:h2:mem:testpictorg;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";

    private JPAConnection() {
        if (mode == DBMode.TEST) {
            // Run Liquibase to create the H2 schema from the versioned changelogs,
            // then hand the already-initialised DataSource to Hibernate (hbm2ddl=none).
            applyLiquibaseSchema(H2_JDBC_URL);
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

    /**
     * Runs all Liquibase changelogs from {@code db/changelog/db.changelog-master.xml}
     * against the given JDBC URL.  The H2-specific changesets will create the current
     * schema; MySQL-only changesets are silently skipped by Liquibase's {@code dbms} filter.
     */
    private static void applyLiquibaseSchema(String jdbcUrl) {
        try {
            Class.forName("org.h2.Driver");
            try (Connection conn = DriverManager.getConnection(jdbcUrl, "sa", "")) {
                liquibase.database.Database db =
                        liquibase.database.DatabaseFactory.getInstance()
                                .findCorrectDatabaseImplementation(
                                        new liquibase.database.jvm.JdbcConnection(conn));
                liquibase.Liquibase liquibase = new liquibase.Liquibase(
                        "db/changelog/db.changelog-master.xml",
                        new liquibase.resource.ClassLoaderResourceAccessor(),
                        db);
                liquibase.update(new liquibase.Contexts());
            }
        } catch (Exception e) {
            throw new RuntimeException("Liquibase schema initialisation failed for " + jdbcUrl, e);
        }
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
