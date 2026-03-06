package com.ryuqq.setof.domain.member.vo;

/**
 * 성별 Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    OTHER("기타");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
