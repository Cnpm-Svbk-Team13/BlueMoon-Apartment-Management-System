package com.bluemoon.bfms;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

/**
 * Foundation smoke test for US-001 and US-SEC-01/02 (NFR-02, NFR-05).
 * Verifies framework wiring and the isolated test database, not business acceptance.
 */
@SpringBootTest
@ActiveProfiles("test")
class BlueMoonBfmsApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private DataSource dataSource;

    @Test
    void startsApplicationWithIsolatedH2Database() throws Exception {
        assertThat(context.getBean(BlueMoonBfmsApplication.class)).isNotNull();
        try (var connection = dataSource.getConnection()) {
            assertThat(connection.getMetaData().getURL()).startsWith("jdbc:h2:mem:bluemoon_test");
            assertThat(connection.isValid(2)).isTrue();
        }
    }
}
