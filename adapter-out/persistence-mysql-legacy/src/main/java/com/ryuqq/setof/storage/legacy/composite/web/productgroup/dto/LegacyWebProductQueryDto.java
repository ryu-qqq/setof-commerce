package com.ryuqq.setof.storage.legacy.composite.web.productgroup.dto;

/**
 * LegacyWebProductQueryDto - 레거시 Web 개별 상품 + 옵션 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * <p>fetchProducts 쿼리(쿼리 2)에서 product, product_stock, product_option, option_group, option_detail
 * 조인 결과를 담습니다.
 *
 * @param productGroupId 상품그룹 ID
 * @param productId 개별 상품 ID
 * @param stockQuantity 재고 수량
 * @param additionalPrice 옵션 추가금
 * @param soldOutYn 품절 여부
 * @param displayYn 표시 여부
 * @param optionGroupId 옵션 그룹 ID (없으면 0)
 * @param optionGroupName 옵션 그룹명 (SIZE/COLOR 등)
 * @param optionDetailId 옵션 상세 ID (없으면 0)
 * @param optionValue 옵션값 (XL/RED 등, 없으면 "")
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebProductQueryDto(
        long productGroupId,
        long productId,
        int stockQuantity,
        int additionalPrice,
        String soldOutYn,
        String displayYn,
        long optionGroupId,
        String optionGroupName,
        long optionDetailId,
        String optionValue) {}
