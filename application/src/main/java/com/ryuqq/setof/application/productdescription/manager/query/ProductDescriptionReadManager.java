package com.ryuqq.setof.application.productdescription.manager.query;

import com.ryuqq.setof.application.productdescription.port.out.query.ProductDescriptionQueryPort;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품설명 조회 Manager
 *
 * <p>트랜잭션 경계를 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductDescriptionReadManager {

    private final ProductDescriptionQueryPort productDescriptionQueryPort;

    public ProductDescriptionReadManager(ProductDescriptionQueryPort productDescriptionQueryPort) {
        this.productDescriptionQueryPort = productDescriptionQueryPort;
    }

    /**
     * ID로 상품설명 조회
     *
     * @param productDescriptionId 상품설명 ID
     * @return 상품설명
     */
    @Transactional(readOnly = true)
    public ProductDescription findById(Long productDescriptionId) {
        return productDescriptionQueryPort.findById(productDescriptionId);
    }

    /**
     * 상품그룹 ID로 상품설명 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품설명 (없으면 Optional.empty)
     */
    @Transactional(readOnly = true)
    public Optional<ProductDescription> findByProductGroupId(Long productGroupId) {
        return productDescriptionQueryPort.findByProductGroupId(productGroupId);
    }
}
