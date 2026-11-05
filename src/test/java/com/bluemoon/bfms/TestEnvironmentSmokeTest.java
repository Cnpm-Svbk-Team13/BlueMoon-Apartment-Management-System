package com.bluemoon.bfms;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

class TestEnvironmentSmokeTest extends BaseTest {

    @Autowired
    private Environment env;
    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("Profile 'test' đang active")
    void profileTestIsActive() {
        assertThat(env.getActiveProfiles()).contains("test");
    }

    @Test
    @DisplayName("DataSource là H2 in-memory, không phải PostgreSQL thật")
    void usesH2InMemory() throws Exception {
        try (var conn = dataSource.getConnection()) {
            assertThat(conn.getMetaData().getURL()).startsWith("jdbc:h2:mem:");
        }
    }
}
