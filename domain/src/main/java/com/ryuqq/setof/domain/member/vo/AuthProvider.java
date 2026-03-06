package com.ryuqq.setof.domain.member.vo;

/**
 * 인증 제공자 Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum AuthProvider {
    PHONE("전화번호"),
    KAKAO("카카오");

    private final String displayName;

    AuthProvider(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    /** 비밀번호가 필요한 인증 방식인지 확인. */
    public boolean requiresPassword() {
        return this == PHONE;
    }
}
