package com.setof.connectly.module.review.service.stat.query;

import com.setof.connectly.module.review.entity.ProductRatingStats;
import com.setof.connectly.module.review.entity.Review;

public interface ProductRatingStatQueryService {

    ProductRatingStats saveEntity(long productGroupId);

    void updateRatingStats(Review review);

    void rollBackProductRating(Review review);
}
