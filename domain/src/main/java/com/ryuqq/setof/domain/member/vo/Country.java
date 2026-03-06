package com.ryuqq.setof.domain.member.vo;

/**
 * 국가 Value Object.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum Country {
    KR("대한민국"),
    US("미국"),
    JP("일본"),
    CN("중국");

    private final String displayName;

    Country(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
