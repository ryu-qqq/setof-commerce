package com.ryuqq.setof.application.product.manager.query;

import com.ryuqq.setof.application.product.port.out.query.ProductSkuQueryPort;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.exception.ProductNotFoundException;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Product(SKU) Read Manager
 *
 * <p>Product(SKU) 조회 트랜잭션 경계 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductSkuReadManager {

    private final ProductSkuQueryPort productSkuQueryPort;

    public ProductSkuReadManager(ProductSkuQueryPort productSkuQueryPort) {
        this.productSkuQueryPort = productSkuQueryPort;
    }

    /**
     * Product ID로 단건 조회
     *
     * @param productId Product ID
     * @return Product
     * @throws ProductNotFoundException Product가 없는 경우
     */
    @Transactional(readOnly = true)
    public Product findById(Long productId) {
        return productSkuQueryPort
                .findById(ProductId.of(productId))
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    /**
     * ProductGroup ID로 Product 목록 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return Product 목록
     */
    @Transactional(readOnly = true)
    public List<Product> findByProductGroupId(Long productGroupId) {
        return productSkuQueryPort.findByProductGroupId(ProductGroupId.of(productGroupId));
    }

    /**
     * 여러 ProductGroup의 Product 목록 일괄 조회
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return Product 목록
     */
    @Transactional(readOnly = true)
    public List<Product> findByProductGroupIds(List<Long> productGroupIds) {
        List<ProductGroupId> ids = productGroupIds.stream().map(ProductGroupId::of).toList();
        return productSkuQueryPort.findByProductGroupIds(ids);
    }
}
