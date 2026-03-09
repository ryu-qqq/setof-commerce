package com.ryuqq.setof.domain.order.vo;

import java.util.List;

/**
 * 주문 시점 상품 스냅샷 VO.
 *
 * <p>주문 생성 시 상품/셀러/브랜드/이미지/옵션 정보를 복사하여 보관합니다. 이후 상품 정보가 변경되어도 주문 시점의 정보를 유지합니다.
 *
 * @param productGroupId 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param productId 상품(SKU) ID
 * @param sellerId 판매자 ID
 * @param sellerName 판매자명
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param categoryId 카테고리 ID
 * @param mainImageUrl 대표 이미지 URL
 * @param options 주문 시점 옵션 스냅샷 목록
 */
public record OrderProductSnapshot(
        long productGroupId,
        String productGroupName,
        long productId,
        long sellerId,
        String sellerName,
        long brandId,
        String brandName,
        long categoryId,
        String mainImageUrl,
        List<OptionSnapshot> options) {

    public OrderProductSnapshot {
        if (productGroupName == null || productGroupName.isBlank()) {
            throw new IllegalArgumentException("상품그룹명은 필수입니다");
        }
        options = options != null ? List.copyOf(options) : List.of();
    }

    /** 옵션 조합 문자열 (예: "사이즈 270mm / 색상 화이트"). */
    public String optionLabel() {
        if (options.isEmpty()) {
            return "";
        }
        return options.stream()
                .map(o -> o.optionName() + " " + o.optionValue())
                .reduce((a, b) -> a + " / " + b)
                .orElse("");
    }
}
