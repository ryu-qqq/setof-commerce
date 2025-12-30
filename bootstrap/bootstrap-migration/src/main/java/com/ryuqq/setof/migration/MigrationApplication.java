package com.ryuqq.setof.migration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Migration Application
 *
 * <p>레거시 DB에서 신규 DB로 데이터를 마이그레이션하는 애플리케이션입니다.
 *
 * <p><strong>Strangler Fig 패턴:</strong>
 *
 * <ul>
 *   <li>레거시 Users 테이블 → 신규 members 테이블
 *   <li>레거시 ORDER 테이블 → 신규 orders 테이블
 *   <li>레거시 PAYMENT 테이블 → 신규 payments 테이블
 * </ul>
 *
 * <p><strong>ID 매핑 전략:</strong>
 *
 * <ul>
 *   <li>신규 테이블에 legacy_*_id 컬럼 추가
 *   <li>UUID v7 (신규 PK) ↔ Long (레거시 PK) 매핑
 *   <li>마이그레이션 완료 후 legacy_*_id 컬럼 제거 예정
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@SpringBootApplication(
        scanBasePackages = {"com.ryuqq.setof.migration", "com.ryuqq.setof.adapter.out.persistence"})
@EnableScheduling
public class MigrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MigrationApplication.class, args);
    }
}
