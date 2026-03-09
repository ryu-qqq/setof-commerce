package com.ryuqq.setof.domain.payment.query;

import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.DateRange;
import java.util.List;

/**
 * 결제 커서 기반 검색 조건.
 *
 * <p>userId + 날짜 범위 + 주문 상태 필터 + 커서 페이징을 포함합니다.
 *
 * @param userId 사용자 ID (필수)
 * @param dateRange 검색 날짜 범위
 * @param orderStatuses 주문 상태 필터 목록
 * @param queryContext 정렬 + 커서 페이징 컨텍스트
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PaymentSearchCriteria(
        long userId,
        DateRange dateRange,
        List<String> orderStatuses,
        CursorQueryContext<PaymentSortKey, Long> queryContext) {

    public PaymentSearchCriteria {
        if (queryContext == null) {
            queryContext = CursorQueryContext.defaultOf(PaymentSortKey.defaultKey());
        }
        if (orderStatuses != null && orderStatuses.isEmpty()) {
            orderStatuses = null;
        }
    }

    public static PaymentSearchCriteria of(
            long userId,
            DateRange dateRange,
            List<String> orderStatuses,
            CursorQueryContext<PaymentSortKey, Long> queryContext) {
        return new PaymentSearchCriteria(userId, dateRange, orderStatuses, queryContext);
    }

    public int size() {
        return queryContext.size();
    }

    public int fetchSize() {
        return queryContext.fetchSize();
    }

    public Long cursor() {
        return queryContext.cursor();
    }

    public boolean hasCursor() {
        return queryContext.hasCursor();
    }

    public boolean hasDateRange() {
        return dateRange != null && !dateRange.isEmpty();
    }

    public boolean hasStatusFilter() {
        return orderStatuses != null && !orderStatuses.isEmpty();
    }
}
