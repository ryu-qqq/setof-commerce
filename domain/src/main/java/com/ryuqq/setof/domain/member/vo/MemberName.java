package com.ryuqq.setof.domain.member.vo;

/**
 * 회원 이름 Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record MemberName(String value) {

    private static final int MAX_LENGTH = 50;

    public MemberName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("회원 이름은 필수입니다");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("회원 이름은 %d자를 초과할 수 없습니다: %d자", MAX_LENGTH, value.length()));
        }
    }

    public static MemberName of(String value) {
        return new MemberName(value);
    }
}
