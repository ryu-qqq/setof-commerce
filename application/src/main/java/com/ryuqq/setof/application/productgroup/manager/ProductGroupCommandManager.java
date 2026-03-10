package com.ryuqq.setof.application.productgroup.manager;

import com.ryuqq.setof.application.productgroup.port.out.command.ProductGroupCommandPort;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품그룹 Command Manager.
 *
 * <p>ProductGroupCommandPort를 감싸며 트랜잭션 경계를 관리합니다.
 */
@Component
@Transactional
public class ProductGroupCommandManager {

    private final ProductGroupCommandPort commandPort;

    public ProductGroupCommandManager(ProductGroupCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    /**
     * 상품그룹을 저장합니다.
     *
     * @param productGroup 저장할 상품그룹 도메인 객체
     * @return 저장된 상품그룹 ID
     */
    public Long persist(ProductGroup productGroup) {
        return commandPort.persist(productGroup);
    }
}
