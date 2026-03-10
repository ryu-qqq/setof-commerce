package com.ryuqq.setof.application.product.manager;

import com.ryuqq.setof.application.product.port.out.command.ProductOptionMappingCommandPort;
import com.ryuqq.setof.domain.product.aggregate.ProductOptionMapping;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ProductOptionMappingCommandManager {

    private final ProductOptionMappingCommandPort commandPort;

    public ProductOptionMappingCommandManager(ProductOptionMappingCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public void persistAll(List<ProductOptionMapping> mappings) {
        commandPort.persistAll(mappings);
    }

    public void persistAllForProduct(Long productId, List<ProductOptionMapping> mappings) {
        commandPort.persistAllForProduct(productId, mappings);
    }
}
