package com.ryuqq.setof.application.productnotice.manager.query;

import com.ryuqq.setof.application.productnotice.port.out.query.ProductNoticeQueryPort;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품고시 조회 Manager
 *
 * <p>트랜잭션 경계를 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductNoticeReadManager {

    private final ProductNoticeQueryPort productNoticeQueryPort;

    public ProductNoticeReadManager(ProductNoticeQueryPort productNoticeQueryPort) {
        this.productNoticeQueryPort = productNoticeQueryPort;
    }

    /**
     * ID로 상품고시 조회
     *
     * @param productNoticeId 상품고시 ID
     * @return 상품고시
     */
    @Transactional(readOnly = true)
    public ProductNotice findById(Long productNoticeId) {
        return productNoticeQueryPort.findById(productNoticeId);
    }

    /**
     * 상품그룹 ID로 상품고시 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품고시 (없으면 Optional.empty)
     */
    @Transactional(readOnly = true)
    public Optional<ProductNotice> findByProductGroupId(Long productGroupId) {
        return productNoticeQueryPort.findByProductGroupId(productGroupId);
    }
}
