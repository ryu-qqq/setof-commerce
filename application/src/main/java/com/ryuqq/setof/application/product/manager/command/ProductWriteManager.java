package com.ryuqq.setof.application.product.manager.command;

import com.ryuqq.setof.application.product.port.out.command.ProductPersistencePort;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.vo.ProductId;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * Product Write Manager
 *
 * <p>Product(SKU) 저장을 담당하는 Manager
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductWriteManager {

    private final ProductPersistencePort productPersistencePort;

    public ProductWriteManager(ProductPersistencePort productPersistencePort) {
        this.productPersistencePort = productPersistencePort;
    }

    /**
     * Product 저장
     *
     * @param product 저장할 Product
     * @return 저장된 Product ID
     */
    public ProductId save(Product product) {
        Objects.requireNonNull(product, "product must not be null");
        return productPersistencePort.persist(product);
    }

    /**
     * Product 목록 일괄 저장
     *
     * @param products 저장할 Product 목록
     * @return 저장된 Product ID 목록
     */
    public List<ProductId> saveAll(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return List.of();
        }
        return productPersistencePort.persistAll(products);
    }
}
