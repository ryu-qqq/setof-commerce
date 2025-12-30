package com.connectly.partnerAdmin.module.order.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.order.dto.OrderHistoryResponse;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.exception.OrderNotFoundException;
import com.connectly.partnerAdmin.module.order.mapper.OrderHistoryMapper;
import com.connectly.partnerAdmin.module.order.repository.OrderFetchRepository;
import com.connectly.partnerAdmin.module.utils.SecurityUtils;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderHistoryFetchServiceImpl implements OrderHistoryFetchService{

    private final OrderFetchRepository orderFetchRepository;
    private final OrderHistoryMapper orderHistoryMapper;

    @Override
    public List<OrderHistoryResponse> fetchOrderHistories(long orderId) {
        Order order = orderFetchRepository.fetchOrderEntity(orderId, SecurityUtils.currentSellerIdOpt())
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return orderHistoryMapper.toOrderHistories(order);
    }

    @Override
    public List<OrderHistoryResponse> fetchOrderHistories(List<Long> orderIds) {
        return orderFetchRepository.fetchOrderHistoryEntities(orderIds);
    }


}
