package com.ryuqq.setof.domain.member.id;

/**
 * 회원 ID Value Object.
 *
 * <p>회원(Member)을 식별하는 ID입니다. Auto-increment Long PK를 사용합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MemberId(Long value) {

    public static MemberId of(Long value) {
        return new MemberId(value);
    }
}
