package com.ryuqq.setof.application.review.port.out.query;

import com.ryuqq.setof.domain.review.query.AvailableReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.ReviewableOrder;
import java.util.List;

/**
 * ReviewCompositeQueryPort - 리뷰 복합 조회 Port.
 *
 * <p>크로스 도메인(Order 등) 데이터를 조합하여 리뷰 컨텍스트에서 사용하는 조회 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ReviewCompositeQueryPort {

    List<ReviewableOrder> fetchAvailableReviewOrders(AvailableReviewSearchCriteria criteria);

    long countAvailableReviewOrders(AvailableReviewSearchCriteria criteria);
}
