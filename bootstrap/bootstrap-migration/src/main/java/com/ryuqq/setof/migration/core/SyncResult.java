package com.ryuqq.setof.migration.core;

import java.time.Duration;
import java.time.Instant;

/**
 * 동기화 실행 결과
 *
 * @param domainName 도메인명
 * @param syncedCount 동기화된 건수
 * @param skippedCount 스킵된 건수 (이미 존재)
 * @param failedCount 실패 건수
 * @param startedAt 시작 시간
 * @param completedAt 완료 시간
 * @param errorMessage 에러 메시지 (실패 시)
 * @author development-team
 * @since 1.0.0
 */
public record SyncResult(
        String domainName,
        long syncedCount,
        long skippedCount,
        long failedCount,
        Instant startedAt,
        Instant completedAt,
        String errorMessage) {

    /** 성공 결과 생성 */
    public static SyncResult success(
            String domainName, long syncedCount, long skippedCount, Instant startedAt) {
        return new SyncResult(
                domainName, syncedCount, skippedCount, 0, startedAt, Instant.now(), null);
    }

    /** 부분 성공 결과 생성 (일부 실패) */
    public static SyncResult partial(
            String domainName,
            long syncedCount,
            long skippedCount,
            long failedCount,
            Instant startedAt,
            String errorMessage) {
        return new SyncResult(
                domainName,
                syncedCount,
                skippedCount,
                failedCount,
                startedAt,
                Instant.now(),
                errorMessage);
    }

    /** 실패 결과 생성 */
    public static SyncResult failure(String domainName, Instant startedAt, String errorMessage) {
        return new SyncResult(domainName, 0, 0, 0, startedAt, Instant.now(), errorMessage);
    }

    /** 성공 여부 */
    public boolean isSuccessful() {
        return failedCount == 0 && errorMessage == null;
    }

    /** 처리된 총 건수 */
    public long totalProcessed() {
        return syncedCount + skippedCount + failedCount;
    }

    /** 소요 시간 */
    public Duration duration() {
        return Duration.between(startedAt, completedAt);
    }

    /** 로그용 요약 문자열 */
    public String toSummary() {
        return String.format(
                "[%s] synced=%d, skipped=%d, failed=%d, duration=%dms%s",
                domainName,
                syncedCount,
                skippedCount,
                failedCount,
                duration().toMillis(),
                errorMessage != null ? ", error=" + errorMessage : "");
    }
}
