package com.ryuqq.setof.storage.legacy.composite.search.condition;

import static com.ryuqq.setof.storage.legacy.productgroup.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.productgroup.entity.QLegacyProductGroupImageEntity.legacyProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.productgroup.entity.QLegacyProductScoreEntity.legacyProductScoreEntity;
import static com.ryuqq.setof.storage.legacy.review.entity.QLegacyProductRatingStatsEntity.legacyProductRatingStatsEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.ryuqq.setof.domain.legacy.search.dto.query.LegacySearchCondition;
import com.ryuqq.setof.storage.legacy.productgroup.entity.LegacyProductGroupEntity;
import com.ryuqq.setof.storage.legacy.productgroup.entity.LegacyProductGroupImageEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebSearchCompositeConditionBuilder - 레거시 Web 검색 Composite QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebSearchCompositeConditionBuilder {

    /**
     * MySQL ngram FULLTEXT 검색 조건.
     *
     * <p>searchWord가 null 또는 blank이면 null 반환 (전체 상품 조회로 동작).
     *
     * @param searchWord 검색 키워드
     * @return BooleanExpression
     */
    public BooleanExpression fullTextSearch(String searchWord) {
        if (searchWord == null || searchWord.isBlank()) {
            return null;
        }
        NumberTemplate<Double> matchAgainst =
                Expressions.numberTemplate(
                        Double.class,
                        "function('match_against', {0}, {1})",
                        legacyProductGroupEntity.productGroupName,
                        "+" + searchWord + "*");
        return matchAgainst.gt(0);
    }

    /**
     * 노출 상품 조건 (display_yn = 'Y').
     *
     * @return BooleanExpression
     */
    public BooleanExpression onSaleProduct() {
        return legacyProductGroupEntity.displayYn.eq(LegacyProductGroupEntity.Yn.Y);
    }

    /**
     * 메인 이미지 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression mainImageCondition() {
        return legacyProductGroupImageEntity.productGroupImageType.eq(
                LegacyProductGroupImageEntity.ProductGroupImageType.MAIN);
    }

    /**
     * 이미지 미삭제 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression imageNotDeleted() {
        return legacyProductGroupImageEntity.deleteYn.eq(LegacyProductGroupImageEntity.Yn.N);
    }

    /**
     * 카테고리 필터 조건 (단일 or 다중).
     *
     * @param condition 검색 조건
     * @return BooleanExpression
     */
    public BooleanExpression categoryFilter(LegacySearchCondition condition) {
        if (condition.categoryId() != null) {
            return legacyProductGroupEntity.categoryId.eq(condition.categoryId());
        }
        if (condition.categoryIds() != null && !condition.categoryIds().isEmpty()) {
            return legacyProductGroupEntity.categoryId.in(condition.categoryIds());
        }
        return null;
    }

    /**
     * 브랜드 필터 조건 (단일 or 다중).
     *
     * @param condition 검색 조건
     * @return BooleanExpression
     */
    public BooleanExpression brandFilter(LegacySearchCondition condition) {
        if (condition.brandId() != null) {
            return legacyProductGroupEntity.brandId.eq(condition.brandId());
        }
        if (condition.brandIds() != null && !condition.brandIds().isEmpty()) {
            return legacyProductGroupEntity.brandId.in(condition.brandIds());
        }
        return null;
    }

    /**
     * 셀러 ID 일치 조건.
     *
     * @param sellerId 셀러 ID
     * @return BooleanExpression
     */
    public BooleanExpression sellerIdEq(Long sellerId) {
        return sellerId != null ? legacyProductGroupEntity.sellerId.eq(sellerId) : null;
    }

    /**
     * 판매가 범위 조건.
     *
     * @param lowestPrice 최저가
     * @param highestPrice 최고가
     * @return BooleanExpression
     */
    public BooleanExpression betweenPrice(Long lowestPrice, Long highestPrice) {
        if (lowestPrice != null && highestPrice != null) {
            return legacyProductGroupEntity.salePrice.between(
                    lowestPrice.intValue(), highestPrice.intValue());
        }
        if (lowestPrice != null) {
            return legacyProductGroupEntity.salePrice.goe(lowestPrice.intValue());
        }
        if (highestPrice != null) {
            return legacyProductGroupEntity.salePrice.loe(highestPrice.intValue());
        }
        return null;
    }

    /**
     * RECOMMEND 커서 조건 (score 기반).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @param cursorScore 커서 스코어
     * @return BooleanExpression
     */
    public BooleanExpression recommendCursor(Long lastDomainId, Double cursorScore) {
        if (lastDomainId == null || cursorScore == null) return null;
        return legacyProductScoreEntity
                .score
                .lt(cursorScore)
                .or(
                        legacyProductScoreEntity
                                .score
                                .eq(cursorScore)
                                .and(legacyProductGroupEntity.id.loe(lastDomainId)));
    }

    /**
     * REVIEW 커서 조건 (reviewCount 기반).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @param cursorCount 커서 리뷰수
     * @return BooleanExpression
     */
    public BooleanExpression reviewCursor(Long lastDomainId, Long cursorCount) {
        if (lastDomainId == null || cursorCount == null) return null;
        return legacyProductRatingStatsEntity
                .reviewCount
                .lt(cursorCount)
                .or(
                        legacyProductRatingStatsEntity
                                .reviewCount
                                .eq(cursorCount)
                                .and(legacyProductGroupEntity.id.loe(lastDomainId)));
    }

    /**
     * HIGH_RATING 커서 조건 (averageRating 기반).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @param cursorRating 커서 평점
     * @return BooleanExpression
     */
    public BooleanExpression highRatingCursor(Long lastDomainId, Double cursorRating) {
        if (lastDomainId == null || cursorRating == null) return null;
        return legacyProductRatingStatsEntity
                .averageRating
                .lt(cursorRating)
                .or(
                        legacyProductRatingStatsEntity
                                .averageRating
                                .eq(cursorRating)
                                .and(legacyProductGroupEntity.id.loe(lastDomainId)));
    }

    /**
     * HIGH_PRICE 커서 조건 (salePrice 내림차순).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @param cursorPrice 커서 가격
     * @return BooleanExpression
     */
    public BooleanExpression highPriceCursor(Long lastDomainId, Integer cursorPrice) {
        if (lastDomainId == null || cursorPrice == null) return null;
        return legacyProductGroupEntity
                .salePrice
                .lt(cursorPrice)
                .or(
                        legacyProductGroupEntity
                                .salePrice
                                .eq(cursorPrice)
                                .and(legacyProductGroupEntity.id.loe(lastDomainId)));
    }

    /**
     * LOW_PRICE 커서 조건 (salePrice 오름차순).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @param cursorPrice 커서 가격
     * @return BooleanExpression
     */
    public BooleanExpression lowPriceCursor(Long lastDomainId, Integer cursorPrice) {
        if (lastDomainId == null || cursorPrice == null) return null;
        return legacyProductGroupEntity
                .salePrice
                .gt(cursorPrice)
                .or(
                        legacyProductGroupEntity
                                .salePrice
                                .eq(cursorPrice)
                                .and(legacyProductGroupEntity.id.loe(lastDomainId)));
    }

    /**
     * HIGH_DISCOUNT 커서 조건 (discountRate 내림차순).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @param cursorRate 커서 할인율
     * @return BooleanExpression
     */
    public BooleanExpression highDiscountCursor(Long lastDomainId, Integer cursorRate) {
        if (lastDomainId == null || cursorRate == null) return null;
        return legacyProductGroupEntity
                .discountRate
                .lt(cursorRate)
                .or(
                        legacyProductGroupEntity
                                .discountRate
                                .eq(cursorRate)
                                .and(legacyProductGroupEntity.id.loe(lastDomainId)));
    }

    /**
     * LOW_DISCOUNT 커서 조건 (discountRate 오름차순).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @param cursorRate 커서 할인율
     * @return BooleanExpression
     */
    public BooleanExpression lowDiscountCursor(Long lastDomainId, Integer cursorRate) {
        if (lastDomainId == null || cursorRate == null) return null;
        return legacyProductGroupEntity
                .discountRate
                .gt(cursorRate)
                .or(
                        legacyProductGroupEntity
                                .discountRate
                                .eq(cursorRate)
                                .and(legacyProductGroupEntity.id.loe(lastDomainId)));
    }

    /**
     * RECENT 커서 조건 (insertDate 내림차순).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @param cursorDate 커서 날짜
     * @return BooleanExpression
     */
    public BooleanExpression recentCursor(Long lastDomainId, LocalDateTime cursorDate) {
        if (lastDomainId == null || cursorDate == null) return null;
        return legacyProductGroupEntity
                .insertDate
                .lt(cursorDate)
                .or(
                        legacyProductGroupEntity
                                .insertDate
                                .eq(cursorDate)
                                .and(legacyProductGroupEntity.id.loe(lastDomainId)));
    }

    /**
     * ID 기반 커서 조건 (기본 페이징).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @return BooleanExpression
     */
    public BooleanExpression idCursor(Long lastDomainId) {
        return lastDomainId != null ? legacyProductGroupEntity.id.loe(lastDomainId) : null;
    }

    /**
     * OrderType에 따른 동적 커서 조건 빌드.
     *
     * @param condition 검색 조건
     * @return BooleanExpression
     */
    public BooleanExpression dynamicCursorCondition(LegacySearchCondition condition) {
        if (!condition.hasCursor()) {
            return idCursor(condition.lastDomainId());
        }
        String orderType = condition.orderType();
        Long lastDomainId = condition.lastDomainId();
        String cursorValue = condition.cursorValue();

        if (orderType == null) {
            return idCursor(lastDomainId);
        }

        return switch (orderType) {
            case "RECOMMEND" -> recommendCursor(lastDomainId, parseDouble(cursorValue));
            case "REVIEW" -> reviewCursor(lastDomainId, parseLong(cursorValue));
            case "HIGH_RATING" -> highRatingCursor(lastDomainId, parseDouble(cursorValue));
            case "HIGH_PRICE" -> highPriceCursor(lastDomainId, parseInt(cursorValue));
            case "LOW_PRICE" -> lowPriceCursor(lastDomainId, parseInt(cursorValue));
            case "HIGH_DISCOUNT" -> highDiscountCursor(lastDomainId, parseInt(cursorValue));
            case "LOW_DISCOUNT" -> lowDiscountCursor(lastDomainId, parseInt(cursorValue));
            case "RECENT" -> recentCursor(lastDomainId, parseDateTime(cursorValue));
            default -> idCursor(lastDomainId);
        };
    }

    /**
     * OrderType에 따른 정렬 기준 생성.
     *
     * @param orderType 정렬 타입 (null이면 RECOMMEND로 처리)
     * @return OrderSpecifier 목록
     */
    public List<OrderSpecifier<?>> buildOrderSpecifiers(String orderType) {
        String effectiveOrderType = (orderType == null) ? "RECOMMEND" : orderType;
        return switch (effectiveOrderType) {
            case "RECOMMEND" ->
                    List.of(
                            legacyProductScoreEntity.score.coalesce(0.0).desc(),
                            legacyProductGroupEntity.id.desc());
            case "REVIEW" ->
                    List.of(
                            legacyProductRatingStatsEntity.reviewCount.coalesce(0L).desc(),
                            legacyProductGroupEntity.id.desc());
            case "HIGH_RATING" ->
                    List.of(
                            legacyProductRatingStatsEntity.averageRating.coalesce(0.0).desc(),
                            legacyProductGroupEntity.id.desc());
            case "LOW_PRICE" ->
                    List.of(
                            legacyProductGroupEntity.salePrice.asc(),
                            legacyProductGroupEntity.id.desc());
            case "HIGH_PRICE" ->
                    List.of(
                            legacyProductGroupEntity.salePrice.desc(),
                            legacyProductGroupEntity.id.desc());
            case "LOW_DISCOUNT" ->
                    List.of(
                            legacyProductGroupEntity.discountRate.asc(),
                            legacyProductGroupEntity.id.desc());
            case "HIGH_DISCOUNT" ->
                    List.of(
                            legacyProductGroupEntity.discountRate.desc(),
                            legacyProductGroupEntity.id.desc());
            case "RECENT" ->
                    List.of(
                            legacyProductGroupEntity.insertDate.desc(),
                            legacyProductGroupEntity.id.desc());
            default ->
                    List.of(
                            legacyProductScoreEntity.score.coalesce(0.0).desc(),
                            legacyProductGroupEntity.id.desc());
        };
    }

    private Double parseDouble(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInt(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDateTime parseDateTime(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return LocalDateTime.parse(value);
        } catch (Exception e) {
            return null;
        }
    }
}
