package com.ryuqq.setof.adapter.out.persistence.seller.condition;

import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerJpaEntity.sellerJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import com.ryuqq.setof.domain.seller.query.SellerSearchField;
import org.springframework.stereotype.Component;

/**
 * SellerConditionBuilder - 셀러 QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerConditionBuilder {

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? sellerJpaEntity.id.eq(id) : null;
    }

    /** ID 불일치 조건 */
    public BooleanExpression idNe(Long id) {
        return id != null ? sellerJpaEntity.id.ne(id) : null;
    }

    /** 셀러명 일치 조건 */
    public BooleanExpression sellerNameEq(String sellerName) {
        return sellerName != null && !sellerName.isBlank()
                ? sellerJpaEntity.sellerName.eq(sellerName)
                : null;
    }

    /** 셀러명 포함 조건 */
    public BooleanExpression sellerNameContains(String sellerName) {
        return sellerName != null && !sellerName.isBlank()
                ? sellerJpaEntity.sellerName.containsIgnoreCase(sellerName)
                : null;
    }

    /** 활성화 상태 조건 */
    public BooleanExpression activeEq(Boolean active) {
        return active != null ? sellerJpaEntity.active.eq(active) : null;
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return sellerJpaEntity.deletedAt.isNull();
    }

    /**
     * 검색 필드 기반 검색 조건.
     *
     * <p>REGISTRATION_NUMBER, COMPANY_NAME, REPRESENTATIVE_NAME은 BusinessInfo 테이블에 있으므로 null 반환.
     * (Composite 쿼리에서 처리)
     */
    public BooleanExpression searchFieldContains(SellerSearchField searchField, String searchWord) {
        if (searchWord == null || searchWord.isBlank()) {
            return null;
        }
        if (searchField == null) {
            return sellerJpaEntity.sellerName.containsIgnoreCase(searchWord);
        }
        return switch (searchField) {
            case SELLER_NAME -> sellerJpaEntity.sellerName.containsIgnoreCase(searchWord);
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
