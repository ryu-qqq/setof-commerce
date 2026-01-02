package com.connectly.partnerAdmin.module.order.mapper;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.order.dto.SettlementResponse;
import com.connectly.partnerAdmin.module.order.dto.response.OrderListResponse;

@Component
public class OrderPageableMapperImpl implements OrderPageableMapper{

    @Override
    public CustomPageable<OrderListResponse> toOrderResponse(List<OrderListResponse> orderResponses, Pageable pageable, long total) {
        Long lastDomainId = orderResponses.isEmpty() ? null : orderResponses.getLast().getOrderId();
        return new CustomPageable<>(orderResponses, pageable, total, lastDomainId);
    }

    @Override
    public CustomPageable<SettlementResponse> toSettlementResponse(List<SettlementResponse> settlementResponse, Pageable pageable, long total) {
        return new CustomPageable<>(settlementResponse, pageable, total, null);
    }
}
