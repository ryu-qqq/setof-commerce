package com.ryuqq.setof.storage.legacy.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Legacy QueryDSL 설정.
 *
 * <p>LegacyJpaConfig와 분리하여 순환 참조를 방지합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Configuration
@ConditionalOnProperty(name = "persistence.legacy.enabled", havingValue = "true")
public class LegacyQueryDslConfig {

    @PersistenceContext(unitName = "legacy")
    private EntityManager legacyEntityManager;

    @Bean
    public JPAQueryFactory legacyJpaQueryFactory() {
        return new JPAQueryFactory(legacyEntityManager);
    }
}
