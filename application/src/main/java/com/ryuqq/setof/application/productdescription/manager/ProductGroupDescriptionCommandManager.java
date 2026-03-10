package com.ryuqq.setof.application.productdescription.manager;

import com.ryuqq.setof.application.productdescription.port.out.command.ProductGroupDescriptionCommandPort;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductGroupDescriptionCommandManager - 상세설명 명령 Manager.
 *
 * <p>CommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@Transactional
public class ProductGroupDescriptionCommandManager {

    private final ProductGroupDescriptionCommandPort commandPort;

    public ProductGroupDescriptionCommandManager(ProductGroupDescriptionCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public Long persist(ProductGroupDescription description) {
        return commandPort.persist(description);
    }
}
