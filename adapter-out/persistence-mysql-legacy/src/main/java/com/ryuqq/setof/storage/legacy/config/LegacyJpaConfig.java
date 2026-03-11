package com.ryuqq.setof.storage.legacy.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * LegacyJpaConfig - 레거시 DB(luxurydb)용 JPA 설정.
 *
 * <p>Strangler Fig 패턴에서 레거시 스키마 접근을 위해 별도 DataSource를 구성합니다. Primary DataSource(setof)와 독립적으로 동작하며,
 * 도메인별 @ConditionalOnProperty로 개별 어댑터를 제어합니다.
 *
 * <p>활성화 조건: persistence.legacy.enabled=true
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Configuration
@ConditionalOnProperty(name = "persistence.legacy.enabled", havingValue = "true")
@EnableJpaRepositories(
        basePackages = "com.ryuqq.setof.storage.legacy",
        entityManagerFactoryRef = "legacyEntityManagerFactory",
        transactionManagerRef = "legacyTransactionManager")
public class LegacyJpaConfig {

    @Bean
    @ConfigurationProperties(prefix = "datasource.legacy.hikari")
    public DataSource legacyDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean legacyEntityManagerFactory(
            @Qualifier("legacyDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.ryuqq.setof.storage.legacy");
        factory.setPersistenceUnitName("legacy");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(false);
        vendorAdapter.setGenerateDdl(false);
        factory.setJpaVendorAdapter(vendorAdapter);

        return factory;
    }

    @Bean
    public PlatformTransactionManager legacyTransactionManager(
            @Qualifier("legacyEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public EntityManager legacyEntityManager(
            @Qualifier("legacyEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
    }

    @Bean
    public JPAQueryFactory legacyJpaQueryFactory(
            @Qualifier("legacyEntityManager") EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
