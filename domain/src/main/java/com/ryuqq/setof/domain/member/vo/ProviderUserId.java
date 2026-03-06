package com.ryuqq.setof.domain.member.vo;

/**
 * 인증 제공자의 사용자 식별자 Value Object.
 *
 * <p>PHONE: 전화번호, KAKAO: 카카오 고유 ID
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProviderUserId(String value) {

    public ProviderUserId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("인증 제공자 사용자 ID는 필수입니다");
        }
        value = value.trim();
    }

    public static ProviderUserId of(String value) {
        return new ProviderUserId(value);
    }
}
