package com.ryuqq.setof.domain.displaycomponent.vo;

/**
 * BrandTarget - 브랜드 컴포넌트 타겟 조건 VO.
 *
 * @param brandId 브랜드 ID
 * @param categoryId 카테고리 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BrandTarget(long brandId, long categoryId) {}
