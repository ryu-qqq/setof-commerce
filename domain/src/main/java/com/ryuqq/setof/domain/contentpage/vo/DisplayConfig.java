package com.ryuqq.setof.domain.contentpage.vo;

/**
 * DisplayConfig - 컴포넌트 표시 설정 VO.
 *
 * @param listType 목록 표시 방식
 * @param orderType 정렬 방식
 * @param badgeType 뱃지 타입
 * @param filterEnabled 필터 사용 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DisplayConfig(
        ListType listType, OrderType orderType, BadgeType badgeType, boolean filterEnabled) {}
