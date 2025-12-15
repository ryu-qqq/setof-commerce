package com.ryuqq.setof.application.productnotice.manager.command;

import com.ryuqq.setof.application.productnotice.port.out.command.ProductNoticePersistencePort;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품고시 영속성 Manager
 *
 * <p>트랜잭션 경계를 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductNoticePersistenceManager {

    private final ProductNoticePersistencePort productNoticePersistencePort;

    public ProductNoticePersistenceManager(
            ProductNoticePersistencePort productNoticePersistencePort) {
        this.productNoticePersistencePort = productNoticePersistencePort;
    }

    /**
     * 상품고시 저장
     *
     * @param productNotice 저장할 상품고시
     * @return 저장된 상품고시 ID
     */
    @Transactional
    public Long persist(ProductNotice productNotice) {
        return productNoticePersistencePort.persist(productNotice);
    }

    /**
     * 상품고시 수정
     *
     * @param productNotice 수정할 상품고시
     */
    @Transactional
    public void update(ProductNotice productNotice) {
        productNoticePersistencePort.update(productNotice);
    }
}
