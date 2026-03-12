package com.setof.commerce.domain.product;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.aggregate.ProductOptionMapping;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.product.id.ProductOptionMappingId;
import com.ryuqq.setof.domain.product.vo.ProductCreationData;
import com.ryuqq.setof.domain.product.vo.ProductDiff;
import com.ryuqq.setof.domain.product.vo.ProductOption;
import com.ryuqq.setof.domain.product.vo.ProductStatus;
import com.ryuqq.setof.domain.product.vo.ProductUpdateData;
import com.ryuqq.setof.domain.product.vo.Products;
import com.ryuqq.setof.domain.product.vo.SkuCode;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import java.time.Instant;
import java.util.List;

/**
 * Product 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Product 관련 객체들을 생성합니다.
 */
public final class ProductFixtures {

    private ProductFixtures() {}

    // ===== 상수 =====
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 1L;
    public static final int DEFAULT_REGULAR_PRICE = 10000;
    public static final int DEFAULT_CURRENT_PRICE = 10000;
    public static final int DEFAULT_STOCK_QUANTITY = 100;
    public static final int DEFAULT_SORT_ORDER = 0;

    // ===== 기본 옵션 매핑 =====
    public static final List<ProductOptionMapping> DEFAULT_OPTION_MAPPINGS =
            List.of(
                    ProductOptionMapping.forNew(ProductId.forNew(), SellerOptionValueId.of(1L)),
                    ProductOptionMapping.forNew(ProductId.forNew(), SellerOptionValueId.of(2L)));

    // ===== Product Aggregate Fixtures =====

    /** 새 상품(SKU) 생성. forNew()를 사용하여 ACTIVE 상태로 생성됩니다. */
    public static Product newProduct() {
        return Product.forNew(
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                null,
                Money.of(DEFAULT_REGULAR_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                DEFAULT_STOCK_QUANTITY,
                DEFAULT_SORT_ORDER,
                DEFAULT_OPTION_MAPPINGS,
                CommonVoFixtures.now());
    }

    /** 활성 상태의 상품을 복원합니다. */
    public static Product activeProduct() {
        return Product.reconstitute(
                ProductId.of(1L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                null,
                Money.of(DEFAULT_REGULAR_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                0,
                DEFAULT_STOCK_QUANTITY,
                ProductStatus.ACTIVE,
                DEFAULT_SORT_ORDER,
                DEFAULT_OPTION_MAPPINGS,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 특정 ID의 활성 상태 상품을 복원합니다. */
    public static Product activeProduct(Long id) {
        return Product.reconstitute(
                ProductId.of(id),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                null,
                Money.of(DEFAULT_REGULAR_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                0,
                DEFAULT_STOCK_QUANTITY,
                ProductStatus.ACTIVE,
                DEFAULT_SORT_ORDER,
                DEFAULT_OPTION_MAPPINGS,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 비활성 상태의 상품을 복원합니다. */
    public static Product inactiveProduct() {
        return Product.reconstitute(
                ProductId.of(2L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                null,
                Money.of(DEFAULT_REGULAR_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                0,
                DEFAULT_STOCK_QUANTITY,
                ProductStatus.INACTIVE,
                DEFAULT_SORT_ORDER,
                DEFAULT_OPTION_MAPPINGS,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 품절 상태의 상품을 복원합니다. */
    public static Product soldOutProduct() {
        return Product.reconstitute(
                ProductId.of(3L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                null,
                Money.of(DEFAULT_REGULAR_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                0,
                DEFAULT_STOCK_QUANTITY,
                ProductStatus.SOLD_OUT,
                DEFAULT_SORT_ORDER,
                DEFAULT_OPTION_MAPPINGS,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 삭제된 상태의 상품을 복원합니다. */
    public static Product deletedProduct() {
        return Product.reconstitute(
                ProductId.of(4L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                null,
                Money.of(DEFAULT_REGULAR_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                0,
                DEFAULT_STOCK_QUANTITY,
                ProductStatus.DELETED,
                DEFAULT_SORT_ORDER,
                DEFAULT_OPTION_MAPPINGS,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 옵션 없는 상품을 복원합니다. */
    public static Product productWithoutOptions() {
        return Product.reconstitute(
                ProductId.of(5L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                null,
                Money.of(DEFAULT_REGULAR_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                0,
                DEFAULT_STOCK_QUANTITY,
                ProductStatus.ACTIVE,
                DEFAULT_SORT_ORDER,
                List.of(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 특정 옵션 매핑 목록을 가진 상품을 복원합니다. */
    public static Product productWithOptions(List<ProductOptionMapping> optionMappings) {
        return Product.reconstitute(
                ProductId.of(6L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                null,
                Money.of(DEFAULT_REGULAR_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                0,
                DEFAULT_STOCK_QUANTITY,
                ProductStatus.ACTIVE,
                DEFAULT_SORT_ORDER,
                optionMappings,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== ProductOptionMapping Fixtures =====

    /** 신규 ProductOptionMapping 생성. */
    public static ProductOptionMapping newProductOptionMapping() {
        return ProductOptionMapping.forNew(ProductId.forNew(), SellerOptionValueId.of(1L));
    }

    /** 특정 ProductId와 SellerOptionValueId로 신규 매핑 생성. */
    public static ProductOptionMapping newProductOptionMapping(
            ProductId productId, SellerOptionValueId sellerOptionValueId) {
        return ProductOptionMapping.forNew(productId, sellerOptionValueId);
    }

    /** 영속성에서 복원된 활성 ProductOptionMapping. */
    public static ProductOptionMapping activeProductOptionMapping() {
        return ProductOptionMapping.reconstitute(
                ProductOptionMappingId.of(1L),
                ProductId.of(1L),
                SellerOptionValueId.of(1L),
                com.ryuqq.setof.domain.common.vo.DeletionStatus.active());
    }

    /** 영속성에서 복원된 삭제된 ProductOptionMapping. */
    public static ProductOptionMapping deletedProductOptionMapping() {
        Instant now = CommonVoFixtures.now();
        return ProductOptionMapping.reconstitute(
                ProductOptionMappingId.of(2L),
                ProductId.of(1L),
                SellerOptionValueId.of(2L),
                com.ryuqq.setof.domain.common.vo.DeletionStatus.deletedAt(now));
    }

    // ===== ProductOptionMappingId Fixtures =====

    /** 기본 ProductOptionMappingId 생성. */
    public static ProductOptionMappingId defaultProductOptionMappingId() {
        return ProductOptionMappingId.of(1L);
    }

    /** 신규 ProductOptionMappingId 생성. */
    public static ProductOptionMappingId newProductOptionMappingId() {
        return ProductOptionMappingId.forNew();
    }

    // ===== SkuCode Fixtures =====

    /** 기본 SkuCode 생성. */
    public static SkuCode defaultSkuCode() {
        return SkuCode.of("SKU-001");
    }

    /** null 값의 SkuCode 생성. */
    public static SkuCode emptySkuCode() {
        return SkuCode.of(null);
    }

    // ===== ProductOption Fixtures =====

    /** 기본 ProductOption 생성. */
    public static ProductOption defaultProductOption() {
        return ProductOption.of(SellerOptionGroupId.of(1L), SellerOptionValueId.of(1L), 0);
    }

    /** 특정 정렬 순서의 ProductOption 생성. */
    public static ProductOption productOptionWithSortOrder(int sortOrder) {
        return ProductOption.of(SellerOptionGroupId.of(1L), SellerOptionValueId.of(1L), sortOrder);
    }

    // ===== ProductCreationData Fixtures =====

    /** 기본 ProductCreationData 생성. */
    public static ProductCreationData defaultProductCreationData() {
        return new ProductCreationData(
                null,
                defaultSkuCode(),
                Money.of(DEFAULT_REGULAR_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                DEFAULT_STOCK_QUANTITY,
                DEFAULT_SORT_ORDER,
                List.of(SellerOptionValueId.of(1L), SellerOptionValueId.of(2L)));
    }

    /** 옵션 없는 ProductCreationData 생성. */
    public static ProductCreationData productCreationDataWithoutOptions() {
        return new ProductCreationData(
                null,
                emptySkuCode(),
                Money.of(DEFAULT_REGULAR_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                DEFAULT_STOCK_QUANTITY,
                DEFAULT_SORT_ORDER,
                List.of());
    }

    // ===== ProductUpdateData Fixtures =====

    /** 기존 상품 수정 엔트리 생성. */
    public static ProductUpdateData.Entry existingProductEntry(Long productId) {
        return new ProductUpdateData.Entry(
                productId,
                defaultSkuCode(),
                Money.of(DEFAULT_REGULAR_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                DEFAULT_STOCK_QUANTITY,
                DEFAULT_SORT_ORDER,
                List.of());
    }

    /** 신규 상품 엔트리 생성. */
    public static ProductUpdateData.Entry newProductEntry() {
        return new ProductUpdateData.Entry(
                null,
                defaultSkuCode(),
                Money.of(DEFAULT_REGULAR_PRICE),
                Money.of(DEFAULT_CURRENT_PRICE),
                DEFAULT_STOCK_QUANTITY,
                DEFAULT_SORT_ORDER,
                List.of(SellerOptionValueId.of(1L)));
    }

    /** 기본 ProductUpdateData 생성 (기존 상품 수정). */
    public static ProductUpdateData defaultProductUpdateData(Long productId) {
        return new ProductUpdateData(
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                List.of(existingProductEntry(productId)),
                CommonVoFixtures.now());
    }

    // ===== Products VO Fixtures =====

    /** 활성 상품 하나를 포함하는 Products 생성. */
    public static Products singleProductsVO() {
        return Products.reconstitute(
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID), List.of(activeProduct()));
    }

    /** 여러 활성 상품을 포함하는 Products 생성. */
    public static Products multiProductsVO() {
        return Products.reconstitute(
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                List.of(activeProduct(1L), activeProduct(2L)));
    }

    /** 빈 Products 생성. */
    public static Products emptyProductsVO() {
        return Products.reconstitute(ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID), List.of());
    }

    // ===== ProductDiff Fixtures =====

    /** 변경 없는 ProductDiff 생성. */
    public static ProductDiff noChangeDiff() {
        return ProductDiff.of(
                List.of(), List.of(), List.of(activeProduct()), CommonVoFixtures.now());
    }

    /** 신규 상품 추가만 있는 ProductDiff 생성. */
    public static ProductDiff addOnlyDiff() {
        return ProductDiff.of(List.of(newProduct()), List.of(), List.of(), CommonVoFixtures.now());
    }
}
