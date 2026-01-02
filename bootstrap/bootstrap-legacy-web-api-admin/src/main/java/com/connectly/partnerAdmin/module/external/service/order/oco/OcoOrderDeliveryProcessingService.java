package com.connectly.partnerAdmin.module.external.service.order.oco;

import com.connectly.partnerAdmin.module.external.annotation.RequiresAccessToken;
import com.connectly.partnerAdmin.module.external.client.OcoClient;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderUpdate;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrder;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrderWrapper;
import com.connectly.partnerAdmin.module.external.dto.order.oco.query.OcoShipOrder;
import com.connectly.partnerAdmin.module.external.enums.oco.OcoOrderStatus;
import com.connectly.partnerAdmin.module.external.enums.oco.OcoShipmentCompanyCode;
import com.connectly.partnerAdmin.module.external.handler.OcoResponseHandler;
import com.connectly.partnerAdmin.module.order.dto.query.ShipOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.order.service.OrderFetchService;
import com.connectly.partnerAdmin.module.shipment.dto.ShipmentInfo;
import org.springframework.stereotype.Service;

@Service
public class OcoOrderDeliveryProcessingService extends AbstractOcoOrderUpdateService<ShipOrder> {


    public OcoOrderDeliveryProcessingService(OcoClient ocoClient, OcoResponseHandler<OcoOrderWrapper> ocoResponseHandler, OrderFetchService orderFetchService) {
        super(ocoClient, ocoResponseHandler, orderFetchService);
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.DELIVERY_PROCESSING;
    }

    @RequiresAccessToken(siteName = SiteName.OCO)
    @Override
    public void updateOrder(ExMallOrderUpdate<ShipOrder> exMallOrderUpdate) {
        OcoOrder ocoOrder = fetchOcoOrder(exMallOrderUpdate.getExMallOrderId());
        long otid = getOtid(ocoOrder, exMallOrderUpdate);

        OcoShipOrder ocoShipOrder = new OcoShipOrder(otid, OcoOrderStatus.I.name(), getCompanyCode(exMallOrderUpdate), getInvoice(exMallOrderUpdate));
        updateOcoOrder(ocoShipOrder);
    }

    private int getCompanyCode(ExMallOrderUpdate<ShipOrder> exMallOrderUpdate){
        ShipmentInfo shipmentInfo = exMallOrderUpdate.getUpdateOrder().getShipmentInfo();
        return OcoShipmentCompanyCode.of(shipmentInfo.getCompanyCode()).getCode();
    }

    private String getInvoice(ExMallOrderUpdate<ShipOrder> exMallOrderUpdate){
        return exMallOrderUpdate.getUpdateOrder().getShipmentInfo().getInvoiceNo();
    }
}
