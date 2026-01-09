package com.ryuqq.setof.batch.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Batch 모듈 DataSource 설정
 *
 * <p>2개의 DataSource를 관리합니다:
 *
 * <ul>
 *   <li>setofDataSource (Primary): 신규 DB + Spring Batch 메타 테이블
 *   <li>legacyDataSource: 레거시 DB
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
public class DataSourceConfig {

    // ========================================
    // Setof DataSource (신규 DB - Primary)
    // Spring Batch 메타 테이블 저장
    // ========================================

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource setofDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @Primary
    public JdbcTemplate setofJdbcTemplate(DataSource setofDataSource) {
        return new JdbcTemplate(setofDataSource);
    }

    @Bean
    @Primary
    public NamedParameterJdbcTemplate setofNamedJdbcTemplate(DataSource setofDataSource) {
        return new NamedParameterJdbcTemplate(setofDataSource);
    }

    // ========================================
    // Legacy DataSource (레거시 DB)
    // ========================================

    @Bean
    @ConfigurationProperties(prefix = "datasource.legacy")
    public DataSource legacyDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public JdbcTemplate legacyJdbcTemplate(
            @Qualifier("legacyDataSource") DataSource legacyDataSource) {
        return new JdbcTemplate(legacyDataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate legacyNamedJdbcTemplate(
            @Qualifier("legacyDataSource") DataSource legacyDataSource) {
        return new NamedParameterJdbcTemplate(legacyDataSource);
    }

    // ========================================
    // Transaction Managers
    // ========================================

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(DataSource setofDataSource) {
        return new DataSourceTransactionManager(setofDataSource);
    }

    @Bean
    public PlatformTransactionManager batchTransactionManager(DataSource setofDataSource) {
        return new DataSourceTransactionManager(setofDataSource);
    }

    @Bean
    public PlatformTransactionManager legacyTransactionManager(DataSource legacyDataSource) {
        return new DataSourceTransactionManager(legacyDataSource);
    }
}
