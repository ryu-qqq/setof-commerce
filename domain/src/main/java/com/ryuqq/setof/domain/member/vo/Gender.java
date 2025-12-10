package com.ryuqq.setof.domain.member.vo;

/**
 * 성별 Enum
 *
 * <ul>
 *   <li>M: 남성 (Male)
 *   <li>W: 여성 (Woman)
 *   <li>N: 미지정 (Not specified)
 * </ul>
 */
public enum Gender {
    M("남성"),
    W("여성"),
    N("미지정");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
