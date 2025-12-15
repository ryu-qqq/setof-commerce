package com.ryuqq.setof.application.product.port.out.query;

import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductId;
import java.util.List;
import java.util.Optional;

/**
 * Product(SKU) 조회 Port
 *
 * <p>Product Aggregate 조회를 위한 출력 포트
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductSkuQueryPort {

    /**
     * Product ID로 단건 조회
     *
     * @param productId Product ID
     * @return Product (Optional)
     */
    Optional<Product> findById(ProductId productId);

    /**
     * ProductGroup ID로 Product 목록 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return Product 목록
     */
    List<Product> findByProductGroupId(ProductGroupId productGroupId);

    /**
     * 여러 ProductGroup의 Product 목록 일괄 조회
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return Product 목록
     */
    List<Product> findByProductGroupIds(List<ProductGroupId> productGroupIds);
}
