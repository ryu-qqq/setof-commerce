package com.setof.connectly.module.review.service.image;

import com.setof.connectly.module.review.dto.ReviewImageDto;
import com.setof.connectly.module.review.entity.ReviewImage;
import com.setof.connectly.module.review.mapper.ReviewImageMapper;
import com.setof.connectly.module.review.repository.image.ReviewImageJdbcRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ReviewImageQueryServiceImpl implements ReviewImageQueryService {

    private final ReviewImageMapper reviewImageMapper;
    private final ReviewImageJdbcRepository reviewImageJdbcRepository;

    @Override
    public void saveReviewImages(long reviewId, List<ReviewImageDto> reviewImages) {
        List<ReviewImage> images = reviewImageMapper.toReviewImageEntities(reviewId, reviewImages);
        reviewImageJdbcRepository.saveAll(images);
    }
}
