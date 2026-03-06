package com.ryuqq.setof.domain.contentpage.vo;

/**
 * ProductSlot - 상품 슬롯 VO.
 *
 * @param productGroupId 상품 그룹 ID
 * @param displayOrder 노출 순서
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductSlot(long productGroupId, int displayOrder) {}
