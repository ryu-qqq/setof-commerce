package com.ryuqq.setof.storage.legacy.productgroup.dto;

/**
 * 상품그룹 가격 조회 결과 행.
 *
 * @param productGroupId 상품그룹 ID
 * @param regularPrice 정가
 * @param currentPrice 현재가
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyProductGroupPriceRow(
        Long productGroupId, Integer regularPrice, Integer currentPrice) {}
