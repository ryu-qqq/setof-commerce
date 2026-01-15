package com.ryuqq.setof.migration.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 3개 DataSource 설정
 *
 * <p>마이그레이션 애플리케이션은 3개의 DataSource를 사용합니다:
 *
 * <ul>
 *   <li><b>migrationDataSource</b>: Spring Batch 메타 테이블 + migration_checkpoint (Primary)
 *   <li><b>legacyDataSource</b>: 레거시 DB (읽기 전용)
 *   <li><b>setofDataSource</b>: 신규 DB (쓰기)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
public class DataSourceConfig {

    // ========================================
    // Migration DataSource (Primary)
    // Spring Batch 메타 테이블이 여기에 생성됨
    // ========================================

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties migrationDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "migrationDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource migrationDataSource() {
        return migrationDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "migrationJdbcTemplate")
    @Primary
    public JdbcTemplate migrationJdbcTemplate(
            @Qualifier("migrationDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // ========================================
    // Legacy DataSource (읽기 전용)
    // ========================================

    @Bean
    @ConfigurationProperties("datasource.legacy")
    public DataSourceProperties legacyDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "legacyDataSource")
    @ConfigurationProperties("datasource.legacy.hikari")
    public HikariDataSource legacyDataSource() {
        return legacyDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "legacyJdbcTemplate")
    public JdbcTemplate legacyJdbcTemplate(@Qualifier("legacyDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // ========================================
    // Setof DataSource (신규 DB - 쓰기)
    // ========================================

    @Bean
    @ConfigurationProperties("datasource.setof")
    public DataSourceProperties setofDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "setofDataSource")
    @ConfigurationProperties("datasource.setof.hikari")
    public HikariDataSource setofDataSource() {
        return setofDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "setofJdbcTemplate")
    public JdbcTemplate setofJdbcTemplate(@Qualifier("setofDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
