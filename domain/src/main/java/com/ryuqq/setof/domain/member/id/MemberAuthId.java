package com.ryuqq.setof.domain.member.id;

/**
 * 회원 인증 수단 ID Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MemberAuthId(Long value) {

    public static MemberAuthId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("MemberAuthId 값은 null일 수 없습니다");
        }
        return new MemberAuthId(value);
    }

    public static MemberAuthId forNew() {
        return new MemberAuthId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
