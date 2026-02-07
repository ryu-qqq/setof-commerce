package com.ryuqq.setof.domain.legacy.banner.dto.query;

/**
 * LegacyBannerSearchCondition - 레거시 배너 검색 조건 DTO.
 *
 * <p>Repository 검색 파라미터용 DTO입니다.
 *
 * @param bannerType 배너 타입 (CATEGORY, MY_PAGE, CART, PRODUCT_DETAIL_DESCRIPTION, RECOMMEND, LOGIN)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyBannerSearchCondition(String bannerType) {

    /**
     * 배너 타입으로 조회하는 생성자.
     *
     * @param bannerType 배너 타입
     * @return LegacyBannerSearchCondition
     */
    public static LegacyBannerSearchCondition ofBannerType(String bannerType) {
        return new LegacyBannerSearchCondition(bannerType);
    }

    /**
     * 배너 타입 존재 여부.
     *
     * @return 배너 타입이 있으면 true
     */
    public boolean hasBannerType() {
        return bannerType != null && !bannerType.isBlank();
    }
}
