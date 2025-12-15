package com.setof.connectly.module.review.service.fetch;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.review.dto.ReviewDto;
import com.setof.connectly.module.review.dto.ReviewOrderProductDto;
import com.setof.connectly.module.review.dto.page.ReviewPage;
import com.setof.connectly.module.review.entity.Review;
import com.setof.connectly.module.review.filter.ReviewFilter;
import org.springframework.data.domain.Pageable;

public interface ReviewFindService {
    ReviewPage<ReviewDto> fetchReviews(ReviewFilter filter, Pageable pageable);

    Review fetchReviewEntity(long reviewId, long userId);

    CustomSlice<ReviewDto> fetchMyReviews(ReviewFilter filter, Pageable pageable);

    CustomSlice<ReviewOrderProductDto> fetchAvailableReviews(
            ReviewFilter filter, Pageable pageable);

    boolean isReviewAlreadyWritten(long orderId);

    boolean isReWriteReview(long orderId);
}
