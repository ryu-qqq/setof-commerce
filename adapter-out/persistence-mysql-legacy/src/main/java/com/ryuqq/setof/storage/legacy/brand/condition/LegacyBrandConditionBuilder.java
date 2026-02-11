package com.ryuqq.setof.storage.legacy.brand.condition;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import com.ryuqq.setof.domain.brand.query.BrandSearchField;
import com.ryuqq.setof.storage.legacy.common.Yn;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyBrandConditionBuilder - 레거시 브랜드 QueryDSL 조건 빌더.
 *
 * <p>PER-CND-001: ConditionBuilder는 @Component로 등록.
 *
 * <p>PER-CND-002: 각 조건은 BooleanExpression 반환.
 *
 * <p>PER-CND-003: null 입력 시 null 반환 (동적 쿼리 지원).
 *
 * <p>레거시 DB는 deletedAt 컬럼이 없으므로 notDeleted() 조건은 제공하지 않습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyBrandConditionBuilder {

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? legacyBrandEntity.id.eq(id) : null;
    }

    /** ID 목록 포함 조건 */
    public BooleanExpression idIn(List<Long> ids) {
        return ids != null && !ids.isEmpty() ? legacyBrandEntity.id.in(ids) : null;
    }

    /** 브랜드명 포함 조건 (레거시 호환용, searchFieldContains 사용 권장) */
    public BooleanExpression brandNameContains(String brandName) {
        return brandName != null && !brandName.isBlank()
                ? legacyBrandEntity.brandName.containsIgnoreCase(brandName)
                : null;
    }

    /**
     * 검색 필드 기반 검색 조건.
     *
     * @param searchField 검색 필드
     * @param searchWord 검색어
     * @return BooleanExpression
     */
    public BooleanExpression searchFieldContains(BrandSearchField searchField, String searchWord) {
        if (searchWord == null || searchWord.isBlank()) {
            return null;
        }
        if (searchField == null) {
            return legacyBrandEntity.brandName.containsIgnoreCase(searchWord);
        }
        return switch (searchField) {
            case BRAND_NAME -> legacyBrandEntity.brandName.containsIgnoreCase(searchWord);
            case DISPLAY_KOREAN_NAME ->
                    legacyBrandEntity.displayKoreanName.containsIgnoreCase(searchWord);
            case DISPLAY_ENGLISH_NAME ->
                    legacyBrandEntity.displayEnglishName.containsIgnoreCase(searchWord);
        };
    }

    /**
     * 검색 조건 (Criteria 기반).
     *
     * @param criteria 검색 조건
     * @return BooleanExpression
     */
    public BooleanExpression searchCondition(BrandSearchCriteria criteria) {
        if (!criteria.hasSearchCondition()) {
            return null;
        }
        return searchFieldContains(criteria.searchField(), criteria.searchWord());
    }

    /** 표시 여부 일치 조건 (Boolean → Yn 변환) */
    public BooleanExpression displayedEq(Boolean displayed) {
        if (displayed == null) {
            return null;
        }
        Yn yn = Yn.fromBoolean(displayed);
        return legacyBrandEntity.displayYn.eq(yn);
    }
}
