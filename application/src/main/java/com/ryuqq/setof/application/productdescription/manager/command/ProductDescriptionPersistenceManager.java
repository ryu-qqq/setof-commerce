package com.ryuqq.setof.application.productdescription.manager.command;

import com.ryuqq.setof.application.productdescription.port.out.command.ProductDescriptionPersistencePort;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품설명 영속성 Manager
 *
 * <p>트랜잭션 경계를 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductDescriptionPersistenceManager {

    private final ProductDescriptionPersistencePort productDescriptionPersistencePort;

    public ProductDescriptionPersistenceManager(
            ProductDescriptionPersistencePort productDescriptionPersistencePort) {
        this.productDescriptionPersistencePort = productDescriptionPersistencePort;
    }

    /**
     * 상품설명 저장
     *
     * @param productDescription 저장할 상품설명
     * @return 저장된 상품설명 ID
     */
    @Transactional
    public Long persist(ProductDescription productDescription) {
        return productDescriptionPersistencePort.persist(productDescription);
    }

    /**
     * 상품설명 수정
     *
     * @param productDescription 수정할 상품설명
     */
    @Transactional
    public void update(ProductDescription productDescription) {
        productDescriptionPersistencePort.update(productDescription);
    }
}
