package com.ryuqq.setof.application.order.service.query;

import com.ryuqq.setof.application.order.assembler.OrderAssembler;
import com.ryuqq.setof.application.order.dto.query.OrderSearchParams;
import com.ryuqq.setof.application.order.dto.response.OrderSliceResult;
import com.ryuqq.setof.application.order.factory.OrderQueryFactory;
import com.ryuqq.setof.application.order.internal.OrderReadFacade;
import com.ryuqq.setof.application.order.port.in.query.GetOrdersUseCase;
import com.ryuqq.setof.domain.order.query.OrderSearchCriteria;
import com.ryuqq.setof.domain.order.vo.OrderDetail;
import com.ryuqq.setof.domain.order.vo.OrderStatusCount;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetOrdersService - 주문 목록 조회 서비스.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션.
 *
 * <p>APP-SVC-003: 1 UseCase = 1 Service.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetOrdersService implements GetOrdersUseCase {

    private final OrderQueryFactory orderQueryFactory;
    private final OrderReadFacade orderReadFacade;
    private final OrderAssembler orderAssembler;

    public GetOrdersService(
            OrderQueryFactory orderQueryFactory,
            OrderReadFacade orderReadFacade,
            OrderAssembler orderAssembler) {
        this.orderQueryFactory = orderQueryFactory;
        this.orderReadFacade = orderReadFacade;
        this.orderAssembler = orderAssembler;
    }

    @Override
    public OrderSliceResult execute(OrderSearchParams params) {
        OrderSearchCriteria criteria = orderQueryFactory.createCriteria(params);

        List<Long> orderIds = orderReadFacade.fetchOrderIds(criteria);

        List<Long> pageOrderIds =
                orderIds.size() > criteria.size() ? orderIds.subList(0, criteria.size()) : orderIds;

        List<OrderDetail> details = orderReadFacade.fetchOrderDetails(pageOrderIds);

        List<String> countStatuses =
                criteria.orderStatuses() != null ? criteria.orderStatuses() : List.of();
        List<OrderStatusCount> statusCounts =
                orderReadFacade.countByStatus(params.userId(), countStatuses);

        return orderAssembler.toSliceResult(details, statusCounts, criteria, orderIds);
    }
}
