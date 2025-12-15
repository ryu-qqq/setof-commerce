package com.ryuqq.setof.application.product.port.out.command;

import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.vo.ProductId;
import java.util.List;

/**
 * Product Persistence Port
 *
 * <p>Product(SKU) 영속화를 위한 Port-Out 인터페이스
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductPersistencePort {

    /**
     * Product 저장
     *
     * @param product 저장할 Product
     * @return 저장된 Product ID
     */
    ProductId persist(Product product);

    /**
     * Product 목록 일괄 저장
     *
     * @param products 저장할 Product 목록
     * @return 저장된 Product ID 목록
     */
    List<ProductId> persistAll(List<Product> products);
}
