package com.ryuqq.setof.migration.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Legacy DataSource 설정
 *
 * <p>레거시 DB 연결을 위한 별도의 DataSource를 설정합니다.
 *
 * <p><strong>다중 DataSource 전략:</strong>
 *
 * <ul>
 *   <li>Primary DataSource: 신규 DB (기존 JPA 설정 사용)
 *   <li>Legacy DataSource: 레거시 DB (JdbcTemplate으로 조회 전용)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
public class LegacyDataSourceConfig {

    /**
     * 레거시 DataSource 프로퍼티
     *
     * <p>application.yml의 spring.datasource.legacy.* 설정을 바인딩합니다.
     */
    @Bean
    @ConfigurationProperties("spring.datasource.legacy")
    public DataSourceProperties legacyDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 레거시 DataSource
     *
     * <p>레거시 DB 조회를 위한 DataSource입니다. 쓰기 작업은 절대 수행하지 않습니다.
     */
    @Bean(name = "legacyDataSource")
    public DataSource legacyDataSource() {
        return legacyDataSourceProperties().initializeDataSourceBuilder().build();
    }

    /**
     * 레거시 JdbcTemplate
     *
     * <p>레거시 DB 조회 전용 JdbcTemplate입니다.
     */
    @Bean(name = "legacyJdbcTemplate")
    public JdbcTemplate legacyJdbcTemplate(@Qualifier("legacyDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
