package com.ryuqq.setof.application.productgroup.dto.command;

/**
 * 상품그룹 기본정보 수정 커맨드.
 *
 * @param productGroupId 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param brandId 브랜드 ID
 * @param categoryId 카테고리 ID
 * @param shippingPolicyId 배송정책 ID
 * @param refundPolicyId 환불정책 ID
 */
public record UpdateProductGroupBasicInfoCommand(
        long productGroupId,
        String productGroupName,
        long brandId,
        long categoryId,
        long shippingPolicyId,
        long refundPolicyId) {}
