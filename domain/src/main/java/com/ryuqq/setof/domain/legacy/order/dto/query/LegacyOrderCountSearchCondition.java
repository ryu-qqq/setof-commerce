package com.ryuqq.setof.domain.legacy.order.dto.query;

import java.util.List;

/**
 * LegacyOrderCountSearchCondition - 레거시 주문 상태별 건수 검색 조건 DTO.
 *
 * <p>마이페이지 주문 현황 뱃지 표시 등에 사용됩니다.
 *
 * @param userId 사용자 ID
 * @param orderStatuses 조회할 주문 상태 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyOrderCountSearchCondition(Long userId, List<String> orderStatuses) {

    /**
     * 사용자 ID와 상태 목록으로 조건 생성.
     *
     * @param userId 사용자 ID
     * @param orderStatuses 주문 상태 목록
     * @return LegacyOrderCountSearchCondition
     */
    public static LegacyOrderCountSearchCondition of(Long userId, List<String> orderStatuses) {
        return new LegacyOrderCountSearchCondition(userId, orderStatuses);
    }

    /**
     * 사용자 ID로만 조건 생성.
     *
     * @param userId 사용자 ID
     * @return LegacyOrderCountSearchCondition
     */
    public static LegacyOrderCountSearchCondition ofUserId(Long userId) {
        return new LegacyOrderCountSearchCondition(userId, null);
    }

    /**
     * 주문 상태 필터 존재 여부.
     *
     * @return 주문 상태가 있으면 true
     */
    public boolean hasOrderStatuses() {
        return orderStatuses != null && !orderStatuses.isEmpty();
    }
}
