package com.ryuqq.setof.integration.test.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Repository 통합 테스트 설정
 *
 * <p>DataJpaTest에서 필요한 추가 빈들을 등록합니다.
 */
@TestConfiguration
@ComponentScan(
        basePackages = {
            "com.ryuqq.setof.adapter.out.persistence.seller.condition",
            "com.ryuqq.setof.adapter.out.persistence.seller.repository",
            "com.ryuqq.setof.adapter.out.persistence.commoncode.condition",
            "com.ryuqq.setof.adapter.out.persistence.commoncode.repository",
            "com.ryuqq.setof.adapter.out.persistence.commoncodetype.condition",
            "com.ryuqq.setof.adapter.out.persistence.commoncodetype.repository",
            "com.ryuqq.setof.adapter.out.persistence.sellerapplication.condition",
            "com.ryuqq.setof.adapter.out.persistence.sellerapplication.repository",
            "com.ryuqq.setof.adapter.out.persistence.shippingpolicy.condition",
            "com.ryuqq.setof.adapter.out.persistence.shippingpolicy.repository",
            "com.ryuqq.setof.adapter.out.persistence.refundpolicy.condition",
            "com.ryuqq.setof.adapter.out.persistence.refundpolicy.repository"
        })
public class RepositoryTestConfig {

    /**
     * 테스트용 고정 Clock 제공
     *
     * <p>시간 관련 테스트의 일관성을 위해 고정된 시간 사용
     *
     * @return 고정된 Clock
     */
    @Bean
    public Clock clock() {
        return Clock.fixed(Instant.parse("2026-01-29T10:00:00Z"), ZoneId.of("Asia/Seoul"));
    }

    /**
     * QueryDSL용 JPAQueryFactory 빈 등록
     *
     * <p>QueryDslRepository 테스트에 필요합니다.
     *
     * @param entityManager EntityManager
     * @return JPAQueryFactory
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
