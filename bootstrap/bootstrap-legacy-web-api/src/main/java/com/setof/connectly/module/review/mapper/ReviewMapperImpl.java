package com.setof.connectly.module.review.mapper;

import com.setof.connectly.module.product.dto.option.OptionDto;
import com.setof.connectly.module.review.dto.CreateReview;
import com.setof.connectly.module.review.dto.ReviewDto;
import com.setof.connectly.module.review.dto.page.ReviewPage;
import com.setof.connectly.module.review.entity.Review;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Review toEntity(long userId, CreateReview createReview) {
        return Review.builder()
                .orderId(createReview.getOrderId())
                .productGroupId(createReview.getProductGroupId())
                .rating(createReview.getRating())
                .userId(userId)
                .content(createReview.getContent())
                .build();
    }

    public List<ReviewDto> setOptions(List<ReviewDto> reviews) {
        reviews.forEach(
                review -> {
                    if (!review.options.isEmpty()) {
                        review.option = getOptionName(review.options);
                    } else review.option = "";
                });
        return reviews;
    }

    @Override
    public ReviewPage<ReviewDto> toPage(Page<ReviewDto> page, double averageRating) {
        return ReviewPage.<ReviewDto>builder()
                .content(page.getContent())
                .pageable(page.getPageable())
                .last(page.isLast())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .first(page.isFirst())
                .number(page.getNumber())
                .sort(page.getSort())
                .size(page.getSize())
                .numberOfElements(page.getNumberOfElements())
                .empty(page.isEmpty())
                .originalPage(page)
                .averageRating(averageRating)
                .build();
    }

    private String getOptionName(Set<OptionDto> options) {
        return options.stream()
                .sorted(Comparator.comparing(OptionDto::getOptionGroupId))
                .map(OptionDto::getOptionValue)
                .collect(Collectors.joining("/"));
    }
}
