package com.setof.connectly.module.review.mapper;

import com.setof.connectly.module.review.dto.ReviewImageDto;
import com.setof.connectly.module.review.entity.ReviewImage;
import java.util.List;

public interface ReviewImageMapper {
    List<ReviewImage> toReviewImageEntities(long reviewId, List<ReviewImageDto> reviewImages);
}
