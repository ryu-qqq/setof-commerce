package com.setof.connectly.module.review.repository.stat;

import com.setof.connectly.module.review.entity.ProductRatingStats;
import java.util.Optional;

public interface ProductRatingFindRepository {

    Optional<ProductRatingStats> fetchProductRatingStats(long productGroupId);
}
