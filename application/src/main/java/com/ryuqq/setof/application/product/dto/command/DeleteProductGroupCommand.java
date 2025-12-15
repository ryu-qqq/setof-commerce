package com.ryuqq.setof.application.product.dto.command;

/**
 * Delete ProductGroup Command
 *
 * <p>상품그룹 삭제 요청 데이터를 담는 불변 객체
 *
 * @param productGroupId 상품그룹 ID
 * @param sellerId 셀러 ID (권한 검증용)
 * @author development-team
 * @since 1.0.0
 */
public record DeleteProductGroupCommand(Long productGroupId, Long sellerId) {}
