package com.ryuqq.setof.application.payment.dto.query;

import java.time.Instant;
import java.util.List;

/**
 * GetPaymentsQuery - 결제 목록 조회 Query DTO
 *
 * <p>결제 목록 조회에 필요한 조건을 담습니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public record GetPaymentsQuery(
        String memberId,
        List<String> statuses,
        Instant startDate,
        Instant endDate,
        String lastPaymentId,
        int pageSize) {

    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * Static Factory Method
     *
     * @param memberId 회원 ID
     * @param statuses 결제 상태 목록
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @param lastPaymentId 마지막 결제 ID (커서)
     * @param pageSize 페이지 크기
     * @return GetPaymentsQuery
     */
    public static GetPaymentsQuery of(
            String memberId,
            List<String> statuses,
            Instant startDate,
            Instant endDate,
            String lastPaymentId,
            Integer pageSize) {
        int size = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;
        return new GetPaymentsQuery(memberId, statuses, startDate, endDate, lastPaymentId, size);
    }

    /**
     * 커서 존재 여부
     *
     * @return 커서가 있으면 true
     */
    public boolean hasCursor() {
        return lastPaymentId != null && !lastPaymentId.isBlank();
    }
}
