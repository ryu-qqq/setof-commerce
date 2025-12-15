package com.setof.connectly.module.review.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.review.dto.ReviewDto;
import com.setof.connectly.module.review.entity.Review;
import com.setof.connectly.module.review.filter.ReviewFilter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface ReviewFindRepository {
    List<ReviewDto> fetchReviews(ReviewFilter filter, Pageable pageable);

    List<ReviewDto> fetchMyReviews(long userId, Long lastDomainId, Pageable pageable);

    JPAQuery<Long> fetchMyReviewCountQuery(long userId);

    JPAQuery<Long> fetchReviewCountQuery(Long productGroupId);

    Optional<Review> fetchReviewEntity(long reviewId, long userId);

    Optional<Double> fetchAverageRating(long productGroupId);

    boolean isReviewAlreadyWritten(long orderId, long userId);

    boolean isReWriteReview(long orderId, long userId);
}
