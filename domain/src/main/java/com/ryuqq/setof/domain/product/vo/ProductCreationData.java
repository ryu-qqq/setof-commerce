package com.ryuqq.setof.domain.product.vo;

import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.aggregate.ProductOptionMapping;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import java.time.Instant;
import java.util.List;

/**
 * 상품 생성 데이터.
 *
 * <p>ProductGroup persist 후 ProductGroupId와 SellerOptionValueId 목록을 받아서 Product를 생성하기 위한 데이터입니다.
 * Registration/Update 양쪽에서 공통으로 사용합니다.
 *
 * <p>salePrice와 discountRate는 도메인 내부에서 자동 계산됩니다.
 */
public record ProductCreationData(
        Long productId,
        SkuCode skuCode,
        Money regularPrice,
        Money currentPrice,
        int stockQuantity,
        int sortOrder,
        List<SellerOptionValueId> resolvedOptionValueIds) {

    /**
     * Product 도메인 객체 생성.
     *
     * @param productGroupId 확정된 ProductGroupId
     * @param now 생성 시각
     * @return Product 도메인 객체
     */
    public Product toProduct(ProductGroupId productGroupId, Instant now) {
        ProductId tempProductId = productId != null ? ProductId.of(productId) : ProductId.forNew();
        List<ProductOptionMapping> optionMappings =
                resolvedOptionValueIds.stream()
                        .map(valueId -> ProductOptionMapping.forNew(tempProductId, valueId))
                        .toList();

        return Product.forNew(
                productGroupId,
                skuCode,
                regularPrice,
                currentPrice,
                stockQuantity,
                sortOrder,
                optionMappings,
                now);
    }
}
