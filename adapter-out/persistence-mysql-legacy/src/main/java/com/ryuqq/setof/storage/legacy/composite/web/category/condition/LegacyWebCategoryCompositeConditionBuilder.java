package com.ryuqq.setof.storage.legacy.composite.web.category.condition;

import static com.ryuqq.setof.storage.legacy.category.entity.QLegacyCategoryEntity.legacyCategoryEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.ryuqq.setof.storage.legacy.category.entity.LegacyCategoryEntity;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * LegacyWebCategoryCompositeConditionBuilder - 레거시 Web 카테고리 Composite QueryDSL 조건 빌더.
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
public class LegacyWebCategoryCompositeConditionBuilder {

    /** 비노출 카테고리 제외 ID. 레거시 시스템의 하드코딩된 제외 조건. */
    private static final long EXCLUDED_CATEGORY_ID = 1828L;

    // ===== ID 조건 =====

    /**
     * 카테고리 ID 일치 조건.
     *
     * @param categoryId 카테고리 ID
     * @return BooleanExpression
     */
    public BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? legacyCategoryEntity.id.eq(categoryId) : null;
    }

    /**
     * 카테고리 ID 목록 포함 조건.
     *
     * @param categoryIds 카테고리 ID 목록
     * @return BooleanExpression
     */
    public BooleanExpression categoryIdIn(Set<Long> categoryIds) {
        return categoryIds != null && !categoryIds.isEmpty()
                ? legacyCategoryEntity.id.in(categoryIds)
                : null;
    }

    /**
     * 카테고리 ID 목록 포함 조건 (List 버전).
     *
     * @param categoryIds 카테고리 ID 목록
     * @return BooleanExpression
     */
    public BooleanExpression categoryIdIn(List<Long> categoryIds) {
        return categoryIds != null && !categoryIds.isEmpty()
                ? legacyCategoryEntity.id.in(categoryIds)
                : null;
    }

    // ===== No-Offset 페이징 조건 =====

    /**
     * No-Offset 페이징 조건 (ASC 정렬 시 id > lastId).
     *
     * @param lastCategoryId 마지막 카테고리 ID
     * @return BooleanExpression
     */
    public BooleanExpression categoryIdGt(Long lastCategoryId) {
        return lastCategoryId != null ? legacyCategoryEntity.id.gt(lastCategoryId) : null;
    }

    /**
     * No-Offset 페이징 조건 (DESC 정렬 시 id < lastId).
     *
     * @param lastCategoryId 마지막 카테고리 ID
     * @return BooleanExpression
     */
    public BooleanExpression categoryIdLt(Long lastCategoryId) {
        return lastCategoryId != null ? legacyCategoryEntity.id.lt(lastCategoryId) : null;
    }

    /**
     * No-Offset 페이징 조건 (정렬 방향에 따라 자동 선택).
     *
     * @param lastCategoryId 마지막 카테고리 ID
     * @param pageable Pageable (정렬 정보 포함)
     * @return BooleanExpression
     */
    public BooleanExpression categoryIdCondition(Long lastCategoryId, Pageable pageable) {
        if (lastCategoryId == null) {
            return null;
        }
        Sort.Order order = pageable.getSort().getOrderFor("id");
        if (order != null && order.isDescending()) {
            return categoryIdLt(lastCategoryId);
        }
        return categoryIdGt(lastCategoryId);
    }

    // ===== 검색 조건 =====

    /**
     * 카테고리명 LIKE 검색 조건 (displayName 기준).
     *
     * @param categoryName 검색어
     * @return BooleanExpression
     */
    public BooleanExpression displayNameLike(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            return null;
        }
        return legacyCategoryEntity.displayName.contains(categoryName);
    }

    /**
     * 카테고리 depth 일치 조건.
     *
     * @param categoryDepth 카테고리 depth
     * @return BooleanExpression
     */
    public BooleanExpression categoryDepthEq(Integer categoryDepth) {
        return categoryDepth != null ? legacyCategoryEntity.categoryDepth.eq(categoryDepth) : null;
    }

    /**
     * 부모 카테고리 ID 일치 조건.
     *
     * @param parentCategoryId 부모 카테고리 ID
     * @return BooleanExpression
     */
    public BooleanExpression parentCategoryIdEq(Long parentCategoryId) {
        return parentCategoryId != null
                ? legacyCategoryEntity.parentCategoryId.eq(parentCategoryId)
                : null;
    }

    // ===== 상태 조건 =====

    /**
     * 비노출 카테고리 제외 조건. 레거시 시스템의 id NOT IN (1828) 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression excludeNonDisplayCategory() {
        return legacyCategoryEntity.id.ne(EXCLUDED_CATEGORY_ID);
    }

    /**
     * 표시 중인 카테고리 조건.
     *
     * @return BooleanExpression
     */
    public BooleanExpression displayed() {
        return legacyCategoryEntity.displayYn.eq(LegacyCategoryEntity.Yn.Y);
    }

    /**
     * 대상 그룹 일치 조건.
     *
     * @param targetGroup 대상 그룹
     * @return BooleanExpression
     */
    public BooleanExpression targetGroupEq(LegacyCategoryEntity.TargetGroup targetGroup) {
        return targetGroup != null ? legacyCategoryEntity.targetGroup.eq(targetGroup) : null;
    }

    /**
     * 대상 그룹 일치 조건 (문자열 버전).
     *
     * @param targetGroup 대상 그룹 문자열
     * @return BooleanExpression
     */
    public BooleanExpression targetGroupEq(String targetGroup) {
        if (targetGroup == null || targetGroup.isBlank()) {
            return null;
        }
        try {
            LegacyCategoryEntity.TargetGroup group =
                    LegacyCategoryEntity.TargetGroup.valueOf(targetGroup);
            return legacyCategoryEntity.targetGroup.eq(group);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // ===== 정렬 조건 =====

    /**
     * ID 기준 정렬 OrderSpecifier 생성.
     *
     * @param pageable Pageable (정렬 정보 포함)
     * @return OrderSpecifier
     */
    public OrderSpecifier<Long> getOrderSpecifier(Pageable pageable) {
        Sort.Order order = pageable.getSort().getOrderFor("id");
        if (order != null && order.isDescending()) {
            return legacyCategoryEntity.id.desc();
        }
        return legacyCategoryEntity.id.asc();
    }

    /**
     * Depth 기준 오름차순 정렬.
     *
     * @return OrderSpecifier
     */
    public OrderSpecifier<Integer> orderByDepthAsc() {
        return legacyCategoryEntity.categoryDepth.asc();
    }

    /**
     * Depth 기준 내림차순 정렬.
     *
     * @return OrderSpecifier
     */
    public OrderSpecifier<Integer> orderByDepthDesc() {
        return legacyCategoryEntity.categoryDepth.desc();
    }
}
