package com.ryuqq.setof.domain.product.vo;

import com.ryuqq.setof.domain.productgroup.id.SellerOptionGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import java.util.Objects;

/**
 * 상품(SKU)의 옵션 정보 Value Object.
 *
 * <p>이 SKU가 어떤 옵션 조합인지를 나타냅니다. 예: 색상=블랙, 사이즈=M
 *
 * @param optionGroupId 옵션 그룹 ID (색상, 사이즈 등)
 * @param optionDetailId 옵션 상세 ID (블랙, M 등)
 * @param sortOrder 정렬 순서
 */
public record ProductOption(
        SellerOptionGroupId optionGroupId, SellerOptionValueId optionDetailId, int sortOrder) {

    public ProductOption {
        Objects.requireNonNull(optionGroupId, "옵션 그룹 ID는 필수입니다");
        Objects.requireNonNull(optionDetailId, "옵션 상세 ID는 필수입니다");
        if (sortOrder < 0) {
            throw new IllegalArgumentException("정렬 순서는 0 이상이어야 합니다");
        }
    }

    public static ProductOption of(
            SellerOptionGroupId optionGroupId, SellerOptionValueId optionDetailId, int sortOrder) {
        return new ProductOption(optionGroupId, optionDetailId, sortOrder);
    }

    public Long optionGroupIdValue() {
        return optionGroupId.value();
    }

    public Long optionDetailIdValue() {
        return optionDetailId.value();
    }
}
