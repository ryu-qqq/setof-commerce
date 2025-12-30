package com.ryuqq.setof.migration.core;

import java.time.Instant;

/**
 * 동기화 상태
 *
 * @param id ID
 * @param domainName 도메인명
 * @param lastSyncAt 마지막 동기화 시간
 * @param lastSyncedCount 마지막 동기화 건수
 * @param totalSyncedCount 누적 동기화 건수
 * @param status 상태 (ACTIVE, PAUSED, COMPLETED)
 * @param syncIntervalMinutes 동기화 주기 (분)
 * @param errorMessage 에러 메시지
 * @author development-team
 * @since 1.0.0
 */
public record SyncStatus(
        Long id,
        String domainName,
        Instant lastSyncAt,
        long lastSyncedCount,
        long totalSyncedCount,
        SyncStatusType status,
        int syncIntervalMinutes,
        String errorMessage) {

    /** 활성화 상태인지 확인 */
    public boolean isActive() {
        return status == SyncStatusType.ACTIVE;
    }

    /**
     * 동기화 필요 여부 확인
     *
     * @param now 현재 시간
     * @return 주기가 지났으면 true
     */
    public boolean needsSync(Instant now) {
        if (!isActive()) {
            return false;
        }
        Instant nextSyncTime = lastSyncAt.plusSeconds(syncIntervalMinutes * 60L);
        return now.isAfter(nextSyncTime);
    }
}
