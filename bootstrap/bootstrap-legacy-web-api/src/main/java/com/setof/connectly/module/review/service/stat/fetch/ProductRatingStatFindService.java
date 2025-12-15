package com.setof.connectly.module.review.service.stat.fetch;

import com.setof.connectly.module.review.entity.ProductRatingStats;
import java.util.Optional;

public interface ProductRatingStatFindService {

    Optional<ProductRatingStats> fetchProductRatingStats(long productGroupId);
}
