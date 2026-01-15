package com.ryuqq.setof.migration.core.checkpoint;

import java.time.Instant;

/**
 * 마이그레이션 체크포인트
 *
 * <p>도메인별 마이그레이션 진행 상태를 추적합니다. PK 기반으로 "어디까지 마이그레이션했는지"를 저장합니다.
 *
 * @param id 체크포인트 ID
 * @param domainName 도메인명 (member, product, order 등)
 * @param lastMigratedId 마지막으로 마이그레이션한 레거시 PK
 * @param migratedCount 누적 마이그레이션 건수
 * @param legacyTotalCount 레거시 전체 건수 (스냅샷)
 * @param status 상태
 * @param errorMessage 에러 메시지 (실패 시)
 * @param lastExecutedAt 마지막 실행 시작 시간
 * @param lastCompletedAt 마지막 실행 완료 시간
 * @param executionTimeMs 마지막 실행 소요시간 (ms)
 * @param lastSyncedAt 마지막 증분 동기화 기준 시점
 * @param syncMode 동기화 모드 (INITIAL, INCREMENTAL, FULL)
 * @param syncIntervalSeconds 증분 동기화 간격 (초)
 * @param lastBatchSize 마지막 배치에서 처리된 건수
 * @param skippedCount 중복/스킵된 누적 건수
 * @param failedCount 실패한 누적 건수
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @author development-team
 * @since 1.0.0
 */
public record MigrationCheckpoint(
        Long id,
        String domainName,
        long lastMigratedId,
        long migratedCount,
        Long legacyTotalCount,
        MigrationCheckpointStatus status,
        String errorMessage,
        Instant lastExecutedAt,
        Instant lastCompletedAt,
        Long executionTimeMs,
        Instant lastSyncedAt,
        SyncMode syncMode,
        int syncIntervalSeconds,
        int lastBatchSize,
        long skippedCount,
        long failedCount,
        Instant createdAt,
        Instant updatedAt) {

    /**
     * 마이그레이션 진행률 계산
     *
     * @return 진행률 (0.0 ~ 100.0), 전체 건수를 모르면 -1
     */
    public double progressPercentage() {
        if (legacyTotalCount == null || legacyTotalCount == 0) {
            return -1;
        }
        return (migratedCount * 100.0) / legacyTotalCount;
    }

    /**
     * 마이그레이션 가능 상태인지 확인
     *
     * @return PENDING, FAILED, PAUSED 상태면 true
     */
    public boolean canMigrate() {
        return status == MigrationCheckpointStatus.PENDING
                || status == MigrationCheckpointStatus.FAILED
                || status == MigrationCheckpointStatus.PAUSED;
    }

    /**
     * 마이그레이션 완료 상태인지 확인
     *
     * @return COMPLETED 상태면 true
     */
    public boolean isCompleted() {
        return status == MigrationCheckpointStatus.COMPLETED;
    }

    /**
     * 마이그레이션 실행 중인지 확인
     *
     * @return RUNNING 상태면 true
     */
    public boolean isRunning() {
        return status == MigrationCheckpointStatus.RUNNING;
    }
}
