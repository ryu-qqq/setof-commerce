package com.ryuqq.setof.application.product.manager.command;

import com.ryuqq.setof.application.product.port.out.command.ProductGroupPersistencePort;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductGroup Persistence Manager
 *
 * <p>ProductGroup 영속화를 담당하는 Manager
 *
 * <p>트랜잭션 경계를 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductGroupPersistenceManager {

    private final ProductGroupPersistencePort productGroupPersistencePort;

    public ProductGroupPersistenceManager(ProductGroupPersistencePort productGroupPersistencePort) {
        this.productGroupPersistencePort = productGroupPersistencePort;
    }

    /**
     * ProductGroup 저장
     *
     * @param productGroup 저장할 ProductGroup
     * @return 저장된 ProductGroup의 ID
     */
    @Transactional
    public ProductGroupId persist(ProductGroup productGroup) {
        return productGroupPersistencePort.persist(productGroup);
    }
}
