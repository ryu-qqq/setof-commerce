package com.ryuqq.setof.application.review.manager;

import com.ryuqq.setof.application.review.port.out.query.ReviewCompositeQueryPort;
import com.ryuqq.setof.domain.review.query.AvailableReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.ReviewableOrder;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ReviewCompositeReadManager - 리뷰 복합 조회 Manager.
 *
 * <p>크로스 도메인(Order 등) 데이터를 조합하여 리뷰 컨텍스트에서 사용합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@Transactional(readOnly = true)
public class ReviewCompositeReadManager {

    private final ReviewCompositeQueryPort reviewCompositeQueryPort;

    public ReviewCompositeReadManager(ReviewCompositeQueryPort reviewCompositeQueryPort) {
        this.reviewCompositeQueryPort = reviewCompositeQueryPort;
    }

    public List<ReviewableOrder> fetchAvailableReviewOrders(
            AvailableReviewSearchCriteria criteria) {
        return reviewCompositeQueryPort.fetchAvailableReviewOrders(criteria);
    }

    public long countAvailableReviewOrders(AvailableReviewSearchCriteria criteria) {
        return reviewCompositeQueryPort.countAvailableReviewOrders(criteria);
    }
}
