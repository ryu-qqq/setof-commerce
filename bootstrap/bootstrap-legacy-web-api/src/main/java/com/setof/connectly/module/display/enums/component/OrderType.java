package com.setof.connectly.module.display.enums.component;

import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;

import com.querydsl.core.types.Order;
import com.setof.connectly.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType implements EnumType {
    NONE("", "", DESC),
    RECOMMEND("score", "score", DESC),
    REVIEW("reviewCount", "review_count", DESC),
    RECENT("updateDate", "update_date", DESC),
    HIGH_RATING("averageRating", "average_rating", DESC),
    LOW_PRICE("productGroupDetails.price.salePrice", "sale_price", ASC),
    HIGH_PRICE("productGroupDetails.price.salePrice", "sale_price", DESC),
    LOW_DISCOUNT("productGroupDetails.price.discountRate", "discount_rate", ASC),
    HIGH_DISCOUNT("productGroupDetails.price.discountRate", "discount_rate", DESC);

    private final String field;

    private final String elkField;
    private final Order direction;

    public boolean isAscending() {
        return this.direction.equals(ASC);
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return "정렬 필드명 " + field + " 정렬 방향 " + direction;
    }

    public boolean isRecommend() {
        return this.equals(RECOMMEND);
    }

    public boolean isReview() {
        return this.equals(REVIEW);
    }

    public boolean isHighRating() {
        return this.equals(HIGH_RATING);
    }
}
