package com.ryuqq.setof.application.review.manager.command;

import com.ryuqq.setof.application.review.port.out.command.ProductRatingStatsPersistencePort;
import com.ryuqq.setof.domain.review.aggregate.ProductRatingStats;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductRatingStats Persistence Manager
 *
 * <p>ProductRatingStats 영속화를 담당하는 Manager
 *
 * <p>트랜잭션 경계를 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductRatingStatsPersistenceManager {

    private final ProductRatingStatsPersistencePort productRatingStatsPersistencePort;

    public ProductRatingStatsPersistenceManager(
            ProductRatingStatsPersistencePort productRatingStatsPersistencePort) {
        this.productRatingStatsPersistencePort = productRatingStatsPersistencePort;
    }

    /**
     * ProductRatingStats 저장
     *
     * @param stats 저장할 ProductRatingStats
     */
    @Transactional
    public void persist(ProductRatingStats stats) {
        productRatingStatsPersistencePort.persist(stats);
    }
}
