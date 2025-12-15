package com.setof.connectly.module.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.display.enums.component.OrderType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CursorDto {

    private long lastDomainId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;

    private long salePrice;
    private int discountRate;
    private double averageRating;
    private long reviewCount;

    private double score;

    @QueryProjection
    public CursorDto(
            long lastDomainId,
            LocalDateTime insertDate,
            long salePrice,
            int discountRate,
            double averageRating,
            long reviewCount,
            double score) {
        this.lastDomainId = lastDomainId;
        this.insertDate = insertDate;
        this.salePrice = salePrice;
        this.discountRate = discountRate;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.score = score;
    }

    public String getCursorValue(OrderType orderType) {
        if (orderType == null) return null;
        switch (orderType) {
            case NONE:
                return null;
            case RECENT:
                return insertDate.toString();
            case HIGH_PRICE:
            case LOW_PRICE:
                return String.valueOf(salePrice);
            case HIGH_DISCOUNT:
            case LOW_DISCOUNT:
                return String.valueOf(discountRate);
            case HIGH_RATING:
                return String.valueOf(averageRating);
            case REVIEW:
                return String.valueOf(reviewCount);
            case RECOMMEND:
                return String.valueOf(score);
            default:
                throw new IllegalArgumentException("지원하지 않는 정렬 유형 입니다.");
        }
    }
}
