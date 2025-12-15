package com.setof.connectly.module.order.service.query.update.strategy;

import com.setof.connectly.module.exception.order.ExpireReturnRequestException;
import com.setof.connectly.module.exception.order.InvalidOrderStatusException;
import com.setof.connectly.module.order.dto.fetch.OrderHistoryResponse;
import com.setof.connectly.module.order.dto.fetch.UpdateOrderResponse;
import com.setof.connectly.module.order.dto.query.ClaimOrder;
import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.order.mapper.OrderMapper;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import com.setof.connectly.module.order.service.status.OrderStatusProcessor;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderReturnRequestService extends AbstractOrderUpdateService<ClaimOrder> {

    public OrderReturnRequestService(
            OrderFindService orderFindService,
            OrderMapper orderMapper,
            OrderStatusProcessor orderStatusProcessor) {
        super(orderFindService, orderMapper, orderStatusProcessor);
    }

    @Override
    public List<UpdateOrderResponse> updateOrder(ClaimOrder claimOrder) {
        checkRaffleOrderProduct(claimOrder.getOrderId());
        Order order = fetchOrder(claimOrder.getOrderId());

        if (!order.isAvailableRefundOrder())
            throw new InvalidOrderStatusException(
                    order.getId(), order.getOrderStatus(), claimOrder.getOrderStatus());

        if (order.getOrderStatus().isSettlementProcessing()) {
            Optional<OrderHistoryResponse> orderHistoryResponse =
                    fetchOrderHistory(order.getId(), order.getOrderStatus());
            orderHistoryResponse.ifPresent(
                    o -> {
                        LocalDateTime updateDate = o.getUpdateDate();
                        Duration duration = Duration.between(updateDate, LocalDateTime.now());
                        if (duration.toDays() >= 7) {
                            throw new ExpireReturnRequestException();
                        }
                    });
        }

        OrderStatus pastOrderStatus = order.getOrderStatus();
        Order updatedOrder = updateOrderStatus(order, claimOrder.getOrderStatus());

        return Collections.singletonList(
                toUpdateOrderResponse(
                        pastOrderStatus,
                        updatedOrder,
                        claimOrder.getChangeReason(),
                        claimOrder.getChangeDetailReason()));
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.RETURN_REQUEST;
    }
}
