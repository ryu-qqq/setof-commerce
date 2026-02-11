package com.ryuqq.setof.storage.legacy.seller.condition;

import static com.ryuqq.setof.storage.legacy.seller.entity.QLegacySellerEntity.legacySellerEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import com.ryuqq.setof.domain.seller.query.SellerSearchField;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacySellerConditionBuilder - 레거시 셀러 QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * <p>레거시 DB는 deletedAt, active 컬럼이 없으므로 해당 조건을 제공하지 않습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacySellerConditionBuilder {

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? legacySellerEntity.id.eq(id) : null;
    }

    /** ID 목록 포함 조건 */
    public BooleanExpression idIn(List<Long> ids) {
        return ids != null && !ids.isEmpty() ? legacySellerEntity.id.in(ids) : null;
    }

    /** ID 제외 조건 */
    public BooleanExpression idNe(Long excludeId) {
        return excludeId != null ? legacySellerEntity.id.ne(excludeId) : null;
    }

    /** 셀러명 일치 조건 */
    public BooleanExpression sellerNameEq(String sellerName) {
        return sellerName != null ? legacySellerEntity.sellerName.eq(sellerName) : null;
    }

    /** 셀러명 LIKE 조건 */
    public BooleanExpression sellerNameContains(String sellerName) {
        return sellerName != null && !sellerName.isBlank()
                ? legacySellerEntity.sellerName.containsIgnoreCase(sellerName)
                : null;
    }

    /**
     * 검색 필드 기반 검색 조건.
     *
     * <p>레거시 seller 테이블에는 displayName이 없으므로 sellerName으로 대체합니다. REGISTRATION_NUMBER, COMPANY_NAME,
     * REPRESENTATIVE_NAME은 seller_business_info 테이블에 있으므로 단일 테이블 쿼리에서는 지원하지 않습니다.
     *
     * @param searchField 검색 필드
     * @param searchWord 검색어
     * @return BooleanExpression
     */
    public BooleanExpression searchFieldContains(SellerSearchField searchField, String searchWord) {
        if (searchWord == null || searchWord.isBlank()) {
            return null;
        }
        if (searchField == null) {
            return legacySellerEntity.sellerName.containsIgnoreCase(searchWord);
        }
        return switch (searchField) {
            case SELLER_NAME -> legacySellerEntity.sellerName.containsIgnoreCase(searchWord);
            case REGISTRATION_NUMBER, COMPANY_NAME, REPRESENTATIVE_NAME -> null;
        };
    }

    /**
     * 검색 조건 (Criteria 기반).
     *
     * @param criteria 검색 조건
     * @return BooleanExpression
     */
    public BooleanExpression searchCondition(SellerSearchCriteria criteria) {
        if (!criteria.hasSearchCondition()) {
            return null;
        }
        return searchFieldContains(criteria.searchField(), criteria.searchWord());
    }
}
