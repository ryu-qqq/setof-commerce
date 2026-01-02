package com.ryuqq.setof.domain.cms.vo;

/**
 * 상품 정렬 타입 Enum
 *
 * @author development-team
 * @since 1.0.0
 */
public enum OrderType {
    /** 최신순 */
    LATEST,
    /** 인기순 */
    POPULAR,
    /** 가격 오름차순 */
    PRICE_ASC,
    /** 가격 내림차순 */
    PRICE_DESC,
    /** 없음 */
    NONE
}
