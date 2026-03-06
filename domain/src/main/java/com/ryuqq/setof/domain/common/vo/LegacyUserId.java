package com.ryuqq.setof.domain.common.vo;

/**
 * 레거시 사용자 ID Value Object.
 *
 * <p>레거시 시스템의 auto-increment Long 타입 사용자 ID를 래핑합니다. 마이그레이션 기간 동안 레거시 DB 조회에 사용됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyUserId(long value) {

    public LegacyUserId {
        if (value <= 0) {
            throw new IllegalArgumentException("레거시 사용자 ID는 양수여야 합니다: " + value);
        }
    }

    public static LegacyUserId of(long value) {
        return new LegacyUserId(value);
    }
}
