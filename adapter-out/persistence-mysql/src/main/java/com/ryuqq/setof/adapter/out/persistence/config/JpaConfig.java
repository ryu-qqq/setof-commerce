package com.ryuqq.setof.adapter.out.persistence.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JpaConfig - JPA 및 QueryDSL 설정
 *
 * <p>Persistence Layer의 JPA 및 QueryDSL 설정을 담당합니다.
 *
 * <p><strong>설정 내용:</strong>
 *
 * <ul>
 *   <li>JPA Repository 스캔 경로 설정
 *   <li>Entity 스캔 경로 설정
 *   <li>JPAQueryFactory Bean 등록
 * </ul>
 *
 * <p><strong>QueryDSL 사용:</strong>
 *
 * <ul>
 *   <li>JPAQueryFactory는 QueryDslRepository에서 주입받아 사용
 *   <li>EntityManager와 연동하여 쿼리 실행
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.ryuqq.setof.storage.mysql")
@EntityScan(basePackages = "com.ryuqq.setof.storage.mysql")
public class JpaConfig {

    @PersistenceContext private EntityManager entityManager;

    /**
     * JPAQueryFactory Bean 등록
     *
     * <p>QueryDSL에서 사용하는 JPAQueryFactory를 Bean으로 등록합니다.
     *
     * <p>QueryDslRepository에서 생성자 주입으로 사용합니다.
     *
     * @return JPAQueryFactory 인스턴스
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
