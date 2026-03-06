package com.ryuqq.setof.domain.contentpage.vo;

/**
 * BrandFilter - 브랜드 필터 VO.
 *
 * @param brandId 브랜드 ID
 * @param categoryId 카테고리 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BrandFilter(long brandId, long categoryId) {}
