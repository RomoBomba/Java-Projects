package com.petapp.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.petapp.entities.Owner;
import com.petapp.entities.Pet;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static void configure(String url, String user, String password) {
        try {
            Configuration config = new Configuration();

            config.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            config.setProperty("hibernate.connection.url", url);
            config.setProperty("hibernate.connection.username", user);
            config.setProperty("hibernate.connection.password", password);
            config.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            config.setProperty("hibernate.hbm2ddl.auto", "create-drop");
            config.setProperty("hibernate.show_sql", "true");

            config.addAnnotatedClass(Owner.class);
            config.addAnnotatedClass(Pet.class);

            sessionFactory = config.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Test config error: " + ex.getMessage());
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}