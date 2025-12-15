package com.setof.connectly.module.order.service.query.update;

import com.setof.connectly.module.order.dto.fetch.UpdateOrderResponse;
import com.setof.connectly.module.order.dto.query.UpdateOrder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderQueryServiceImpl<T extends UpdateOrder> implements OrderQueryService<T> {

    private final OrderUpdateStrategy<T> orderUpdateStrategy;

    @Override
    public List<UpdateOrderResponse> updateOrder(T dto) {
        OrderUpdateService<T> serviceByOrderStatus = orderUpdateStrategy.get(dto.getOrderStatus());
        return serviceByOrderStatus.updateOrder(dto);
    }
}
