package com.ryuqq.setof.domain.legacy.payment.dto.query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * LegacyPaymentSearchCondition - 레거시 결제 검색 조건 DTO.
 *
 * <p>Repository 검색 파라미터용 DTO입니다.
 *
 * @param userId 사용자 ID (필수)
 * @param lastDomainId 커서 기반 페이징용 마지막 도메인 ID
 * @param startDate 조회 시작일
 * @param endDate 조회 종료일
 * @param orderStatusList 주문 상태 필터 목록 (String 형태)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyPaymentSearchCondition(
        long userId,
        Long lastDomainId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<String> orderStatusList) {

    /**
     * 사용자 ID로 조회하는 생성자.
     *
     * @param userId 사용자 ID
     * @return LegacyPaymentSearchCondition
     */
    public static LegacyPaymentSearchCondition ofUserId(long userId) {
        return new LegacyPaymentSearchCondition(userId, null, null, null, null);
    }

    /**
     * 전체 조건을 포함하는 생성자.
     *
     * @param userId 사용자 ID
     * @param lastDomainId 커서 기반 페이징용 마지막 도메인 ID
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @param orderStatusList 주문 상태 필터 목록 (String 형태)
     * @return LegacyPaymentSearchCondition
     */
    public static LegacyPaymentSearchCondition of(
            long userId,
            Long lastDomainId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<String> orderStatusList) {
        return new LegacyPaymentSearchCondition(
                userId, lastDomainId, startDate, endDate, orderStatusList);
    }

    /**
     * 기간 필터 존재 여부.
     *
     * @return 기간 필터가 있으면 true
     */
    public boolean hasDateRange() {
        return startDate != null && endDate != null;
    }

    /**
     * 주문 상태 필터 존재 여부.
     *
     * @return 주문 상태 필터가 있으면 true
     */
    public boolean hasOrderStatusFilter() {
        return orderStatusList != null && !orderStatusList.isEmpty();
    }
}
