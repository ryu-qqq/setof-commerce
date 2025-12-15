package com.setof.connectly.module.review.service.query;

import com.setof.connectly.module.review.dto.CreateReview;
import com.setof.connectly.module.review.entity.Review;

public interface ReviewQueryService {

    Review doReview(CreateReview createReview);

    Review deleteReview(long reviewId);
}
