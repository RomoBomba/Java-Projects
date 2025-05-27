package com.petapptest;

import com.petapp.util.HibernateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractTestcontainersTest {

    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15.0")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass")
                    .withExposedPorts(5432);

    @BeforeAll
    static void setup() {
        if (!postgres.isRunning()) {
            postgres.start();
        }

        HibernateUtil.configure(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
    }

    @AfterAll
    static void cleanup() {
        HibernateUtil.shutdown();
        postgres.stop();
    }
}