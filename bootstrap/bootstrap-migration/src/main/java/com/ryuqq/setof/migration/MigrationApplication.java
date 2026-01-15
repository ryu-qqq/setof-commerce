package com.ryuqq.setof.migration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Migration Application
 *
 * <p>레거시 DB에서 신규 DB로 데이터를 마이그레이션하는 Spring Batch 기반 애플리케이션입니다.
 *
 * <h2>3개 DataSource 구조</h2>
 *
 * <ul>
 *   <li><b>migrationDataSource</b>: Spring Batch 메타 테이블 + migration_checkpoint
 *   <li><b>legacyDataSource</b>: 레거시 DB (읽기 전용)
 *   <li><b>setofDataSource</b>: 신규 DB (쓰기)
 * </ul>
 *
 * <h2>마이그레이션 전략</h2>
 *
 * <ul>
 *   <li>PK 기반 체크포인트로 진행 상태 추적
 *   <li>Chunk 단위 트랜잭션으로 안전한 배치 처리
 *   <li>실패 시 재시작 지원
 * </ul>
 *
 * <h2>ID 매핑 전략</h2>
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
@ConfigurationPropertiesScan
public class MigrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MigrationApplication.class, args);
    }
}
