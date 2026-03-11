package com.ryuqq.setof.domain.discount.dto;

/**
 * 상품그룹 가격 정보 행.
 *
 * <p>할인 계산 시 필요한 상품그룹의 가격 정보를 담습니다.
 *
 * @param productGroupId 상품그룹 ID
 * @param regularPrice 정가
 * @param currentPrice 현재가 (셀러 설정가)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductGroupPriceRow(long productGroupId, int regularPrice, int currentPrice) {}
