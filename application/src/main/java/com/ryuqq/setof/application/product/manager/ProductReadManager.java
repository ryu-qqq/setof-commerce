package com.ryuqq.setof.application.product.manager;

import com.ryuqq.setof.application.product.port.out.query.ProductQueryPort;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.exception.ProductNotFoundException;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductReadManager {

    private final ProductQueryPort queryPort;

    public ProductReadManager(ProductQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public Product getById(ProductId id) {
        return queryPort.findById(id).orElseThrow(() -> new ProductNotFoundException(id.value()));
    }

    @Transactional(readOnly = true)
    public List<Product> findByProductGroupId(ProductGroupId productGroupId) {
        return queryPort.findByProductGroupId(productGroupId);
    }

    @Transactional(readOnly = true)
    public List<Product> getByIds(List<ProductId> ids) {
        return queryPort.findByIds(ids);
    }
}
