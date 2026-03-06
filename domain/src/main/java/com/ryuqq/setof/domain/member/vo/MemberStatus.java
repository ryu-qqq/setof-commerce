package com.ryuqq.setof.domain.member.vo;

/**
 * 회원 상태 Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum MemberStatus {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    SUSPENDED("정지"),
    WITHDRAWN("탈퇴");

    private final String displayName;

    MemberStatus(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public boolean canLogin() {
        return this == ACTIVE;
    }
}
