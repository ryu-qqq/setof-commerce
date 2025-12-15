package com.ryuqq.setof.domain.product;

import com.ryuqq.setof.domain.brand.vo.BrandId;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.OptionType;
import com.ryuqq.setof.domain.product.vo.Price;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductGroupName;
import com.ryuqq.setof.domain.product.vo.ProductGroupStatus;
import com.ryuqq.setof.domain.refundpolicy.vo.RefundPolicyId;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * ProductGroup TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 ProductGroup 인스턴스 생성을 위한 팩토리 클래스
 */
public final class ProductGroupFixture {

    private static final Instant DEFAULT_CREATED_AT = Instant.parse("2024-01-01T00:00:00Z");
    private static final Instant DEFAULT_UPDATED_AT = Instant.parse("2024-01-01T00:00:00Z");

    private ProductGroupFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 기본 상품그룹 생성 (2단 옵션)
     *
     * @return ProductGroup 인스턴스
     */
    public static ProductGroup create() {
        return ProductGroup.reconstitute(
                ProductGroupId.of(1L),
                SellerId.of(1L),
                CategoryId.of(100L),
                BrandId.of(1L),
                ProductGroupName.of("테스트 상품그룹"),
                OptionType.TWO_LEVEL,
                Price.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(45000)),
                ProductGroupStatus.ACTIVE,
                ShippingPolicyId.of(1L),
                RefundPolicyId.of(1L),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * ID 지정하여 상품그룹 생성
     *
     * @param id 상품그룹 ID
     * @return ProductGroup 인스턴스
     */
    public static ProductGroup createWithId(Long id) {
        return ProductGroup.reconstitute(
                ProductGroupId.of(id),
                SellerId.of(1L),
                CategoryId.of(100L),
                BrandId.of(1L),
                ProductGroupName.of("테스트 상품그룹"),
                OptionType.TWO_LEVEL,
                Price.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(45000)),
                ProductGroupStatus.ACTIVE,
                ShippingPolicyId.of(1L),
                RefundPolicyId.of(1L),
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * 단품 상품그룹 생성 (옵션 없음)
     *
     * @return ProductGroup 인스턴스
     */
    public static ProductGroup createSingleOption() {
        return ProductGroup.reconstitute(
                ProductGroupId.of(2L),
                SellerId.of(1L),
                CategoryId.of(100L),
                BrandId.of(1L),
                ProductGroupName.of("단품 상품그룹"),
                OptionType.SINGLE,
                Price.of(BigDecimal.valueOf(30000), BigDecimal.valueOf(30000)),
                ProductGroupStatus.ACTIVE,
                null,
                null,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * 1단 옵션 상품그룹 생성
     *
     * @return ProductGroup 인스턴스
     */
    public static ProductGroup createOneLevelOption() {
        return ProductGroup.reconstitute(
                ProductGroupId.of(3L),
                SellerId.of(1L),
                CategoryId.of(100L),
                BrandId.of(1L),
                ProductGroupName.of("1단옵션 상품그룹"),
                OptionType.ONE_LEVEL,
                Price.of(BigDecimal.valueOf(40000), BigDecimal.valueOf(35000)),
                ProductGroupStatus.ACTIVE,
                ShippingPolicyId.of(1L),
                null,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * 비활성 상품그룹 생성
     *
     * @return ProductGroup 인스턴스 (비활성)
     */
    public static ProductGroup createInactive() {
        return ProductGroup.reconstitute(
                ProductGroupId.of(4L),
                SellerId.of(1L),
                CategoryId.of(100L),
                BrandId.of(1L),
                ProductGroupName.of("비활성 상품그룹"),
                OptionType.TWO_LEVEL,
                Price.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(45000)),
                ProductGroupStatus.INACTIVE,
                null,
                null,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * 삭제된 상품그룹 생성
     *
     * @return ProductGroup 인스턴스 (삭제됨)
     */
    public static ProductGroup createDeleted() {
        Instant deletedAt = Instant.parse("2024-06-01T00:00:00Z");
        return ProductGroup.reconstitute(
                ProductGroupId.of(5L),
                SellerId.of(1L),
                CategoryId.of(100L),
                BrandId.of(1L),
                ProductGroupName.of("삭제된 상품그룹"),
                OptionType.TWO_LEVEL,
                Price.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(45000)),
                ProductGroupStatus.DELETED,
                null,
                null,
                DEFAULT_CREATED_AT,
                deletedAt,
                deletedAt);
    }

    /**
     * 정책 없는 상품그룹 생성 (셀러 기본 정책 사용)
     *
     * @return ProductGroup 인스턴스
     */
    public static ProductGroup createWithoutPolicies() {
        return ProductGroup.reconstitute(
                ProductGroupId.of(6L),
                SellerId.of(1L),
                CategoryId.of(100L),
                BrandId.of(1L),
                ProductGroupName.of("정책없음 상품그룹"),
                OptionType.TWO_LEVEL,
                Price.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(45000)),
                ProductGroupStatus.ACTIVE,
                null,
                null,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * 여러 상품그룹 목록 생성
     *
     * @return ProductGroup 목록
     */
    public static List<ProductGroup> createList() {
        return List.of(create(), createSingleOption(), createOneLevelOption(), createInactive());
    }

    /**
     * 커스텀 상품그룹 생성
     *
     * @param id 상품그룹 ID
     * @param sellerId 셀러 ID
     * @param categoryId 카테고리 ID
     * @param brandId 브랜드 ID
     * @param name 상품그룹명
     * @param optionType 옵션 타입
     * @param regularPrice 정가
     * @param currentPrice 판매가
     * @param status 상태
     * @return ProductGroup 인스턴스
     */
    public static ProductGroup createCustom(
            Long id,
            Long sellerId,
            Long categoryId,
            Long brandId,
            String name,
            OptionType optionType,
            BigDecimal regularPrice,
            BigDecimal currentPrice,
            ProductGroupStatus status) {
        return ProductGroup.reconstitute(
                ProductGroupId.of(id),
                SellerId.of(sellerId),
                CategoryId.of(categoryId),
                BrandId.of(brandId),
                ProductGroupName.of(name),
                optionType,
                Price.of(regularPrice, currentPrice),
                status,
                null,
                null,
                DEFAULT_CREATED_AT,
                DEFAULT_UPDATED_AT,
                null);
    }

    /**
     * 신규 생성용 ProductGroup 생성 (ID 없음)
     *
     * @return ProductGroup 인스턴스 (ID null)
     */
    public static ProductGroup createNew() {
        return ProductGroup.create(
                SellerId.of(1L),
                CategoryId.of(100L),
                BrandId.of(1L),
                ProductGroupName.of("신규 상품그룹"),
                OptionType.TWO_LEVEL,
                Price.of(BigDecimal.valueOf(50000), BigDecimal.valueOf(45000)),
                ShippingPolicyId.of(1L),
                RefundPolicyId.of(1L),
                DEFAULT_CREATED_AT);
    }
}
