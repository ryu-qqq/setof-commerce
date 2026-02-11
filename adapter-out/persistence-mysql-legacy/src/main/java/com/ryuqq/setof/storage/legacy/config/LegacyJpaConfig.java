package com.ryuqq.setof.storage.legacy.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * LegacyJpaConfig - 레거시 DB용 JPA 설정.
 *
 * <p>Strangler Fig 패턴의 Phase 1에서 레거시 스키마 접근을 위해 사용됩니다. 동일 DataSource를 공유하며, 레거시 엔티티와 리포지토리를 조건부로
 * 등록합니다.
 *
 * <p>JPAQueryFactory는 persistence-mysql 모듈의 JpaConfig에서 제공하는 빈을 공유합니다.
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
public class LegacyJpaConfig {}
