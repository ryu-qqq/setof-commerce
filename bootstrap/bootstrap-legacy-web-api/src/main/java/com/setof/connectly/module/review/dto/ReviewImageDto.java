package com.setof.connectly.module.review.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ReviewImageDto {

    @JsonIgnore private Long reviewImageId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProductGroupImageType reviewImageType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String imageUrl;

    @QueryProjection
    public ReviewImageDto(
            Long reviewImageId, ProductGroupImageType reviewImageType, String imageUrl) {
        this.reviewImageId = reviewImageId;
        this.reviewImageType = reviewImageType;
        this.imageUrl = imageUrl;
    }

    @Override
    public int hashCode() {
        return (String.valueOf(reviewImageId)).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReviewImageDto) {
            ReviewImageDto p = (ReviewImageDto) obj;
            return this.hashCode() == p.hashCode();
        }
        return false;
    }
}
