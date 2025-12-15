package com.setof.connectly.module.common.repository;

import static com.setof.connectly.module.product.entity.group.QProductGroup.productGroup;
import static com.setof.connectly.module.review.entity.QProductRatingStats.productRatingStats;
import static com.setof.connectly.module.search.entity.QProductScore.productScore;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.setof.connectly.module.common.filter.ItemFilter;
import com.setof.connectly.module.display.enums.component.OrderType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public abstract class AbstractCommonRepository implements CommonRepository {

    public List<OrderSpecifier<?>> createOrderSpecifiersFromPageable(
            EntityPathBase<?> entityPath, OrderType orderType) {
        List<OrderSpecifier<?>> orderSpecifiers = new LinkedList<>();
        if (orderType != null) {
            Order direction = orderType.isAscending() ? Order.ASC : Order.DESC;
            String field = orderType.getField();

            EntityPathBase<?> tempEntityPath = entityPath;
            if (orderType.isReview() || orderType.isHighRating()) {
                tempEntityPath = productRatingStats;
            } else if (orderType.isRecommend()) {
                tempEntityPath = productScore;
            }

            orderSpecifiers.add(getSortedColumn(direction, tempEntityPath, field));
            orderSpecifiers.add(getSortedColumn(Order.DESC, productGroup, "id"));
            return orderSpecifiers;
        }
        orderSpecifiers.add(getSortedColumn(Order.DESC, entityPath, "id"));
        return orderSpecifiers;
    }

    public BooleanExpression createDynamicWhereCondition(ItemFilter filter, OrderType orderType) {
        String cursorValue = filter.getCursorValue();
        Long lastDomainId = filter.getLastDomainId();

        if (StringUtils.hasText(cursorValue) && lastDomainId != null) {
            BooleanExpression baseCondition = null;
            BooleanExpression idCondition = productGroup.id.loe(lastDomainId);

            switch (orderType) {
                case RECENT:
                    LocalDateTime dateCursor = parseToLocalDateTime(cursorValue);
                    baseCondition =
                            productGroup
                                    .insertDate
                                    .lt(dateCursor)
                                    .or(productGroup.insertDate.eq(dateCursor).and(idCondition));
                    break;
                case HIGH_PRICE:
                    Long highPrice = parseToLong(cursorValue);
                    baseCondition =
                            productGroup
                                    .productGroupDetails
                                    .price
                                    .salePrice
                                    .lt(highPrice)
                                    .or(
                                            productGroup
                                                    .productGroupDetails
                                                    .price
                                                    .salePrice
                                                    .eq(highPrice)
                                                    .and(idCondition));
                    break;
                case LOW_PRICE:
                    Long lowPrice = parseToLong(cursorValue);
                    baseCondition =
                            productGroup
                                    .productGroupDetails
                                    .price
                                    .salePrice
                                    .gt(lowPrice)
                                    .or(
                                            productGroup
                                                    .productGroupDetails
                                                    .price
                                                    .salePrice
                                                    .eq(lowPrice)
                                                    .and(idCondition));
                    break;
                case HIGH_DISCOUNT:
                    Integer highDiscount = parseToInteger(cursorValue);
                    baseCondition =
                            productGroup
                                    .productGroupDetails
                                    .price
                                    .discountRate
                                    .lt(highDiscount)
                                    .or(
                                            productGroup
                                                    .productGroupDetails
                                                    .price
                                                    .discountRate
                                                    .eq(highDiscount)
                                                    .and(idCondition));
                    break;
                case LOW_DISCOUNT:
                    Integer lowDiscount = parseToInteger(cursorValue);
                    baseCondition =
                            productGroup
                                    .productGroupDetails
                                    .price
                                    .discountRate
                                    .gt(lowDiscount)
                                    .or(
                                            productGroup
                                                    .productGroupDetails
                                                    .price
                                                    .discountRate
                                                    .eq(lowDiscount)
                                                    .and(idCondition));
                    break;
                case HIGH_RATING:
                    Double highRating = parseToDouble(cursorValue);
                    baseCondition =
                            productRatingStats
                                    .averageRating
                                    .lt(highRating)
                                    .or(
                                            productRatingStats
                                                    .averageRating
                                                    .eq(highRating)
                                                    .and(idCondition));
                    break;
                case REVIEW:
                    Long highReview = parseToLong(cursorValue);

                    BooleanExpression reviewCountLessThanHighReview =
                            Expressions.asNumber(productRatingStats.reviewCount.coalesce(0L))
                                    .lt(highReview);

                    BooleanExpression reviewEqualToHighReviewAndIdCondition =
                            Expressions.asNumber(productRatingStats.reviewCount.coalesce(0L))
                                    .eq(highReview)
                                    .and(idCondition);

                    baseCondition =
                            reviewCountLessThanHighReview.or(reviewEqualToHighReviewAndIdCondition);

                    break;
                case RECOMMEND:
                    Double highScore = parseToDouble(cursorValue);

                    BooleanExpression scoreLessThanHighScore =
                            Expressions.asNumber(productScore.score.coalesce(0.0)).lt(highScore);

                    BooleanExpression scoreEqualToHighScoreAndIdCondition =
                            Expressions.asNumber(productScore.score.coalesce(0.0))
                                    .eq(highScore)
                                    .and(idCondition);

                    baseCondition = scoreLessThanHighScore.or(scoreEqualToHighScoreAndIdCondition);
                    break;

                default:
                    throw new IllegalArgumentException("지원하지 않는 정렬 유형입니다.");
            }
            return baseCondition;
        }

        return createDynamicProductGroupIdCondition(filter);
    }

    protected OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }

    protected BooleanExpression createDynamicProductGroupIdCondition(ItemFilter filter) {
        Long lastDomainId = filter.getLastDomainId();
        if (lastDomainId == null) return null;

        OrderType orderType = filter.getOrderType();
        boolean isAscending = false;
        if (orderType != null) isAscending = orderType.isAscending();

        if (isAscending) {
            return productGroup.id.goe(lastDomainId);
        } else {
            return productGroup.id.loe(lastDomainId);
        }
    }

    private LocalDateTime parseToLocalDateTime(String value) {
        if (value.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(value, formatter);
        } else {
            return LocalDateTime.parse(value);
        }
    }

    private Long parseToLong(String value) {
        return Long.parseLong(value);
    }

    private Integer parseToInteger(String value) {
        return Integer.parseInt(value);
    }

    private Double parseToDouble(String value) {
        return Double.parseDouble(value);
    }
}
