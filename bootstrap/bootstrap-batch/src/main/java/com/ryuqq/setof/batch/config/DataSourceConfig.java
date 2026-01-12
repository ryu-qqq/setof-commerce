package com.ryuqq.setof.batch.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
 * <p>2단계 바인딩 패턴을 사용하여 프로퍼티를 올바르게 바인딩합니다:
 *
 * <ol>
 *   <li>DataSourceProperties: URL, username, password 등 기본 속성
 *   <li>HikariDataSource: HikariCP 설정 (pool size, timeout 등)
 * </ol>
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
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties setofDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource setofDataSource() {
        return setofDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @Primary
    public JdbcTemplate setofJdbcTemplate(
            @Qualifier("setofDataSource") DataSource setofDataSource) {
        return new JdbcTemplate(setofDataSource);
    }

    @Bean
    @Primary
    public NamedParameterJdbcTemplate setofNamedJdbcTemplate(
            @Qualifier("setofDataSource") DataSource setofDataSource) {
        return new NamedParameterJdbcTemplate(setofDataSource);
    }

    // ========================================
    // Legacy DataSource (레거시 DB)
    // ========================================

    @Bean
    @ConfigurationProperties("datasource.legacy")
    public DataSourceProperties legacyDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("datasource.legacy.hikari")
    public HikariDataSource legacyDataSource() {
        return legacyDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
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
    public PlatformTransactionManager transactionManager(
            @Qualifier("setofDataSource") DataSource setofDataSource) {
        return new DataSourceTransactionManager(setofDataSource);
    }

    @Bean
    public PlatformTransactionManager batchTransactionManager(
            @Qualifier("setofDataSource") DataSource setofDataSource) {
        return new DataSourceTransactionManager(setofDataSource);
    }

    @Bean
    public PlatformTransactionManager legacyTransactionManager(
            @Qualifier("legacyDataSource") DataSource legacyDataSource) {
        return new DataSourceTransactionManager(legacyDataSource);
    }
}
