package com.ryuqq.setof.domain.member.vo;

/**
 * 회원 상태 Enum
 *
 * <ul>
 *   <li>ACTIVE: 정상 활동 중
 *   <li>INACTIVE: 휴면 상태
 *   <li>SUSPENDED: 정지 상태
 *   <li>WITHDRAWN: 탈퇴 상태
 * </ul>
 */
public enum MemberStatus {
    ACTIVE("활동"),
    INACTIVE("휴면"),
    SUSPENDED("정지"),
    WITHDRAWN("탈퇴");

    private final String displayName;

    MemberStatus(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
