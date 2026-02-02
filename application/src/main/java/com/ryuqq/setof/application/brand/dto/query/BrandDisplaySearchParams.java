package com.ryuqq.setof.application.brand.dto.query;

/**
 * 브랜드 노출 조회용 검색 파라미터.
 *
 * <p>Public API용 간단한 검색 조건입니다.
 *
 * @param brandName 브랜드명 (검색 조건)
 * @param displayed 노출 여부
 * @author ryu-qqq
 * @since 1.0.0
 */
public record BrandDisplaySearchParams(String brandName, Boolean displayed) {

    public static BrandDisplaySearchParams of(String brandName, Boolean displayed) {
        return new BrandDisplaySearchParams(brandName, displayed);
    }
}
