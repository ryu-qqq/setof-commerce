package com.ryuqq.setof.domain.legacy.order.dto.query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * LegacyOrderSearchCondition - 레거시 주문 검색 조건 DTO.
 *
 * <p>Repository 검색 파라미터용 DTO입니다.
 *
 * @param userId 사용자 ID (필수)
 * @param lastDomainId 커서 기반 페이징용 마지막 주문 ID
 * @param startDate 검색 시작일
 * @param endDate 검색 종료일
 * @param orderStatusList 주문 상태 필터 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyOrderSearchCondition(
        long userId,
        Long lastDomainId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<String> orderStatusList) {

    /**
     * 전체 조회용 빈 조건 (사용자 ID만 필수).
     *
     * @param userId 사용자 ID
     * @return LegacyOrderSearchCondition
     */
    public static LegacyOrderSearchCondition empty(long userId) {
        return new LegacyOrderSearchCondition(userId, null, null, null, null);
    }

    /**
     * 커서 기반 페이징 조건.
     *
     * @param userId 사용자 ID
     * @param lastDomainId 마지막 주문 ID
     * @return LegacyOrderSearchCondition
     */
    public static LegacyOrderSearchCondition ofCursor(long userId, Long lastDomainId) {
        return new LegacyOrderSearchCondition(userId, lastDomainId, null, null, null);
    }

    /**
     * 날짜 범위 조건.
     *
     * @param userId 사용자 ID
     * @param startDate 시작일
     * @param endDate 종료일
     * @return LegacyOrderSearchCondition
     */
    public static LegacyOrderSearchCondition ofDateRange(
            long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return new LegacyOrderSearchCondition(userId, null, startDate, endDate, null);
    }

    /**
     * 상태 필터 조건.
     *
     * @param userId 사용자 ID
     * @param orderStatusList 주문 상태 목록
     * @return LegacyOrderSearchCondition
     */
    public static LegacyOrderSearchCondition ofStatus(long userId, List<String> orderStatusList) {
        return new LegacyOrderSearchCondition(userId, null, null, null, orderStatusList);
    }

    /**
     * 커서 기반 페이징 여부.
     *
     * @return lastDomainId가 있으면 true
     */
    public boolean hasCursor() {
        return lastDomainId != null;
    }

    /**
     * 날짜 범위 존재 여부.
     *
     * @return startDate와 endDate가 모두 있으면 true
     */
    public boolean hasDateRange() {
        return startDate != null && endDate != null;
    }

    /**
     * 상태 필터 존재 여부.
     *
     * @return orderStatusList가 비어있지 않으면 true
     */
    public boolean hasStatusFilter() {
        return orderStatusList != null && !orderStatusList.isEmpty();
    }
}
