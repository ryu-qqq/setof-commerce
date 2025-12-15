package com.setof.connectly.module.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.mapper.LastDomainIdProvider;
import com.setof.connectly.module.product.dto.brand.BrandDto;
import com.setof.connectly.module.product.dto.option.OptionDto;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ReviewDto implements LastDomainIdProvider {

    private long reviewId;
    private long orderId;
    private String userName;
    private double rating;
    private String content;
    private long productGroupId;
    private String productGroupName;
    private String productGroupImageUrl;
    private BrandDto brand;
    private long categoryId;
    private String categoryName;
    private Set<ReviewImageDto> reviewImages;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    public String option = "";

    @JsonIgnore public Set<OptionDto> options = new HashSet<>();

    @QueryProjection
    public ReviewDto(
            long reviewId,
            long orderId,
            String userName,
            double rating,
            String content,
            long productGroupId,
            String productGroupName,
            String productGroupImageUrl,
            BrandDto brand,
            long categoryId,
            String categoryName,
            Set<ReviewImageDto> reviewImages,
            LocalDateTime insertDate,
            LocalDateTime paymentDate,
            Set<OptionDto> options) {
        this.reviewId = reviewId;
        this.orderId = orderId;
        this.userName = userName;
        this.rating = rating;
        this.content = content;
        this.productGroupId = productGroupId;
        this.productGroupName = productGroupName;
        this.productGroupImageUrl = productGroupImageUrl;
        this.brand = brand;
        this.categoryId = categoryId;
        this.categoryName = categoryName;

        this.reviewImages =
                reviewImages.stream()
                        .filter(
                                image ->
                                        image != null
                                                && (image.getReviewImageId() != null
                                                        || image.getReviewImageType() != null
                                                        || image.getImageUrl() != null))
                        .collect(Collectors.toSet());

        if (this.reviewImages.isEmpty()) {
            this.reviewImages = Collections.emptySet();
        }

        this.insertDate = insertDate;
        this.paymentDate = paymentDate;
        this.options = options;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public Long getId() {
        return reviewId;
    }
}
