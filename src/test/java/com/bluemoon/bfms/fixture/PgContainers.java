package com.bluemoon.bfms.fixture;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;
import java.util.Objects;

public final class PgContainers {
    private PgContainers() {
    }

    /**
     * schema + permissions, DB rỗng. Dùng chung cả JVM, nên test phải dùng
     * unique*().
     */
    public static final PostgreSQLContainer<?> PLAIN = create(false);

    /** Bản đã chạy seed_data.sql. Chỉ khởi tạo khi test đầu tiên chạm tới. */
    public static PostgreSQLContainer<?> seeded() {
        return Holder.SEEDED;
    }

    private static final class Holder {
        static final PostgreSQLContainer<?> SEEDED = create(true);
    }

    private static PostgreSQLContainer<?> create(boolean seeded) {
        PostgreSQLContainer<?> c = new PostgreSQLContainer<>("postgres:16-alpine")
                .withCopyFileToContainer(MountableFile.forClasspathResource("db/00_test_role.sql"),
                        "/docker-entrypoint-initdb.d/00_role.sql")
                .withCopyFileToContainer(MountableFile.forClasspathResource("db/schema.sql"),
                        "/docker-entrypoint-initdb.d/10_schema.sql")
                .withCopyFileToContainer(MountableFile.forClasspathResource("db/permissions.sql"),
                        "/docker-entrypoint-initdb.d/20_permissions.sql");
        if (seeded) {
            c.withCopyFileToContainer(MountableFile.forClasspathResource("db/seed_data.sql"),
                    "/docker-entrypoint-initdb.d/30_seed_data.sql");
        }
        c.start();
        return c;
    }

    /** Kết nối bằng owner: dùng để dựng fixture. Mỗi câu lệnh tự commit. */
    public static JdbcTemplate ownerJdbc(PostgreSQLContainer<?> c) {
        return new JdbcTemplate(new DriverManagerDataSource(
                Objects.requireNonNull(c.getJdbcUrl()),
                Objects.requireNonNull(c.getUsername()),
                Objects.requireNonNull(c.getPassword())));
    }

    /** Kết nối bằng bluemoon_app: dùng để kiểm tra quyền (permissions.sql). */
    public static JdbcTemplate appJdbc(PostgreSQLContainer<?> c) {
        Objects.requireNonNull(c, "Container must not be null");

        return new JdbcTemplate(new DriverManagerDataSource(
                Objects.requireNonNull(c.getJdbcUrl()),
                "bluemoon_app",
                "bluemoon_test_pw"));
    }
}
