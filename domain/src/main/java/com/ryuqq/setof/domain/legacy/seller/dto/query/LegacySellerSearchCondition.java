package com.ryuqq.setof.domain.legacy.seller.dto.query;

/**
 * LegacySellerSearchCondition - 레거시 판매자 검색 조건 DTO.
 *
 * <p>Repository 검색 파라미터용 DTO입니다.
 *
 * @param sellerId 판매자 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacySellerSearchCondition(Long sellerId) {

    /**
     * 판매자 ID로 조회하는 생성자.
     *
     * @param sellerId 판매자 ID
     * @return LegacySellerSearchCondition
     */
    public static LegacySellerSearchCondition ofSellerId(Long sellerId) {
        return new LegacySellerSearchCondition(sellerId);
    }

    /**
     * 전체 조회용 빈 조건.
     *
     * @return LegacySellerSearchCondition
     */
    public static LegacySellerSearchCondition empty() {
        return new LegacySellerSearchCondition(null);
    }
}
