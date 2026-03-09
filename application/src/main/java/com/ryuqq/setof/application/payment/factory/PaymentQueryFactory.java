package com.ryuqq.setof.application.payment.factory;

import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.application.payment.dto.query.PaymentSearchParams;
import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.DateRange;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.payment.query.PaymentSearchCriteria;
import com.ryuqq.setof.domain.payment.query.PaymentSortKey;
import org.springframework.stereotype.Component;

/**
 * PaymentQueryFactory - 결제 조회 Criteria 생성 Factory.
 *
 * <p>PaymentSearchParams(Application DTO) → PaymentSearchCriteria(Domain) 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentQueryFactory {

    private final CommonVoFactory commonVoFactory;

    public PaymentQueryFactory(CommonVoFactory commonVoFactory) {
        this.commonVoFactory = commonVoFactory;
    }

    /**
     * PaymentSearchParams를 PaymentSearchCriteria로 변환합니다.
     *
     * @param params 결제 검색 파라미터
     * @return 결제 검색 조건 (도메인)
     */
    public PaymentSearchCriteria createCriteria(PaymentSearchParams params) {
        CursorPageRequest<Long> cursorPageRequest =
                commonVoFactory.createCursorPageRequestAfterCursor(
                        params.lastPaymentId(), params.pageSize());

        CursorQueryContext<PaymentSortKey, Long> queryContext =
                commonVoFactory.createCursorQueryContext(
                        PaymentSortKey.defaultKey(), SortDirection.DESC, cursorPageRequest);

        DateRange dateRange = commonVoFactory.createDateRange(params.startDate(), params.endDate());

        return PaymentSearchCriteria.of(
                params.userId(), dateRange, params.orderStatuses(), queryContext);
    }
}
