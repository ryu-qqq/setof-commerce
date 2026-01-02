package com.connectly.partnerAdmin.module.external.service.order.oco;

import com.connectly.partnerAdmin.module.external.annotation.RequiresAccessToken;
import com.connectly.partnerAdmin.module.external.client.OcoClient;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderUpdate;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrder;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrderWrapper;
import com.connectly.partnerAdmin.module.external.dto.order.oco.query.OcoNormalOrder;
import com.connectly.partnerAdmin.module.external.enums.oco.OcoOrderStatus;
import com.connectly.partnerAdmin.module.external.handler.OcoResponseHandler;
import com.connectly.partnerAdmin.module.order.dto.query.NormalOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.order.service.OrderFetchService;
import org.springframework.stereotype.Service;

@Service
public class OcoOrderDeliveryPendingService  extends AbstractOcoOrderUpdateService<NormalOrder> {

    public OcoOrderDeliveryPendingService(OcoClient ocoClient, OcoResponseHandler<OcoOrderWrapper> ocoResponseHandler, OrderFetchService orderFetchService) {
        super(ocoClient, ocoResponseHandler, orderFetchService);
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.DELIVERY_PENDING;
    }

    @RequiresAccessToken(siteName = SiteName.OCO)
    @Override
    public void updateOrder(ExMallOrderUpdate<NormalOrder> exMallOrderUpdate) {
        OcoOrder ocoOrder = fetchOcoOrder(exMallOrderUpdate.getExMallOrderId());

        long otid = getOtid(ocoOrder, exMallOrderUpdate);
        OcoNormalOrder ocoNormalOrder = new OcoNormalOrder(otid, OcoOrderStatus.S.name());
        updateOcoOrder(ocoNormalOrder);
    }


}
