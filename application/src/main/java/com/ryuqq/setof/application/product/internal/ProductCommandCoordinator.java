package com.ryuqq.setof.application.product.internal;

import com.ryuqq.setof.application.product.manager.ProductCommandManager;
import com.ryuqq.setof.application.product.manager.ProductOptionMappingCommandManager;
import com.ryuqq.setof.application.product.manager.ProductReadManager;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.vo.ProductDiff;
import com.ryuqq.setof.domain.product.vo.ProductUpdateData;
import com.ryuqq.setof.domain.product.vo.Products;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductCommandCoordinator {

    private final ProductCommandManager productCommandManager;
    private final ProductOptionMappingCommandManager optionMappingCommandManager;
    private final ProductReadManager productReadManager;

    public ProductCommandCoordinator(
            ProductCommandManager productCommandManager,
            ProductOptionMappingCommandManager optionMappingCommandManager,
            ProductReadManager productReadManager) {
        this.productCommandManager = productCommandManager;
        this.optionMappingCommandManager = optionMappingCommandManager;
        this.productReadManager = productReadManager;
    }

    @Transactional
    public List<Long> register(List<Product> products) {
        List<Long> productIds = productCommandManager.persistAll(products);

        for (int i = 0; i < products.size(); i++) {
            optionMappingCommandManager.persistAllForProduct(
                    productIds.get(i), products.get(i).optionMappings());
        }

        return productIds;
    }

    @Transactional
    public void update(ProductGroupId pgId, ProductUpdateData updateData) {
        Products existing =
                Products.reconstitute(pgId, productReadManager.findByProductGroupId(pgId));
        ProductDiff diff = existing.update(updateData);
        productCommandManager.persistAll(diff.allDirtyProducts());
        for (Product removed : diff.removed()) {
            optionMappingCommandManager.persistAll(removed.optionMappings());
        }
        register(diff.added());
    }
}
