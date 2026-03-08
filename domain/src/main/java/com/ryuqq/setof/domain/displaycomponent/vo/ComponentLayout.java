package com.ryuqq.setof.domain.displaycomponent.vo;

/**
 * ComponentLayout - 컴포넌트 레이아웃 설정 VO.
 *
 * <p>컴포넌트의 타입, 리스트 형식, 정렬 방식, 배지 타입, 필터 여부를 정의한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ComponentLayout(
        ComponentType componentType,
        ListType listType,
        OrderType orderType,
        BadgeType badgeType,
        boolean filterEnabled) {

    public boolean isProductRelated() {
        return componentType.isProductRelated();
    }
}
