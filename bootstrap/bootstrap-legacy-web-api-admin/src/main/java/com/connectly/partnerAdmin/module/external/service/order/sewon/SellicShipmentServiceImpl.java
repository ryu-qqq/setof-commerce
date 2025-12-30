package com.connectly.partnerAdmin.module.external.service.order.sewon;


import com.connectly.partnerAdmin.module.external.client.SeWonClient;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderUpdate;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicClaimOrderResponse;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicShipmentInfo;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicShipmentRequestDto;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicShipmentResponseDto;
import com.connectly.partnerAdmin.module.external.enums.sellic.SellicShipmentCompanyCode;
import com.connectly.partnerAdmin.module.external.handler.SewonResponseHandler;
import com.connectly.partnerAdmin.module.order.dto.query.ShipOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SellicShipmentServiceImpl extends AbstractSellicOrderUpdateService<ShipOrder> {

    public SellicShipmentServiceImpl(SeWonClient seWonClient, SewonResponseHandler<List<SellicClaimOrderResponse>> sewonResponseHandler, SewonResponseHandler<List<SellicShipmentResponseDto>> sewonShipmentResponseHandler) {
        super(seWonClient, sewonResponseHandler, sewonShipmentResponseHandler);
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.DELIVERY_PROCESSING;
    }

    @Override
    public void updateOrder(ExMallOrderUpdate<ShipOrder> exMallOrderUpdate) {
        long exMallOrderId = exMallOrderUpdate.getExMallOrderId();
        checkSellicClaimOrder(exMallOrderId);

        ShipOrder shipOrder = exMallOrderUpdate.getUpdateOrder();

        SellicShipmentCompanyCode shipmentCompanyCode = SellicShipmentCompanyCode.of(shipOrder.getShipmentInfo().getCompanyCode());
        SellicShipmentInfo sellicShipmentInfo = new SellicShipmentInfo(exMallOrderId, shipmentCompanyCode.getCode(), shipOrder.getShipmentInfo().getInvoiceNo());
        SellicShipmentRequestDto sellicShipmentRequestDto = new SellicShipmentRequestDto(Collections.singletonList(sellicShipmentInfo));

        updateSellicOrderShipment(sellicShipmentRequestDto);
    }
}

