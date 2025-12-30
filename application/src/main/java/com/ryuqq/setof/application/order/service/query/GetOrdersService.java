package com.ryuqq.setof.application.order.service.query;

import com.ryuqq.setof.application.common.response.SliceResponse;
import com.ryuqq.setof.application.order.assembler.OrderAssembler;
import com.ryuqq.setof.application.order.dto.query.GetOrdersQuery;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.order.factory.query.OrderQueryFactory;
import com.ryuqq.setof.application.order.manager.query.OrderReadManager;
import com.ryuqq.setof.application.order.port.in.query.GetOrdersUseCase;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.query.criteria.OrderSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * GetOrdersService - 주문 목록 조회 서비스
 *
 * <p>고객의 주문 목록을 커서 기반 페이지네이션으로 조회합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>주문 목록 조회 비즈니스 로직
 *   <li>커서 기반 페이징 처리
 *   <li>Domain → Response 변환
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class GetOrdersService implements GetOrdersUseCase {

    private final OrderQueryFactory orderQueryFactory;
    private final OrderReadManager orderReadManager;
    private final OrderAssembler orderAssembler;

    public GetOrdersService(
            OrderQueryFactory orderQueryFactory,
            OrderReadManager orderReadManager,
            OrderAssembler orderAssembler) {
        this.orderQueryFactory = orderQueryFactory;
        this.orderReadManager = orderReadManager;
        this.orderAssembler = orderAssembler;
    }

    /**
     * 주문 목록 조회
     *
     * <p>Slice 방식으로 pageSize + 1 조회하여 hasNext 판단
     *
     * @param query 조회 조건 (memberId, orderStatuses, 기간, 페이징)
     * @return Slice 형태의 주문 목록
     */
    @Override
    public SliceResponse<OrderResponse> getOrders(GetOrdersQuery query) {
        OrderSearchCriteria criteria = orderQueryFactory.create(query);
        List<Order> orders = orderReadManager.findByCriteria(criteria);

        boolean hasNext = orders.size() > query.pageSize();
        List<Order> content = hasNext ? orders.subList(0, query.pageSize()) : orders;

        List<OrderResponse> responses = content.stream().map(orderAssembler::toResponse).toList();

        String nextCursor = null;
        if (hasNext && !content.isEmpty()) {
            Order lastOrder = content.get(content.size() - 1);
            nextCursor = lastOrder.id().value().toString();
        }

        return SliceResponse.of(responses, query.pageSize(), hasNext, nextCursor);
    }
}
