package com.ryuqq.setof.storage.legacy.category.condition;

import static com.ryuqq.setof.storage.legacy.category.entity.QLegacyCategoryEntity.legacyCategoryEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import com.ryuqq.setof.domain.category.query.CategorySearchField;
import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import com.ryuqq.setof.storage.legacy.common.Yn;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyCategoryConditionBuilder - 레거시 카테고리 QueryDSL 조건 빌더.
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
public class LegacyCategoryConditionBuilder {

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? legacyCategoryEntity.id.eq(id) : null;
    }

    /** ID 목록 포함 조건 */
    public BooleanExpression idIn(List<Long> ids) {
        return ids != null && !ids.isEmpty() ? legacyCategoryEntity.id.in(ids) : null;
    }

    /** 부모 카테고리 ID 일치 조건 */
    public BooleanExpression parentCategoryIdEq(CategoryId parentCategoryId) {
        return parentCategoryId != null
                ? legacyCategoryEntity.parentCategoryId.eq(parentCategoryId.value())
                : null;
    }

    /** 표시 여부 일치 조건 (Boolean → Yn 변환) */
    public BooleanExpression displayedEq(Boolean displayed) {
        if (displayed == null) {
            return null;
        }
        Yn yn = Yn.fromBoolean(displayed);
        return legacyCategoryEntity.displayYn.eq(yn);
    }

    /** 타겟 그룹 일치 조건 */
    public BooleanExpression targetGroupEq(TargetGroup targetGroup) {
        return targetGroup != null ? legacyCategoryEntity.targetGroup.eq(targetGroup) : null;
    }

    /** 카테고리 타입 일치 조건 */
    public BooleanExpression categoryTypeEq(CategoryType categoryType) {
        return categoryType != null ? legacyCategoryEntity.categoryType.eq(categoryType) : null;
    }

    /** 카테고리명 포함 조건 */
    public BooleanExpression categoryNameContains(String categoryName) {
        return categoryName != null && !categoryName.isBlank()
                ? legacyCategoryEntity.categoryName.containsIgnoreCase(categoryName)
                : null;
    }

    /**
     * 검색 필드 기반 검색 조건.
     *
     * @param searchField 검색 필드
     * @param searchWord 검색어
     * @return BooleanExpression
     */
    public BooleanExpression searchFieldContains(
            CategorySearchField searchField, String searchWord) {
        if (searchWord == null || searchWord.isBlank()) {
            return null;
        }
        if (searchField == null) {
            return legacyCategoryEntity
                    .categoryName
                    .containsIgnoreCase(searchWord)
                    .or(legacyCategoryEntity.displayName.containsIgnoreCase(searchWord));
        }
        return switch (searchField) {
            case CATEGORY_NAME -> legacyCategoryEntity.categoryName.containsIgnoreCase(searchWord);
            case DISPLAY_NAME -> legacyCategoryEntity.displayName.containsIgnoreCase(searchWord);
        };
    }

    /**
     * 검색 조건 (Criteria 기반).
     *
     * @param criteria 검색 조건
     * @return BooleanExpression
     */
    public BooleanExpression searchCondition(CategorySearchCriteria criteria) {
        if (!criteria.hasSearchCondition()) {
            return null;
        }
        return searchFieldContains(criteria.searchField(), criteria.searchWord());
    }

    /** 경로 시작 조건 (하위 카테고리 조회용) */
    public BooleanExpression pathStartsWith(String path) {
        return path != null && !path.isBlank() ? legacyCategoryEntity.path.startsWith(path) : null;
    }
}
