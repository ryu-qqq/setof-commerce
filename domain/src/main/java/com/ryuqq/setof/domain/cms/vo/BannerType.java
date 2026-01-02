package com.ryuqq.setof.domain.cms.vo;

/**
 * Banner 타입 Enum
 *
 * <p>배너가 표시되는 위치/용도를 나타냅니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public enum BannerType {
    /** 카테고리 페이지 배너 */
    CATEGORY,
    /** 마이페이지 배너 */
    MY_PAGE,
    /** 장바구니 배너 */
    CART,
    /** 상품 상세 설명 배너 */
    PRODUCT_DETAIL_DESCRIPTION,
    /** 추천 배너 */
    RECOMMEND,
    /** 로그인 페이지 배너 */
    LOGIN
}
