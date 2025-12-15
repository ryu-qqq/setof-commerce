package com.setof.connectly.module.review.service.stat.fetch;

import com.setof.connectly.module.review.entity.ProductRatingStats;
import com.setof.connectly.module.review.repository.stat.ProductRatingFindRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductRatingStatFindServiceImpl implements ProductRatingStatFindService {

    private final ProductRatingFindRepository productRatingFindRepository;

    @Override
    public Optional<ProductRatingStats> fetchProductRatingStats(long productGroupId) {
        return productRatingFindRepository.fetchProductRatingStats(productGroupId);
    }
}
