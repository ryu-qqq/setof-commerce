package com.connectly.partnerAdmin.module.order.mapper;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.order.dto.SettlementResponse;
import com.connectly.partnerAdmin.module.order.dto.response.OrderListResponse;

public interface OrderPageableMapper {

    CustomPageable<OrderListResponse> toOrderResponse(List<OrderListResponse> orderResponses, Pageable pageable, long total);
    CustomPageable<SettlementResponse> toSettlementResponse(List<SettlementResponse> settlementResponse, Pageable pageable, long total);
}
