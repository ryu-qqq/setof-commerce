package com.setof.connectly.module.review.mapper;

import com.setof.connectly.module.review.dto.CreateReview;
import com.setof.connectly.module.review.dto.ReviewDto;
import com.setof.connectly.module.review.dto.page.ReviewPage;
import com.setof.connectly.module.review.entity.Review;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ReviewMapper {

    Review toEntity(long userId, CreateReview createReview);

    List<ReviewDto> setOptions(List<ReviewDto> reviews);

    ReviewPage<ReviewDto> toPage(Page<ReviewDto> page, double averageRating);
}
