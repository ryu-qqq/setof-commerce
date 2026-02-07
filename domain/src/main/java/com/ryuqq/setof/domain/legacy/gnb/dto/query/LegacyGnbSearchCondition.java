package com.ryuqq.setof.domain.legacy.gnb.dto.query;

/**
 * LegacyGnbSearchCondition - 레거시 GNB 검색 조건 DTO.
 *
 * <p>Repository 검색 파라미터용 DTO입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyGnbSearchCondition() {

    /**
     * 전체 GNB 조회용 빈 조건.
     *
     * @return LegacyGnbSearchCondition
     */
    public static LegacyGnbSearchCondition empty() {
        return new LegacyGnbSearchCondition();
    }
}
