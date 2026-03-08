package com.ryuqq.setof.application.review.service.query;

import com.ryuqq.setof.application.review.assembler.ReviewAssembler;
import com.ryuqq.setof.application.review.dto.query.MyReviewSearchParams;
import com.ryuqq.setof.application.review.dto.response.ReviewSliceResult;
import com.ryuqq.setof.application.review.factory.ReviewQueryFactory;
import com.ryuqq.setof.application.review.manager.ReviewReadManager;
import com.ryuqq.setof.application.review.port.in.query.GetMyReviewsUseCase;
import com.ryuqq.setof.domain.review.query.MyReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.WrittenReview;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetMyReviewsService - 내 리뷰 조회 Service.
 *
 * <p>Cursor 기반 페이징. 인증 필요 엔드포인트 대응.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetMyReviewsService implements GetMyReviewsUseCase {

    private final ReviewQueryFactory queryFactory;
    private final ReviewReadManager readManager;
    private final ReviewAssembler assembler;

    public GetMyReviewsService(
            ReviewQueryFactory queryFactory,
            ReviewReadManager readManager,
            ReviewAssembler assembler) {
        this.queryFactory = queryFactory;
        this.readManager = readManager;
        this.assembler = assembler;
    }

    @Override
    public ReviewSliceResult execute(MyReviewSearchParams params) {
        MyReviewSearchCriteria criteria = queryFactory.createMyReviewCriteria(params);
        List<WrittenReview> reviews = readManager.fetchMyReviews(criteria);
        long totalElements = readManager.countMyReviews(criteria);
        return assembler.toReviewSliceResult(reviews, criteria.size(), totalElements);
    }
}
