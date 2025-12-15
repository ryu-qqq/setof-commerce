package com.setof.connectly.module.review.service.stat.query;

import com.setof.connectly.module.review.entity.ProductRatingStats;
import com.setof.connectly.module.review.entity.Review;
import com.setof.connectly.module.review.repository.stat.ProductRatingRepository;
import com.setof.connectly.module.review.service.stat.fetch.ProductRatingStatFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductRatingStatQueryServiceImpl implements ProductRatingStatQueryService {

    private final ProductRatingStatFindService productRatingStatFindService;
    private final ProductRatingRepository productRatingRepository;

    @Override
    public ProductRatingStats saveEntity(long productGroupId) {
        return productRatingRepository.save(new ProductRatingStats(productGroupId));
    }

    @Override
    public void updateRatingStats(Review review) {
        ProductRatingStats stats =
                productRatingStatFindService
                        .fetchProductRatingStats(review.getProductGroupId())
                        .orElseGet(() -> saveEntity(review.getProductGroupId()));

        stats.updateAverageRating(review.getRating());
    }

    @Override
    public void rollBackProductRating(Review review) {

        ProductRatingStats stats =
                productRatingStatFindService
                        .fetchProductRatingStats(review.getProductGroupId())
                        .orElseGet(() -> saveEntity(review.getProductGroupId()));

        stats.updateAverageRating(review.getRating());
    }
}
