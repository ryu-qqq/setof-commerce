package com.setof.connectly.module.review.service.image;

import com.setof.connectly.module.review.dto.ReviewImageDto;
import java.util.List;

public interface ReviewImageQueryService {

    void saveReviewImages(long reviewId, List<ReviewImageDto> reviewImages);
}
