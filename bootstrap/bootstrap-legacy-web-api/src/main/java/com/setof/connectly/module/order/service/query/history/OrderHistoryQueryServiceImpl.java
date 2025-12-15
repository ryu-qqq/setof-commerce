package com.setof.connectly.module.order.service.query.history;

import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.entity.order.OrderHistory;
import com.setof.connectly.module.order.mapper.OrderMapper;
import com.setof.connectly.module.order.repository.history.OrderHistoryJdbcRepository;
import com.setof.connectly.module.order.repository.history.OrderHistoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderHistoryQueryServiceImpl implements OrderHistoryQueryService {

    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderHistoryJdbcRepository orderHistoryJdbcRepository;
    private final OrderMapper orderMapper;

    @Override
    public void saveOrderHistory(Order order) {
        OrderHistory orderHistory = orderMapper.toHistoryEntity(order);
        orderHistoryRepository.save(orderHistory);
    }

    @Override
    public void saveOrderHistories(List<Order> orders) {
        List<OrderHistory> orderHistories =
                orders.stream().map(orderMapper::toHistoryEntity).collect(Collectors.toList());
        saveOrderHistoryEntities(orderHistories);
    }

    @Override
    public void saveOrderHistoryEntities(List<OrderHistory> orderHistories) {
        orderHistoryJdbcRepository.saveAll(orderHistories);
    }
}
