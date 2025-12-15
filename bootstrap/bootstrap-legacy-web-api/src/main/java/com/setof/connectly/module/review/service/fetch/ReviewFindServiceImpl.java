package com.setof.connectly.module.review.service.fetch;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.exception.review.ReviewNotFoundException;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import com.setof.connectly.module.review.dto.ReviewDto;
import com.setof.connectly.module.review.dto.ReviewOrderProductDto;
import com.setof.connectly.module.review.dto.page.ReviewPage;
import com.setof.connectly.module.review.entity.Review;
import com.setof.connectly.module.review.filter.ReviewFilter;
import com.setof.connectly.module.review.mapper.ReviewMapper;
import com.setof.connectly.module.review.mapper.ReviewSliceMapper;
import com.setof.connectly.module.review.repository.ReviewFindRepository;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ReviewFindServiceImpl implements ReviewFindService {

    private final ReviewMapper reviewMapper;

    private final ReviewSliceMapper reviewSliceMapper;
    private final ReviewFindRepository reviewFindRepository;
    private final OrderFindService orderFindService;

    @Override
    public Review fetchReviewEntity(long reviewId, long userId) {
        return reviewFindRepository
                .fetchReviewEntity(reviewId, userId)
                .orElseThrow(ReviewNotFoundException::new);
    }

    @Override
    public ReviewPage<ReviewDto> fetchReviews(ReviewFilter filter, Pageable pageable) {
        List<ReviewDto> results = reviewFindRepository.fetchReviews(filter, pageable);
        List<ReviewDto> reviews = reviewMapper.setOptions(results);
        Page<ReviewDto> page =
                PageableExecutionUtils.getPage(
                        reviews,
                        pageable,
                        () ->
                                reviewFindRepository
                                        .fetchReviewCountQuery(filter.getProductGroupId())
                                        .fetchCount());
        double averageRating = 0.0;
        if (filter.getProductGroupId() != null) {
            Optional<Double> averageRatingOpt =
                    reviewFindRepository.fetchAverageRating(filter.getProductGroupId());
            if (averageRatingOpt.isPresent()) averageRating = averageRatingOpt.get();
        }

        return reviewMapper.toPage(page, averageRating);
    }

    @Override
    public CustomSlice<ReviewDto> fetchMyReviews(ReviewFilter filter, Pageable pageable) {
        long userId = SecurityUtils.currentUserId();
        List<ReviewDto> myReviews =
                reviewFindRepository.fetchMyReviews(userId, filter.getLastDomainId(), pageable);
        List<ReviewDto> reviews = reviewMapper.setOptions(myReviews);
        JPAQuery<Long> longJPAQuery = reviewFindRepository.fetchMyReviewCountQuery(userId);
        return reviewSliceMapper.toSlice(reviews, pageable, longJPAQuery.fetchCount());
    }

    @Override
    public CustomSlice<ReviewOrderProductDto> fetchAvailableReviews(
            ReviewFilter filter, Pageable pageable) {
        return orderFindService.fetchAvailableReviewOrders(filter.getLastDomainId(), pageable);
    }

    @Override
    public boolean isReviewAlreadyWritten(long orderId) {
        long userId = SecurityUtils.currentUserId();
        return reviewFindRepository.isReviewAlreadyWritten(orderId, userId);
    }

    @Override
    public boolean isReWriteReview(long orderId) {
        long userId = SecurityUtils.currentUserId();
        return reviewFindRepository.isReWriteReview(orderId, userId);
    }
}
