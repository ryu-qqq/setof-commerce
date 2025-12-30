package com.ryuqq.setof.adapter.out.persistence.payment.condition;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * PaymentSearchCondition - Payment 검색 조건 DTO
 *
 * <p>Persistence Layer 전용 검색 조건 객체입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public record PaymentSearchCondition(
        String memberId,
        List<String> statuses,
        Instant startDate,
        Instant endDate,
        UUID lastPaymentId,
        int limit) {

    /**
     * Static Factory Method
     *
     * @param memberId 회원 ID
     * @param statuses 결제 상태 목록
     * @param startDate 시작일
     * @param endDate 종료일
     * @param lastPaymentId 마지막 결제 ID (커서)
     * @param limit 조회 개수
     * @return PaymentSearchCondition
     */
    public static PaymentSearchCondition of(
            String memberId,
            List<String> statuses,
            Instant startDate,
            Instant endDate,
            UUID lastPaymentId,
            int limit) {
        return new PaymentSearchCondition(
                memberId, statuses, startDate, endDate, lastPaymentId, limit);
    }

    /**
     * 상태 필터 존재 여부
     *
     * @return 상태 필터가 있으면 true
     */
    public boolean hasStatuses() {
        return statuses != null && !statuses.isEmpty();
    }

    /**
     * 시작일 필터 존재 여부
     *
     * @return 시작일이 있으면 true
     */
    public boolean hasStartDate() {
        return startDate != null;
    }

    /**
     * 종료일 필터 존재 여부
     *
     * @return 종료일이 있으면 true
     */
    public boolean hasEndDate() {
        return endDate != null;
    }

    /**
     * 커서 존재 여부
     *
     * @return 커서가 있으면 true
     */
    public boolean hasCursor() {
        return lastPaymentId != null;
    }
}
