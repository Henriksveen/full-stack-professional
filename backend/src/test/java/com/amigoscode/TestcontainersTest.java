package com.amigoscode;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TestcontainersTest extends AbstractTestcontainers {

    @Test
    void canStartPostgresDB() {
        assertThat(POSTGRE_SQL_CONTAINER.isRunning()).isTrue();
        assertThat(POSTGRE_SQL_CONTAINER.isCreated()).isTrue();
    }
}