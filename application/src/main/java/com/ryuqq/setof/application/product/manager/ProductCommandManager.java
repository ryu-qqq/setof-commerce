package com.ryuqq.setof.application.product.manager;

import com.ryuqq.setof.application.product.port.out.command.ProductCommandPort;
import com.ryuqq.setof.domain.product.aggregate.Product;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ProductCommandManager {

    private final ProductCommandPort commandPort;

    public ProductCommandManager(ProductCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public Long persist(Product product) {
        return commandPort.persist(product);
    }

    public List<Long> persistAll(List<Product> products) {
        return commandPort.persistAll(products);
    }
}
