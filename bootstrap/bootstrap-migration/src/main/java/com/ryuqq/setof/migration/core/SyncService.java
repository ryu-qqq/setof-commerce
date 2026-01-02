package com.ryuqq.setof.migration.core;

import java.time.Instant;

/**
 * 도메인별 동기화 서비스 인터페이스
 *
 * <p>각 도메인(Member, Product 등)은 이 인터페이스를 구현하여 레거시 DB → 신규 DB 동기화 로직을 제공합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface SyncService {

    /**
     * 도메인명 반환
     *
     * <p>sync_status 테이블의 domain_name과 일치해야 합니다.
     *
     * @return 도메인명 (예: "member", "product")
     */
    String getDomainName();

    /**
     * 초기 마이그레이션 실행 (전체 데이터)
     *
     * <p>Phase 1에서 사용. 레거시 DB의 모든 데이터를 신규 DB로 복사합니다.
     *
     * @return 동기화 결과
     */
    SyncResult initialMigration();

    /**
     * 증분 동기화 실행 (변경분만)
     *
     * <p>Phase 2에서 사용. lastSyncAt 이후 변경된 데이터만 동기화합니다.
     *
     * @param lastSyncAt 마지막 동기화 시간
     * @return 동기화 결과
     */
    SyncResult incrementalSync(Instant lastSyncAt);

    /**
     * 동기화 지원 여부
     *
     * @return 지원하면 true
     */
    default boolean isEnabled() {
        return true;
    }
}
