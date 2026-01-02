package com.ryuqq.setof.application.orderevent.service.query;

import com.ryuqq.setof.application.orderevent.assembler.OrderEventAssembler;
import com.ryuqq.setof.application.orderevent.dto.response.OrderTimelineResponse;
import com.ryuqq.setof.application.orderevent.port.in.query.GetOrderTimelineUseCase;
import com.ryuqq.setof.application.orderevent.port.out.query.OrderEventQueryPort;
import com.ryuqq.setof.domain.order.vo.OrderId;
import com.ryuqq.setof.domain.orderevent.aggregate.OrderEvent;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * GetOrderTimelineService - 주문 타임라인 조회 서비스
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
public class GetOrderTimelineService implements GetOrderTimelineUseCase {

    private final OrderEventQueryPort orderEventQueryPort;
    private final OrderEventAssembler orderEventAssembler;

    public GetOrderTimelineService(
            OrderEventQueryPort orderEventQueryPort, OrderEventAssembler orderEventAssembler) {
        this.orderEventQueryPort = orderEventQueryPort;
        this.orderEventAssembler = orderEventAssembler;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderTimelineResponse getTimeline(String orderId) {
        List<OrderEvent> events = orderEventQueryPort.findByOrderId(OrderId.fromString(orderId));
        return orderEventAssembler.toTimelineResponse(orderId, events);
    }
}
