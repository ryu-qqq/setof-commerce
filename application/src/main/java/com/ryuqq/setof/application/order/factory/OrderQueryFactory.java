package com.ryuqq.setof.application.order.factory;

import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.application.order.dto.query.OrderSearchParams;
import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.DateRange;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.order.query.OrderSearchCriteria;
import com.ryuqq.setof.domain.order.query.OrderSortKey;
import org.springframework.stereotype.Component;

/**
 * OrderQueryFactory - 주문 조회 Criteria 생성 Factory.
 *
 * <p>OrderSearchParams(Application DTO) → OrderSearchCriteria(Domain) 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class OrderQueryFactory {

    private final CommonVoFactory commonVoFactory;

    public OrderQueryFactory(CommonVoFactory commonVoFactory) {
        this.commonVoFactory = commonVoFactory;
    }

    public OrderSearchCriteria createCriteria(OrderSearchParams params) {
        CursorPageRequest<Long> cursorPageRequest =
                commonVoFactory.createCursorPageRequestAfterCursor(
                        params.lastOrderId(), params.pageSize());

        CursorQueryContext<OrderSortKey, Long> queryContext =
                commonVoFactory.createCursorQueryContext(
                        OrderSortKey.defaultKey(), SortDirection.DESC, cursorPageRequest);

        DateRange dateRange = commonVoFactory.createDateRange(params.startDate(), params.endDate());

        return OrderSearchCriteria.of(
                params.userId(), dateRange, params.orderStatuses(), queryContext);
    }
}
