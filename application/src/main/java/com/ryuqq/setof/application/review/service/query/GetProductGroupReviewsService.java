package com.ryuqq.setof.application.review.service.query;

import com.ryuqq.setof.application.review.assembler.ReviewAssembler;
import com.ryuqq.setof.application.review.dto.query.ProductGroupReviewSearchParams;
import com.ryuqq.setof.application.review.dto.response.ReviewPageResult;
import com.ryuqq.setof.application.review.factory.ReviewQueryFactory;
import com.ryuqq.setof.application.review.manager.ReviewReadManager;
import com.ryuqq.setof.application.review.port.in.query.GetProductGroupReviewsUseCase;
import com.ryuqq.setof.domain.review.query.ProductGroupReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.WrittenReview;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetProductGroupReviewsService - 상품그룹 리뷰 조회 Service.
 *
 * <p>Offset 기반 페이징. Public 엔드포인트 대응.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetProductGroupReviewsService implements GetProductGroupReviewsUseCase {

    private final ReviewQueryFactory queryFactory;
    private final ReviewReadManager readManager;
    private final ReviewAssembler assembler;

    public GetProductGroupReviewsService(
            ReviewQueryFactory queryFactory,
            ReviewReadManager readManager,
            ReviewAssembler assembler) {
        this.queryFactory = queryFactory;
        this.readManager = readManager;
        this.assembler = assembler;
    }

    @Override
    public ReviewPageResult execute(ProductGroupReviewSearchParams params) {
        ProductGroupReviewSearchCriteria criteria =
                queryFactory.createProductGroupReviewCriteria(params);

        List<WrittenReview> reviews = readManager.fetchProductGroupReviews(criteria);
        long totalElements = readManager.countProductGroupReviews(criteria.productGroupId());
        double averageRating =
                readManager.fetchAverageRating(criteria.productGroupId()).orElse(0.0);

        return assembler.toReviewPageResult(
                reviews, criteria.page(), criteria.size(), totalElements, averageRating);
    }
}
