package com.ryuqq.setof.application.review.manager.query;

import com.ryuqq.setof.application.review.port.out.query.ProductRatingStatsQueryPort;
import com.ryuqq.setof.domain.review.aggregate.ProductRatingStats;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ProductRatingStats Read Manager
 *
 * <p>ProductRatingStats 조회를 담당하는 Manager
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductRatingStatsReadManager {

    private final ProductRatingStatsQueryPort productRatingStatsQueryPort;

    public ProductRatingStatsReadManager(ProductRatingStatsQueryPort productRatingStatsQueryPort) {
        this.productRatingStatsQueryPort = productRatingStatsQueryPort;
    }

    /**
     * 상품 그룹 ID로 평점 통계 조회
     *
     * @param productGroupId 상품 그룹 ID
     * @return ProductRatingStats (Optional)
     */
    public Optional<ProductRatingStats> findByProductGroupId(Long productGroupId) {
        return productRatingStatsQueryPort.findByProductGroupId(productGroupId);
    }

    /**
     * 상품 그룹 ID로 평점 통계 조회 (없으면 새로 생성)
     *
     * @param productGroupId 상품 그룹 ID
     * @return ProductRatingStats
     */
    public ProductRatingStats getOrCreate(Long productGroupId) {
        return productRatingStatsQueryPort
                .findByProductGroupId(productGroupId)
                .orElse(ProductRatingStats.create(productGroupId));
    }

    /**
     * 상품 그룹 ID로 평점 통계 존재 여부 확인
     *
     * @param productGroupId 상품 그룹 ID
     * @return 존재 여부
     */
    public boolean existsByProductGroupId(Long productGroupId) {
        return productRatingStatsQueryPort.existsByProductGroupId(productGroupId);
    }
}
