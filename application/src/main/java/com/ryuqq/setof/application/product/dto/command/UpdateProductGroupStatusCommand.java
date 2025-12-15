package com.ryuqq.setof.application.product.dto.command;

/**
 * Update ProductGroup Status Command
 *
 * <p>상품그룹 상태 변경 요청 데이터를 담는 불변 객체
 *
 * @param productGroupId 상품그룹 ID
 * @param sellerId 셀러 ID (권한 검증용)
 * @param status 변경할 상태 (ACTIVE, INACTIVE, DELETED)
 * @author development-team
 * @since 1.0.0
 */
public record UpdateProductGroupStatusCommand(Long productGroupId, Long sellerId, String status) {}
