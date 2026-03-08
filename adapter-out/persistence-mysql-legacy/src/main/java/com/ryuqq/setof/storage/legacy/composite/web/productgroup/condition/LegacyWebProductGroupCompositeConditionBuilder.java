package com.ryuqq.setof.storage.legacy.composite.web.productgroup.condition;

import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupImageEntity.legacyProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductRatingStatsEntity.legacyProductRatingStatsEntity;
import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductScoreEntity.legacyProductScoreEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.legacy.product.dto.query.LegacyProductGroupSearchCondition;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductGroupImageEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebProductGroupCompositeConditionBuilder - 레거시 Web 상품그룹 Composite QueryDSL 조건 빌더.
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
public class LegacyWebProductGroupCompositeConditionBuilder {

    /**
     * 상품그룹 ID 일치 조건.
     *
     * @param productGroupId 상품그룹 ID
     * @return BooleanExpression
     */
    public BooleanExpression productGroupIdEq(Long productGroupId) {
        return productGroupId != null ? legacyProductGroupEntity.id.eq(productGroupId) : null;
    }

    /**
     * 상품그룹 ID IN 조건.
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return BooleanExpression
     */
    public BooleanExpression productGroupIdIn(List<Long> productGroupIds) {
        if (productGroupIds == null || productGroupIds.isEmpty()) {
            return null;
        }
        return legacyProductGroupEntity.id.in(productGroupIds);
    }

    /**
     * 브랜드 ID 일치 조건.
     *
     * @param brandId 브랜드 ID
     * @return BooleanExpression
     */
    public BooleanExpression brandIdEq(Long brandId) {
        return brandId != null ? legacyProductGroupEntity.brandId.eq(brandId) : null;
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
     * 카테고리 ID 일치 조건.
     *
     * @param categoryId 카테고리 ID
     * @return BooleanExpression
     */
    public BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? legacyProductGroupEntity.categoryId.eq(categoryId) : null;
    }

    /**
     * 카테고리 ID IN 조건.
     *
     * @param categoryIds 카테고리 ID 목록
     * @return BooleanExpression
     */
    public BooleanExpression categoryIdIn(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return null;
        }
        return legacyProductGroupEntity.categoryId.in(categoryIds);
    }

    /**
     * 카테고리 필터 조건 (단일 or 다중).
     *
     * @param condition 검색 조건
     * @return BooleanExpression
     */
    public BooleanExpression categoryFilter(LegacyProductGroupSearchCondition condition) {
        if (condition.categoryId() != null) {
            return categoryIdEq(condition.categoryId());
        }
        if (condition.categoryIds() != null && !condition.categoryIds().isEmpty()) {
            return categoryIdIn(condition.categoryIds());
        }
        return null;
    }

    /**
     * 브랜드 필터 조건 (단일 or 다중).
     *
     * @param condition 검색 조건
     * @return BooleanExpression
     */
    public BooleanExpression brandFilter(LegacyProductGroupSearchCondition condition) {
        if (condition.brandId() != null) {
            return brandIdEq(condition.brandId());
        }
        if (condition.brandIds() != null && !condition.brandIds().isEmpty()) {
            return legacyProductGroupEntity.brandId.in(condition.brandIds());
        }
        return null;
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
     * 노출 상품 조건 (display_yn = 'Y').
     *
     * @return BooleanExpression
     */
    public BooleanExpression onSaleProduct() {
        return legacyProductGroupEntity.displayYn.eq(LegacyProductGroupEntity_Yn.Y);
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
     * RECOMMEND 커서 조건 (score 기반).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @param cursorScore 커서 스코어 값
     * @return BooleanExpression
     */
    public BooleanExpression recommendCursor(Long lastDomainId, Double cursorScore) {
        if (lastDomainId == null || cursorScore == null) {
            return null;
        }
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
     * @param cursorCount 커서 리뷰수 값
     * @return BooleanExpression
     */
    public BooleanExpression reviewCursor(Long lastDomainId, Long cursorCount) {
        if (lastDomainId == null || cursorCount == null) {
            return null;
        }
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
     * @param cursorRating 커서 평점 값
     * @return BooleanExpression
     */
    public BooleanExpression highRatingCursor(Long lastDomainId, Double cursorRating) {
        if (lastDomainId == null || cursorRating == null) {
            return null;
        }
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
     * HIGH_PRICE 커서 조건 (salePrice 내림차순 기반).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @param cursorPrice 커서 가격 값
     * @return BooleanExpression
     */
    public BooleanExpression highPriceCursor(Long lastDomainId, Integer cursorPrice) {
        if (lastDomainId == null || cursorPrice == null) {
            return null;
        }
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
     * LOW_PRICE 커서 조건 (salePrice 오름차순 기반).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @param cursorPrice 커서 가격 값
     * @return BooleanExpression
     */
    public BooleanExpression lowPriceCursor(Long lastDomainId, Integer cursorPrice) {
        if (lastDomainId == null || cursorPrice == null) {
            return null;
        }
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
     * HIGH_DISCOUNT 커서 조건 (discountRate 내림차순 기반).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @param cursorRate 커서 할인율 값
     * @return BooleanExpression
     */
    public BooleanExpression highDiscountCursor(Long lastDomainId, Integer cursorRate) {
        if (lastDomainId == null || cursorRate == null) {
            return null;
        }
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
     * LOW_DISCOUNT 커서 조건 (discountRate 오름차순 기반).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @param cursorRate 커서 할인율 값
     * @return BooleanExpression
     */
    public BooleanExpression lowDiscountCursor(Long lastDomainId, Integer cursorRate) {
        if (lastDomainId == null || cursorRate == null) {
            return null;
        }
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
     * RECENT 커서 조건 (insertDate 내림차순 기반).
     *
     * @param lastDomainId 마지막 상품그룹 ID
     * @param cursorDate 커서 날짜 값
     * @return BooleanExpression
     */
    public BooleanExpression recentCursor(Long lastDomainId, LocalDateTime cursorDate) {
        if (lastDomainId == null || cursorDate == null) {
            return null;
        }
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
     * ID 기반 커서 조건 (커서값 없는 기본 페이징).
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
    public BooleanExpression dynamicCursorCondition(LegacyProductGroupSearchCondition condition) {
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

    /** LegacyProductGroupEntity 내부에서 Yn enum을 참조하기 위한 inner helper. */
    private static final class LegacyProductGroupEntity_Yn {

        static final com.ryuqq.setof.storage.legacy.product.entity.LegacyProductGroupEntity.Yn Y =
                com.ryuqq.setof.storage.legacy.product.entity.LegacyProductGroupEntity.Yn.Y;

        private LegacyProductGroupEntity_Yn() {}
    }
}
