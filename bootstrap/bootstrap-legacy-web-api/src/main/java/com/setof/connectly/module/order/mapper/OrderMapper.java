package com.setof.connectly.module.order.mapper;

import com.setof.connectly.module.order.dto.OrderCountDto;
import com.setof.connectly.module.order.dto.fetch.OrderResponse;
import com.setof.connectly.module.order.dto.fetch.UpdateOrderResponse;
import com.setof.connectly.module.order.dto.order.OrderSheet;
import com.setof.connectly.module.order.dto.slice.OrderSlice;
import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.entity.order.OrderHistory;
import com.setof.connectly.module.order.enums.OrderStatus;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

public interface OrderMapper {

    Order toEntity(OrderSheet createOrder);

    OrderHistory toHistoryEntity(Order order);

    OrderSlice<OrderResponse> toSlice(
            List<OrderResponse> orderResponses, Pageable pageable, List<OrderCountDto> orderCounts);

    List<OrderCountDto> setOrderCount(Map<OrderStatus, Long> orderStatusLongMap);

    UpdateOrderResponse toUpdateOrderResponse(
            OrderStatus pastOrderStatus,
            Order order,
            String changeReason,
            String changeDetailReason);
}
