package com.ryuqq.setof.migration.core.checkpoint;

/**
 * 마이그레이션 체크포인트 상태
 *
 * @author development-team
 * @since 1.0.0
 */
public enum MigrationCheckpointStatus {

    /** 대기 중 - 아직 마이그레이션 시작 전 */
    PENDING,

    /** 실행 중 - 현재 마이그레이션 진행 중 */
    RUNNING,

    /** 완료 - 마이그레이션 완료 */
    COMPLETED,

    /** 실패 - 마이그레이션 실패 (재시도 필요) */
    FAILED,

    /** 일시 중지 - 수동으로 중지됨 */
    PAUSED
}
