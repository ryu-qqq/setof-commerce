package com.ryuqq.setof.application.order.service.query;

import com.ryuqq.setof.application.common.response.SliceResponse;
import com.ryuqq.setof.application.order.assembler.OrderAssembler;
import com.ryuqq.setof.application.order.dto.query.GetAdminOrdersQuery;
import com.ryuqq.setof.application.order.dto.response.OrderResponse;
import com.ryuqq.setof.application.order.factory.query.OrderQueryFactory;
import com.ryuqq.setof.application.order.manager.query.OrderReadManager;
import com.ryuqq.setof.application.order.port.in.query.GetAdminOrdersUseCase;
import com.ryuqq.setof.domain.order.aggregate.Order;
import com.ryuqq.setof.domain.order.query.criteria.OrderSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Admin 주문 목록 조회 Service
 *
 * <p>Admin에서 주문 목록을 조회합니다
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>Query → Criteria 변환
 *   <li>ReadManager를 통한 조회 위임
 *   <li>Slice 페이징 처리
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class GetAdminOrdersService implements GetAdminOrdersUseCase {

    private final OrderQueryFactory orderQueryFactory;
    private final OrderReadManager orderReadManager;
    private final OrderAssembler orderAssembler;

    public GetAdminOrdersService(
            OrderQueryFactory orderQueryFactory,
            OrderReadManager orderReadManager,
            OrderAssembler orderAssembler) {
        this.orderQueryFactory = orderQueryFactory;
        this.orderReadManager = orderReadManager;
        this.orderAssembler = orderAssembler;
    }

    /**
     * Admin 주문 목록 조회
     *
     * <p>Slice 방식으로 limit + 1 조회하여 hasNext 판단
     *
     * @param query Admin 조회 조건
     * @return Slice 형태의 주문 목록
     */
    @Override
    public SliceResponse<OrderResponse> getOrders(GetAdminOrdersQuery query) {
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
