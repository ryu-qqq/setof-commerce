package com.ryuqq.setof.application.review.service.query;

import com.ryuqq.setof.application.review.assembler.ReviewAssembler;
import com.ryuqq.setof.application.review.dto.response.ProductRatingStatsResponse;
import com.ryuqq.setof.application.review.manager.query.ProductRatingStatsReadManager;
import com.ryuqq.setof.application.review.port.in.query.GetProductRatingStatsUseCase;
import com.ryuqq.setof.domain.review.aggregate.ProductRatingStats;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * 상품 평점 통계 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>ProductRatingStatsReadManager로 통계 조회
 *   <li>존재하면 ReviewAssembler로 변환, 없으면 빈 통계 반환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ProductRatingStatsQueryService implements GetProductRatingStatsUseCase {

    private final ProductRatingStatsReadManager productRatingStatsReadManager;
    private final ReviewAssembler reviewAssembler;

    public ProductRatingStatsQueryService(
            ProductRatingStatsReadManager productRatingStatsReadManager,
            ReviewAssembler reviewAssembler) {
        this.productRatingStatsReadManager = productRatingStatsReadManager;
        this.reviewAssembler = reviewAssembler;
    }

    @Override
    public ProductRatingStatsResponse execute(Long productGroupId) {
        Optional<ProductRatingStats> statsOptional =
                productRatingStatsReadManager.findByProductGroupId(productGroupId);

        return statsOptional
                .map(reviewAssembler::toProductRatingStatsResponse)
                .orElse(ProductRatingStatsResponse.empty(productGroupId));
    }
}
