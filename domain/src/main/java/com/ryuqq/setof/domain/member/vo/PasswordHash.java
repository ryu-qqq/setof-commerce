package com.ryuqq.setof.domain.member.vo;

/**
 * 비밀번호 해시 Value Object.
 *
 * <p>평문 비밀번호가 아닌 해시된 값을 보관합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PasswordHash(String value) {

    public PasswordHash {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("비밀번호 해시는 필수입니다");
        }
    }

    public static PasswordHash of(String value) {
        return new PasswordHash(value);
    }
}
