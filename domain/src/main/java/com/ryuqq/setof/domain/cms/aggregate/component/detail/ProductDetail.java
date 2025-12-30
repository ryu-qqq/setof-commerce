package com.ryuqq.setof.domain.cms.aggregate.component.detail;

import com.ryuqq.setof.domain.cms.vo.BadgeType;
import com.ryuqq.setof.domain.cms.vo.ComponentType;
import com.ryuqq.setof.domain.cms.vo.ListType;
import com.ryuqq.setof.domain.cms.vo.OrderType;

/**
 * Product(상품 리스트) 컴포넌트 상세 정보
 *
 * @param listType 리스트 표시 타입
 * @param orderType 정렬 타입
 * @param badgeType 배지 타입
 * @param showFilter 필터 표시 여부
 * @author development-team
 * @since 1.0.0
 */
public record ProductDetail(
        ListType listType, OrderType orderType, BadgeType badgeType, boolean showFilter)
        implements ComponentDetail {

    /** Compact Constructor */
    public ProductDetail {
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
     * @param listType 리스트 타입
     * @param orderType 정렬 타입
     * @param badgeType 배지 타입
     * @param showFilter 필터 표시 여부
     * @return ProductDetail 인스턴스
     */
    public static ProductDetail of(
            ListType listType, OrderType orderType, BadgeType badgeType, boolean showFilter) {
        return new ProductDetail(listType, orderType, badgeType, showFilter);
    }

    /**
     * 기본 설정으로 생성
     *
     * @return ProductDetail 인스턴스
     */
    public static ProductDetail defaultSettings() {
        return new ProductDetail(ListType.GRID, OrderType.LATEST, BadgeType.NONE, false);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.PRODUCT;
    }
}
