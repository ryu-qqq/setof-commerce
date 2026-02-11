package com.ryuqq.setof.storage.legacy.composite.web.product.condition;

import static com.ryuqq.setof.storage.legacy.product.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.product.entity.LegacyProductGroupEntity;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebProductGroupCompositeConditionBuilder - 레거시 웹 상품그룹 Composite QueryDSL 조건 빌더.
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

    // ===== ID 조건 =====

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
     * 상품그룹 ID 목록 포함 조건.
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return BooleanExpression
     */
    public BooleanExpression productGroupIdIn(List<Long> productGroupIds) {
        return productGroupIds != null && !productGroupIds.isEmpty()
                ? legacyProductGroupEntity.id.in(productGroupIds)
                : null;
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
     * 브랜드 ID 일치 조건.
     *
     * @param brandId 브랜드 ID
     * @return BooleanExpression
     */
    public BooleanExpression brandIdEq(Long brandId) {
        return brandId != null ? legacyProductGroupEntity.brandId.eq(brandId) : null;
    }

    /**
     * 브랜드 ID 목록 포함 조건.
     *
     * @param brandIds 브랜드 ID 목록
     * @return BooleanExpression
     */
    public BooleanExpression brandIdIn(List<Long> brandIds) {
        return brandIds != null && !brandIds.isEmpty()
                ? legacyProductGroupEntity.brandId.in(brandIds)
                : null;
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
     * 카테고리 ID 목록 포함 조건.
     *
     * @param categoryIds 카테고리 ID 목록
     * @return BooleanExpression
     */
    public BooleanExpression categoryIdIn(List<Long> categoryIds) {
        return categoryIds != null && !categoryIds.isEmpty()
                ? legacyProductGroupEntity.categoryId.in(categoryIds)
                : null;
    }

    // ===== 가격 조건 =====

    /**
     * 가격 범위 조건.
     *
     * @param lowestPrice 최저가
     * @param highestPrice 최고가
     * @return BooleanExpression
     */
    public BooleanExpression priceBetween(Long lowestPrice, Long highestPrice) {
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

    // ===== 상태 조건 =====

    /**
     * 판매 중인 상품 조건 (displayYn = 'Y').
     *
     * @return BooleanExpression
     */
    public BooleanExpression onSale() {
        return legacyProductGroupEntity.displayYn.eq(LegacyProductGroupEntity.Yn.Y);
    }

    /**
     * 삭제되지 않은 상품 조건 (deleteYn = 'N').
     *
     * @return BooleanExpression
     */
    public BooleanExpression notDeleted() {
        return legacyProductGroupEntity.deleteYn.eq(LegacyProductGroupEntity.Yn.N);
    }

    /**
     * 활성 상품 조건 (displayYn = 'Y' AND deleteYn = 'N').
     *
     * @return BooleanExpression
     */
    public BooleanExpression active() {
        return onSale().and(notDeleted());
    }

    // ===== 커서 페이징 조건 =====

    /**
     * 커서 조건 (ID 기준 내림차순).
     *
     * @param lastDomainId 마지막 ID
     * @return BooleanExpression
     */
    public BooleanExpression cursorIdLt(Long lastDomainId) {
        return lastDomainId != null ? legacyProductGroupEntity.id.lt(lastDomainId) : null;
    }

    /**
     * 커서 조건 (ID 기준 오름차순).
     *
     * @param lastDomainId 마지막 ID
     * @return BooleanExpression
     */
    public BooleanExpression cursorIdGt(Long lastDomainId) {
        return lastDomainId != null ? legacyProductGroupEntity.id.gt(lastDomainId) : null;
    }
}
