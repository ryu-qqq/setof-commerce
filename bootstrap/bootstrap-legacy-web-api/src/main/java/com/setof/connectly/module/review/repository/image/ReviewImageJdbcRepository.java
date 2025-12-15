package com.setof.connectly.module.review.repository.image;

import com.setof.connectly.module.review.entity.ReviewImage;
import java.util.List;

public interface ReviewImageJdbcRepository {
    void saveAll(List<ReviewImage> reviewImages);
}
