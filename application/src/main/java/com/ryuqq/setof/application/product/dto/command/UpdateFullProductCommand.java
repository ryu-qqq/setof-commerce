package com.ryuqq.setof.application.product.dto.command;

import java.math.BigDecimal;
import java.util.List;

/**
 * 전체 상품 수정 Command
 *
 * <p>상품그룹 + SKU + 이미지 + 설명 + 고시 일괄 수정 요청 데이터
 *
 * <p>수정 전략:
 *
 * <ul>
 *   <li>ID가 있는 항목: 수정
 *   <li>ID가 없는 항목: 신규 추가
 *   <li>기존에만 있는 항목: 삭제
 * </ul>
 *
 * @param productGroupId 상품그룹 ID
 * @param categoryId 카테고리 ID
 * @param brandId 브랜드 ID
 * @param name 상품그룹명
 * @param optionType 옵션 타입 (SINGLE, ONE_LEVEL, TWO_LEVEL)
 * @param regularPrice 정가
 * @param currentPrice 판매가
 * @param status 상품 상태 (DRAFT, PENDING, APPROVED, REJECTED, DISCONTINUED)
 * @param shippingPolicyId 배송 정책 ID
 * @param refundPolicyId 환불 정책 ID
 * @param products 상품(SKU) 목록
 * @param images 이미지 목록
 * @param description 상세설명 (nullable)
 * @param notice 고시정보 (nullable)
 * @author development-team
 * @since 1.0.0
 */
public record UpdateFullProductCommand(
        Long productGroupId,
        Long categoryId,
        Long brandId,
        String name,
        String optionType,
        BigDecimal regularPrice,
        BigDecimal currentPrice,
        String status,
        Long shippingPolicyId,
        Long refundPolicyId,
        List<ProductSkuCommandDto> products,
        List<ProductImageCommandDto> images,
        ProductDescriptionCommandDto description,
        ProductNoticeCommandDto notice) {

    /**
     * 이미지 목록이 비어있는지 확인
     *
     * @return 비어있으면 true
     */
    public boolean hasNoImages() {
        return images == null || images.isEmpty();
    }

    /**
     * 상세설명이 있는지 확인
     *
     * @return 있으면 true
     */
    public boolean hasDescription() {
        return description != null;
    }

    /**
     * 고시정보가 있는지 확인
     *
     * @return 있으면 true
     */
    public boolean hasNotice() {
        return notice != null;
    }
}
