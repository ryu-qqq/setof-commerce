package com.setof.connectly.module.order.service.query;

import com.setof.connectly.module.order.dto.order.CreateOrder;
import com.setof.connectly.module.order.dto.order.OrderSheet;
import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.mapper.OrderMapper;
import com.setof.connectly.module.order.repository.OrderRepository;
import com.setof.connectly.module.order.service.query.history.OrderHistoryQueryService;
import com.setof.connectly.module.product.service.stock.StockReservationService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderCreateServiceImpl implements OrderCreateService {
    private final OrderMapper orderMapper;
    private final StockReservationService stockReservationService;
    private final OrderRepository orderRepository;
    private final OrderHistoryQueryService orderHistoryQueryService;

    @Override
    public Order issueOrder(CreateOrder createOrder) {
        Order order = orderMapper.toEntity(createOrder);
        Order savedOrder = saveOrder(order);
        stockReservationService.stockReserve(savedOrder);
        return savedOrder;
    }

    @Override
    public List<Order> issueOrders(List<? extends OrderSheet> orders) {
        List<Order> collect =
                orders.stream().map(orderMapper::toEntity).collect(Collectors.toList());
        List<Order> savedOrders = saveOrders(collect);
        stockReservationService.stocksReserve(savedOrders);
        return savedOrders;
    }

    protected Order saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        orderHistoryQueryService.saveOrderHistory(savedOrder);
        return savedOrder;
    }

    protected List<Order> saveOrders(List<Order> orders) {
        List<Order> savedOrders = orderRepository.saveAll(orders);
        orderHistoryQueryService.saveOrderHistories(savedOrders);
        return savedOrders;
    }
}
