package com.setof.connectly.module.review.mapper;

import com.setof.connectly.module.image.enums.ImagePath;
import com.setof.connectly.module.image.service.ImageUploadService;
import com.setof.connectly.module.review.dto.ReviewImageDto;
import com.setof.connectly.module.review.entity.ReviewImage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewImageMapperImpl implements ReviewImageMapper {

    private final ImageUploadService imageUploadService;

    @Override
    public List<ReviewImage> toReviewImageEntities(
            long reviewId, List<ReviewImageDto> reviewImages) {
        return reviewImages.stream()
                .map(
                        reviewImage -> {
                            CompletableFuture<String> future =
                                    imageUploadService.uploadImage(
                                            ImagePath.REVIEW, reviewImage.getImageUrl());

                            return ReviewImage.builder()
                                    .reviewId(reviewId)
                                    .reviewImageType(reviewImage.getReviewImageType())
                                    .imageUrl(future.join())
                                    .build();
                        })
                .collect(Collectors.toList());
    }
}
