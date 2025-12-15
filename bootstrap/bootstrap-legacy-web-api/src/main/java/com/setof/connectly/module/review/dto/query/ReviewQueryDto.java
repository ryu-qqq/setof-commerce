package com.setof.connectly.module.review.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewQueryDto {
    private long reviewId;
    private double rating;
    private double score;

    @QueryProjection
    public ReviewQueryDto(long reviewId, double rating, double score) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.score = score;
    }
}
