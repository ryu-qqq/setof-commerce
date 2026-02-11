package com.ryuqq.setof.adapter.out.persistence.category.condition;

import static com.ryuqq.setof.adapter.out.persistence.category.entity.QCategoryJpaEntity.categoryJpaEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import com.ryuqq.setof.domain.category.query.CategorySearchField;
import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * CategoryConditionBuilder - 카테고리 QueryDSL 조건 빌더.
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
public class CategoryConditionBuilder {

    /** ID 일치 조건 */
    public BooleanExpression idEq(Long id) {
        return id != null ? categoryJpaEntity.id.eq(id) : null;
    }

    /** ID 목록 포함 조건 */
    public BooleanExpression idIn(List<Long> ids) {
        return ids != null && !ids.isEmpty() ? categoryJpaEntity.id.in(ids) : null;
    }

    /** 카테고리명 포함 조건 */
    public BooleanExpression categoryNameContains(String categoryName) {
        return categoryName != null && !categoryName.isBlank()
                ? categoryJpaEntity.categoryName.containsIgnoreCase(categoryName)
                : null;
    }

    /** 부모 카테고리 ID 일치 조건 */
    public BooleanExpression parentCategoryIdEq(Long parentCategoryId) {
        return parentCategoryId != null
                ? categoryJpaEntity.parentCategoryId.eq(parentCategoryId)
                : null;
    }

    /** 카테고리 깊이 일치 조건 */
    public BooleanExpression categoryDepthEq(Integer categoryDepth) {
        return categoryDepth != null ? categoryJpaEntity.categoryDepth.eq(categoryDepth) : null;
    }

    /** 표시 여부 일치 조건 */
    public BooleanExpression displayedEq(Boolean displayed) {
        return displayed != null ? categoryJpaEntity.displayed.eq(displayed) : null;
    }

    /** 타겟 그룹 일치 조건 */
    public BooleanExpression targetGroupEq(TargetGroup targetGroup) {
        return targetGroup != null ? categoryJpaEntity.targetGroup.eq(targetGroup) : null;
    }

    /** 카테고리 타입 일치 조건 */
    public BooleanExpression categoryTypeEq(CategoryType categoryType) {
        return categoryType != null ? categoryJpaEntity.categoryType.eq(categoryType) : null;
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
            return categoryJpaEntity
                    .categoryName
                    .containsIgnoreCase(searchWord)
                    .or(categoryJpaEntity.displayName.containsIgnoreCase(searchWord));
        }
        return switch (searchField) {
            case CATEGORY_NAME -> categoryJpaEntity.categoryName.containsIgnoreCase(searchWord);
            case DISPLAY_NAME -> categoryJpaEntity.displayName.containsIgnoreCase(searchWord);
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

    /** 경로 시작 조건 (하위 카테고리 조회 용) */
    public BooleanExpression pathStartsWith(String pathPrefix) {
        return pathPrefix != null && !pathPrefix.isBlank()
                ? categoryJpaEntity.path.startsWith(pathPrefix)
                : null;
    }

    /** 삭제되지 않은 조건 */
    public BooleanExpression notDeleted() {
        return categoryJpaEntity.deletedAt.isNull();
    }
}
