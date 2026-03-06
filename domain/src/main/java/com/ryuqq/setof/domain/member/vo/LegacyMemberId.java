package com.ryuqq.setof.domain.member.vo;

/**
 * 레거시 회원 ID Value Object.
 *
 * <p>레거시 시스템의 user_id (Long)를 참조합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyMemberId(long value) {

    public LegacyMemberId {
        if (value <= 0) {
            throw new IllegalArgumentException("레거시 회원 ID는 양수여야 합니다");
        }
    }

    public static LegacyMemberId of(long value) {
        return new LegacyMemberId(value);
    }
}
