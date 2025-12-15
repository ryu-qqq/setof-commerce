package com.setof.connectly.module.review.service.query;

import com.setof.connectly.module.exception.review.AlreadyReviewWrittenException;
import com.setof.connectly.module.review.dto.CreateReview;
import com.setof.connectly.module.review.entity.Review;
import com.setof.connectly.module.review.mapper.ReviewMapper;
import com.setof.connectly.module.review.repository.ReviewRepository;
import com.setof.connectly.module.review.service.fetch.ReviewFindService;
import com.setof.connectly.module.review.service.image.ReviewImageQueryService;
import com.setof.connectly.module.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageQueryService reviewImageQueryService;
    private final ReviewMapper reviewMapper;
    private final ReviewFindService reviewFindService;

    @Override
    public Review doReview(CreateReview createReview) {
        boolean reviewAlreadyWritten =
                reviewFindService.isReviewAlreadyWritten(createReview.getOrderId());
        if (reviewAlreadyWritten) throw new AlreadyReviewWrittenException();
        long userId = SecurityUtils.currentUserId();
        Review review = reviewMapper.toEntity(userId, createReview);

        Review savedReview = reviewRepository.save(review);
        if (!createReview.getReviewImages().isEmpty())
            reviewImageQueryService.saveReviewImages(
                    savedReview.getId(), createReview.getReviewImages());

        return savedReview;
    }

    @Override
    public Review deleteReview(long reviewId) {
        long userId = SecurityUtils.currentUserId();
        Review review = reviewFindService.fetchReviewEntity(reviewId, userId);
        review.delete();
        return review;
    }
}
