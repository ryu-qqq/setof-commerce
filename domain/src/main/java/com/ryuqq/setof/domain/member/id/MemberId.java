package com.ryuqq.setof.domain.member.id;

/**
 * 회원 ID Value Object.
 *
 * <p>회원(Member)을 식별하는 ID입니다. 외부에서 UUIDv7을 주입받습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MemberId(String value) {

    public static MemberId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("MemberId 값은 null 또는 빈 문자열일 수 없습니다");
        }
        return new MemberId(value);
    }

    public static MemberId forNew(String value) {
        return of(value);
    }
}
