package com.ryuqq.setof.migration.core;

/**
 * 동기화 상태 타입
 *
 * @author development-team
 * @since 1.0.0
 */
public enum SyncStatusType {

    /** 활성화 - 동기화 진행 중 */
    ACTIVE,

    /** 일시 중지 - 수동으로 중지됨 */
    PAUSED,

    /** 완료 - 전환 완료, 더 이상 동기화 불필요 */
    COMPLETED
}
