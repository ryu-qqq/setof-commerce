package com.ryuqq.setof.storage.legacy.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * LegacyJpaConfig - 레거시 DB용 JPA 설정.
 *
 * <p>레거시 DB 연결을 위한 JPA 및 QueryDSL 설정입니다. Strangler Fig 패턴의 Phase 1에서 레거시 스키마 접근을 위해 사용됩니다.
 *
 * <p>활성화 조건: persistence.legacy.enabled=true
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(name = "persistence.legacy.enabled", havingValue = "true")
@EntityScan(basePackages = "com.ryuqq.setof.storage.legacy")
@EnableJpaRepositories(basePackages = "com.ryuqq.setof.storage.legacy")
@EnableTransactionManagement
public class LegacyJpaConfig {

    /**
     * 레거시 DB용 JPAQueryFactory.
     *
     * @param entityManager EntityManager
     * @return JPAQueryFactory
     */
    @Bean(name = "legacyJpaQueryFactory")
    public JPAQueryFactory legacyJpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
