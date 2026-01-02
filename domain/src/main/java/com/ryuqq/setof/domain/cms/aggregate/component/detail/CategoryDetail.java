package com.ryuqq.setof.domain.cms.aggregate.component.detail;

import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.cms.vo.BadgeType;
import com.ryuqq.setof.domain.cms.vo.ComponentType;
import com.ryuqq.setof.domain.cms.vo.ListType;
import com.ryuqq.setof.domain.cms.vo.OrderType;

/**
 * Category(카테고리 기반 상품) 컴포넌트 상세 정보
 *
 * @param categoryId 카테고리 ID
 * @param listType 리스트 표시 타입
 * @param orderType 정렬 타입
 * @param badgeType 배지 타입
 * @param showFilter 필터 표시 여부
 * @author development-team
 * @since 1.0.0
 */
public record CategoryDetail(
        CategoryId categoryId,
        ListType listType,
        OrderType orderType,
        BadgeType badgeType,
        boolean showFilter)
        implements ComponentDetail {

    /** Compact Constructor */
    public CategoryDetail {
        if (categoryId == null) {
            throw new IllegalArgumentException("카테고리 ID는 필수입니다");
        }
        if (listType == null) {
            listType = ListType.GRID;
        }
        if (orderType == null) {
            orderType = OrderType.LATEST;
        }
        if (badgeType == null) {
            badgeType = BadgeType.NONE;
        }
    }

    /**
     * 정적 팩토리 메서드
     *
     * @param categoryId 카테고리 ID
     * @param listType 리스트 타입
     * @param orderType 정렬 타입
     * @param badgeType 배지 타입
     * @param showFilter 필터 표시 여부
     * @return CategoryDetail 인스턴스
     */
    public static CategoryDetail of(
            CategoryId categoryId,
            ListType listType,
            OrderType orderType,
            BadgeType badgeType,
            boolean showFilter) {
        return new CategoryDetail(categoryId, listType, orderType, badgeType, showFilter);
    }

    /**
     * 기본 설정으로 생성
     *
     * @param categoryId 카테고리 ID
     * @return CategoryDetail 인스턴스
     */
    public static CategoryDetail withDefaults(CategoryId categoryId) {
        return new CategoryDetail(
                categoryId, ListType.GRID, OrderType.LATEST, BadgeType.NONE, false);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.CATEGORY;
    }
}
