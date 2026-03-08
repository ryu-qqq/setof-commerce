package com.ryuqq.setof.application.review.service.query;

import com.ryuqq.setof.application.review.assembler.ReviewAssembler;
import com.ryuqq.setof.application.review.dto.query.AvailableReviewSearchParams;
import com.ryuqq.setof.application.review.dto.response.AvailableReviewSliceResult;
import com.ryuqq.setof.application.review.factory.ReviewQueryFactory;
import com.ryuqq.setof.application.review.manager.ReviewCompositeReadManager;
import com.ryuqq.setof.application.review.port.in.query.GetAvailableReviewsUseCase;
import com.ryuqq.setof.domain.review.query.AvailableReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.ReviewableOrder;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetAvailableReviewsService - 작성 가능한 리뷰 주문 조회 Service.
 *
 * <p>커서 기반 페이징. 인증 필요 엔드포인트 대응. Order 도메인 데이터 기반 조회.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetAvailableReviewsService implements GetAvailableReviewsUseCase {

    private final ReviewQueryFactory queryFactory;
    private final ReviewCompositeReadManager compositeReadManager;
    private final ReviewAssembler assembler;

    public GetAvailableReviewsService(
            ReviewQueryFactory queryFactory,
            ReviewCompositeReadManager compositeReadManager,
            ReviewAssembler assembler) {
        this.queryFactory = queryFactory;
        this.compositeReadManager = compositeReadManager;
        this.assembler = assembler;
    }

    @Override
    public AvailableReviewSliceResult execute(AvailableReviewSearchParams params) {
        AvailableReviewSearchCriteria criteria = queryFactory.createAvailableReviewCriteria(params);
        List<ReviewableOrder> orders = compositeReadManager.fetchAvailableReviewOrders(criteria);
        long totalElements = compositeReadManager.countAvailableReviewOrders(criteria);
        return assembler.toAvailableReviewSliceResult(orders, criteria.size(), totalElements);
    }
}
