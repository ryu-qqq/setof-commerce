package com.ryuqq.setof.migration.core.checkpoint;

/**
 * 동기화 모드
 *
 * @author development-team
 * @since 1.0.0
 */
public enum SyncMode {
    /** 초기 마이그레이션 (PK 기반 순차 처리) */
    INITIAL,

    /** 증분 동기화 (UPDATE_DATE 기반) */
    INCREMENTAL,

    /** 전체 재동기화 */
    FULL
}
