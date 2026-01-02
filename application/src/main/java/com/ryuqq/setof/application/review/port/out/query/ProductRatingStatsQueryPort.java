package com.ryuqq.setof.application.review.port.out.query;

import com.ryuqq.setof.domain.review.aggregate.ProductRatingStats;
import java.util.Optional;

/**
 * ProductRatingStats Query Port (Query)
 *
 * <p>ProductRatingStats Aggregate를 조회하는 읽기 전용 Port
 */
public interface ProductRatingStatsQueryPort {

    /**
     * 상품 그룹 ID로 평점 통계 조회
     *
     * @param productGroupId 상품 그룹 ID
     * @return ProductRatingStats Domain (Optional)
     */
    Optional<ProductRatingStats> findByProductGroupId(Long productGroupId);

    /**
     * 상품 그룹 ID로 평점 통계 존재 여부 확인
     *
     * @param productGroupId 상품 그룹 ID
     * @return 존재 여부
     */
    boolean existsByProductGroupId(Long productGroupId);
}
