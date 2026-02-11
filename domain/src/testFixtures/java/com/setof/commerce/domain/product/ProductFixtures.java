package com.setof.commerce.domain.product;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.product.vo.ProductStatus;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;

/**
 * Product 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Product 관련 객체들을 생성합니다.
 */
public final class ProductFixtures {

    private ProductFixtures() {}

    // ===== 상수 =====
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 1L;
    public static final String DEFAULT_OPTION1_NAME = "색상";
    public static final String DEFAULT_OPTION1_VALUE = "블랙";
    public static final String DEFAULT_OPTION2_NAME = "사이즈";
    public static final String DEFAULT_OPTION2_VALUE = "M";
    public static final int DEFAULT_STOCK = 100;
    public static final int DEFAULT_ADDITIONAL_PRICE = 0;

    // ===== Product Aggregate Fixtures =====

    /** 새 상품(SKU) 생성. forNew()를 사용하여 ACTIVE 상태로 생성됩니다. */
    public static Product newProduct() {
        return Product.forNew(
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                DEFAULT_OPTION1_NAME,
                DEFAULT_OPTION1_VALUE,
                DEFAULT_OPTION2_NAME,
                DEFAULT_OPTION2_VALUE,
                Money.of(DEFAULT_ADDITIONAL_PRICE),
                DEFAULT_STOCK,
                CommonVoFixtures.now());
    }

    /** 활성 상태의 상품을 복원합니다. */
    public static Product activeProduct() {
        return Product.reconstitute(
                ProductId.of(1L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                DEFAULT_OPTION1_NAME,
                DEFAULT_OPTION1_VALUE,
                DEFAULT_OPTION2_NAME,
                DEFAULT_OPTION2_VALUE,
                Money.of(DEFAULT_ADDITIONAL_PRICE),
                DEFAULT_STOCK,
                ProductStatus.ACTIVE,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 특정 ID의 활성 상태 상품을 복원합니다. */
    public static Product activeProduct(Long id) {
        return Product.reconstitute(
                ProductId.of(id),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                DEFAULT_OPTION1_NAME,
                DEFAULT_OPTION1_VALUE,
                DEFAULT_OPTION2_NAME,
                DEFAULT_OPTION2_VALUE,
                Money.of(DEFAULT_ADDITIONAL_PRICE),
                DEFAULT_STOCK,
                ProductStatus.ACTIVE,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 비활성 상태의 상품을 복원합니다. */
    public static Product inactiveProduct() {
        return Product.reconstitute(
                ProductId.of(2L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                DEFAULT_OPTION1_NAME,
                DEFAULT_OPTION1_VALUE,
                DEFAULT_OPTION2_NAME,
                DEFAULT_OPTION2_VALUE,
                Money.of(DEFAULT_ADDITIONAL_PRICE),
                DEFAULT_STOCK,
                ProductStatus.INACTIVE,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 품절 상태의 상품을 복원합니다. */
    public static Product soldOutProduct() {
        return Product.reconstitute(
                ProductId.of(3L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                DEFAULT_OPTION1_NAME,
                DEFAULT_OPTION1_VALUE,
                DEFAULT_OPTION2_NAME,
                DEFAULT_OPTION2_VALUE,
                Money.of(DEFAULT_ADDITIONAL_PRICE),
                0,
                ProductStatus.SOLDOUT,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 삭제된 상태의 상품을 복원합니다. */
    public static Product deletedProduct() {
        return Product.reconstitute(
                ProductId.of(4L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                DEFAULT_OPTION1_NAME,
                DEFAULT_OPTION1_VALUE,
                DEFAULT_OPTION2_NAME,
                DEFAULT_OPTION2_VALUE,
                Money.of(DEFAULT_ADDITIONAL_PRICE),
                0,
                ProductStatus.DELETED,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 특정 재고 수량의 상품을 복원합니다. */
    public static Product productWithStock(int quantity) {
        return Product.reconstitute(
                ProductId.of(5L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                DEFAULT_OPTION1_NAME,
                DEFAULT_OPTION1_VALUE,
                DEFAULT_OPTION2_NAME,
                DEFAULT_OPTION2_VALUE,
                Money.of(DEFAULT_ADDITIONAL_PRICE),
                quantity,
                ProductStatus.ACTIVE,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 옵션 이름이 null인 상품을 복원합니다. */
    public static Product productWithoutOptions() {
        return Product.reconstitute(
                ProductId.of(6L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                null,
                null,
                null,
                null,
                Money.of(DEFAULT_ADDITIONAL_PRICE),
                DEFAULT_STOCK,
                ProductStatus.ACTIVE,
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }
}
