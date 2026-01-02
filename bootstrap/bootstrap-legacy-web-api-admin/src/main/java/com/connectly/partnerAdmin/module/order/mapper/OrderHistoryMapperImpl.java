package com.connectly.partnerAdmin.module.order.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.order.dto.OrderHistoryResponse;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.entity.OrderHistory;
import com.connectly.partnerAdmin.module.shipment.entity.Shipment;

@Component
public class OrderHistoryMapperImpl implements OrderHistoryMapper{
    @Override
    public List<OrderHistoryResponse> toOrderHistories(Order order) {

        List<OrderHistory> histories = order.getHistories();
        return histories.stream().map(orderHistory -> {

            OrderHistoryResponse.OrderHistoryResponseBuilder orderHistoryResponseBuilder = OrderHistoryResponse.builder()
                    .orderId(order.getId())
                    .orderStatus(orderHistory.getOrderStatus())
                    .changeReason(orderHistory.getChangeReason())
                    .changeDetailReason(orderHistory.getChangeDetailReason());

            if(orderHistory.getOrderStatus().isDeliveryProcessing()){
                Shipment shipment = order.getShipment();
                orderHistoryResponseBuilder
                        .shipmentCompanyCode(shipment.getCompanyCode())
                        .invoiceNo(shipment.getInvoiceNo());
            }

            if(orderHistory.getOrderStatus().isSettlementProcessing()){
                orderHistoryResponseBuilder.updateDate(order.getSettlement().getExpectedSettlementDate());
            }else{
                orderHistoryResponseBuilder.updateDate(orderHistory.getInsertDate());
            }

            return orderHistoryResponseBuilder.build();

        }).toList();
    }
}
